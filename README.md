# Task 3 - First API with AWS API Gateway and AWS Lambda #

- **S3 URL:** [http://aws-myshop-s3-bucket.s3-website-us-east-1.amazonaws.com/](http://aws-myshop-s3-bucket.s3-website-us-east-1.amazonaws.com/)


- **CloudFront URL:** [https://d3iogd8pacgzjc.cloudfront.net/](https://d3iogd8pacgzjc.cloudfront.net/)

## Architecture ##

![alt text](image.png)

├── backend │   
├── authorization_service <- auth service repo │   
├── import_service <- import service repo 
│   └── product_service <- product service repo 
├── cart_service <- cart service repo │   
├── nodejs-aws-cart-api └── frontend <- frontend repo