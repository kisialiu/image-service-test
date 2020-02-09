package com.vova.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream

@Component
class AWSS3Repository(var client: AmazonS3,
                      @Value("\${aws.s3Endpoint}") var bucketName: String) {

    fun downloadObject(name: String): S3ObjectInputStream {
        val s3Object: S3Object = client.getObject(bucketName, name)
        return s3Object.objectContent
    }

    fun uploadObject(path: String, name: String): PutObjectResult = client.putObject(
            bucketName,
            path,
            File(name)
    )

    fun uploadObjectStream(path: String, stream: InputStream, objectMetadata: ObjectMetadata): PutObjectResult = client.putObject(
            bucketName,
            path,
            stream,
            objectMetadata
    )

    fun listObjects(): ObjectListing = client.listObjects(bucketName)

    fun deleteObject(name: String) = client.deleteObject(bucketName, name)

    fun deleteObjects(objList: ArrayList<String>) {
        val objkeyArr = arrayOfNulls<String>(objList.size)
        objList.toArray(objkeyArr);
        val deleteRequest = DeleteObjectsRequest(bucketName).withKeys(*objkeyArr)
        client.deleteObjects(deleteRequest)
    }

}