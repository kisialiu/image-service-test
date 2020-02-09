# Image-service

## Description
The server is used to serve images from AWS S3
## Setup
Please make sure the following is installed
* Java 8
* Git
* Maven
There are several properties that can be configured for image-server. They are located in application.yml file.
* source.rootUrl - root url for image source
* aws.s3Endpoint - S3 bucket name. Need to be changed in order to be able to run the application for other AWS account
* aws.accessKey - accessKey for AWS account were S3 bucket is located
* aws.secretKey - secretKey for AWS account were S3 bucket is located
## How to run
### IntelliJ IDEA
* Clone github repo
* Import project to IDE
* Run com.vova.ImageServiceAppKr with environment variables:
    * aws.accessKey={your_aws_access_key}
    * aws.secretKey={your_aws_secret_key}
* image-server is available on localhost:8080
### Console
* Clone github repo
* Go to cloned repo
* Execute ```mvn spring-boot:run -Dspring-boot.run.arguments=--Daws.secretKey={your_aws_secret_key},--aws.accessKey={your_aws_access_key}```
* image-server is available on localhost:8080