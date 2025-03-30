package com.myorg;

import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
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

        Function getProductByIdLambdaFunction = Function.Builder.create(this, "GetProductByIdLambda")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.lambda.GetProductById::handleRequest")
                .code(Code.fromAsset("target/backend-0.1.jar"))
                .build();

        RestApi api = RestApi.Builder.create(this, "ShopApi")
            .restApiName("ShopApi")
            .build();

        // Tạo resource "/products" - get
        Resource productsResource = api.getRoot().addResource("products");
        productsResource.addMethod("GET", new LambdaIntegration(getProductLambdaFuntionc));

        // Tạo resource "/products/{id}" - get by id
        Resource productByIdResource = productsResource.addResource("{id}");
        productByIdResource.addMethod("GET", new LambdaIntegration(getProductByIdLambdaFunction));
    }
}
