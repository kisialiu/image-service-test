package com.vova.util

import com.amazonaws.services.s3.model.ObjectMetadata
import com.vova.aws.AWSS3Repository
import com.vova.image.ImageType
import java.io.InputStream

class S3ImageUtil {

    companion object {
        fun uploadImageToS3(imageInputStream: InputStream, s3Path: String, imageType: ImageType, awsS3Repository: AWSS3Repository) {
            /**
             * Uploads an image to provided S3 repo
             */

            val metadata = mapOf("ImageType" to imageType.imageFileType.fileType)
            val objMetadata = ObjectMetadata()
            objMetadata.userMetadata = metadata
            awsS3Repository.uploadObjectStream(s3Path, imageInputStream, objMetadata)
        }

        fun getS3PathByType(typeName: String, objectName: String): String {
            /**
             * Generates the path which will be used for image uploading to S3
             */

            val stringBuilder = StringBuilder()
            stringBuilder.append("$typeName/")
            if (objectName.length > 4) {
                stringBuilder.append("${objectName.subSequence(0, 4)}/")
            }
            if (objectName.length > 8) {
                stringBuilder.append("${objectName.subSequence(4, 8)}/")
            }
            stringBuilder.append(objectName)
            return stringBuilder.toString()
        }

        fun isObjectInS3(path: String, awsS3Repository: AWSS3Repository): Boolean {
            /**
             * Checks if an object is in S3 repo
             */

            var isFound = false
            for (os in awsS3Repository.listObjects().objectSummaries) {
                if (os.key == path) {
                    isFound = true
                }
            }
            return isFound
        }
    }

}