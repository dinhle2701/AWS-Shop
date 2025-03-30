package com.myorg;

import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class BackendStack extends Stack {
    public BackendStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public BackendStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


        Function getProductLambdaFuntionc = Function.Builder.create(this, "GetProductsLambda")
            .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
            .handler("com.myorg.lambda.GetProductsHandler::handleRequest")
            .code(Code.fromAsset("target/backend-0.1.jar"))
            .build();

        RestApi api = RestApi.Builder.create(this, "ShopApi")
            .restApiName("ShopApi")
            .build();
        api.getRoot().addResource("products").addMethod("GET", new LambdaIntegration(getProductLambdaFuntionc));
    }
}
