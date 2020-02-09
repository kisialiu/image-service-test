package com.vova.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class AWSConfig(@Value("\${aws.accessKey}") private val accessKey: String,
                @Value("\${aws.secretKey}") private val secretKey: String) {

    @Bean
    fun credentials(): AWSCredentials = BasicAWSCredentials(
            accessKey,
            secretKey
    )

    @Bean
    fun client(credentials: AWSCredentials): AmazonS3 = AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_CENTRAL_1)
            .build()

}
