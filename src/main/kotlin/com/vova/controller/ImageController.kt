package com.vova.controller

import com.amazonaws.regions.Regions
import com.vova.aws.AWSS3Repository
import com.vova.image.ImageOptimiser
import com.vova.image.ImageType
import com.vova.util.S3ImageUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URL
import java.net.URLDecoder

@RestController
@RequestMapping("/image")
class ImageController(var awsS3Repository: AWSS3Repository,
                      var imageOptimiser: ImageOptimiser,
                      @Value("\${aws.s3Endpoint}") val bucketName: String,
                      @Value("\${source.rootUrl}") val rootUrl: String) {

    @GetMapping("/show/{typeName}/{seoName}", "/show/{typeName}")
    fun findImage(@PathVariable typeName: String,
                  @PathVariable(required = false) seoName: String?,
                  @RequestParam(value = "reference", required = true) fileName: String): String {
        val type = validate(typeName, fileName)

        val decodedFileName = URLDecoder.decode(fileName, "UTF-8").replace("/", "_")
        val typeS3Path = S3ImageUtil.getS3PathByType(typeName, decodedFileName)
        val originalS3Path = S3ImageUtil.getS3PathByType(ImageType.ORIGINAL.type, decodedFileName)

        when {
            !S3ImageUtil.isObjectInS3(originalS3Path, awsS3Repository) -> {
                val imageUrl = URL("$rootUrl$fileName")
                S3ImageUtil.uploadImageToS3(imageUrl.openStream(), originalS3Path, type, awsS3Repository)
                S3ImageUtil.uploadImageToS3(imageOptimiser.optimiseImage(originalS3Path, type), typeS3Path, type, awsS3Repository)
            }
            !S3ImageUtil.isObjectInS3(typeS3Path, awsS3Repository) -> {
                S3ImageUtil.uploadImageToS3(imageOptimiser.optimiseImage(originalS3Path, type), typeS3Path, type, awsS3Repository)
            }
        }

        return "https://$bucketName.s3.${Regions.EU_CENTRAL_1.name}.amazonaws.com/$typeS3Path"
    }

    @DeleteMapping("/flush/{typeName}")
    fun deleteImage(@PathVariable typeName: String,
                    @RequestParam(value = "reference", required = true) fileName: String) {
        val type = validate(typeName, fileName)

        val removeList = arrayListOf<String>()
        val decodedFileName = URLDecoder.decode(fileName, "UTF-8").replace("/", "_")
        removeList.add(S3ImageUtil.getS3PathByType(typeName, decodedFileName))
        if (type == ImageType.ORIGINAL) {
            removeList.add(S3ImageUtil.getS3PathByType(ImageType.THUMBNAIL.type, decodedFileName))
            removeList.add(S3ImageUtil.getS3PathByType(ImageType.DETAIL_LARGE.type, decodedFileName))
            removeList.add(S3ImageUtil.getS3PathByType(ImageType.DETAIL_SMALL.type, decodedFileName))
        }

        awsS3Repository.deleteObjects(removeList)
    }

    fun validate(typeName: String, fileName: String): ImageType {
        val type: ImageType = ImageType.getImageTypeByTypeName(typeName)
        if (fileName.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Reference file name is empty")
        }
        return type
    }

}

