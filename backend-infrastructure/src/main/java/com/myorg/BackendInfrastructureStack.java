package com.myorg;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;
import java.util.Map;

public class BackendInfrastructureStack extends Stack {
    public BackendInfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public BackendInfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // 🛠 Tạo S3 Bucket (Block Public Access)
        Bucket s3bucket = Bucket.Builder.create(this, "MyAWSShopS3Bucket")
                .bucketName("aws-myshop-s3-bucket")
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        // 🛠 Tạo Origin Access Control (OAC)
        CfnOriginAccessControl oac = CfnOriginAccessControl.Builder.create(this, "MyOAC")
                .originAccessControlConfig(CfnOriginAccessControl.OriginAccessControlConfigProperty.builder()
                        .name("MyCloudFrontOAC")
                        .description("OAC for CloudFront to access S3")
                        .originAccessControlOriginType("s3")
                        .signingBehavior("always")
                        .signingProtocol("sigv4")
                        .build())
                .build();

        // 🛠 CloudFront Distribution sử dụng OAC
        Distribution distribution = Distribution.Builder.create(this, "MyAWSShopDistribution")
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(s3bucket))
                        .viewerProtocolPolicy(ViewerProtocolPolicy.REDIRECT_TO_HTTPS)
                        .build())
                .build();

        // Cập nhật DistributionConfig để sử dụng OAC
        CfnDistribution cfnDistribution = (CfnDistribution) distribution.getNode().getDefaultChild();
        cfnDistribution.addPropertyOverride("DistributionConfig.Origins.0.OriginAccessControlId", oac.getAttrId());
        cfnDistribution.addPropertyOverride("DistributionConfig.Origins.0.S3OriginConfig.OriginAccessIdentity", "");

        // 🛠 Gán Bucket Policy chỉ cho phép CloudFront truy cập
        s3bucket.addToResourcePolicy(PolicyStatement.Builder.create()
                .actions(List.of("s3:GetObject"))
                .resources(List.of(s3bucket.getBucketArn() + "/*"))
                .principals(List.of(new ServicePrincipal("cloudfront.amazonaws.com")))
                .conditions(Map.of("StringEquals", Map.of(
                        "AWS:SourceArn", "arn:aws:cloudfront::" + this.getAccount() + ":distribution/" + distribution.getDistributionId()
                )))
                .build());

        // 🛠 Xuất URL CloudFront
        CfnOutput.Builder.create(this, "CloudFrontURL")
                .value("https://" + distribution.getDomainName())
                .description("CloudFront Distribution URL")
                .exportName("CloudFrontURL")
                .build();
    }
}