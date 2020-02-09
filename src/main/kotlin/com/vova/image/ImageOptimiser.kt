package com.vova.image

import com.vova.aws.AWSS3Repository
import org.springframework.stereotype.Component
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.plugins.jpeg.JPEGImageWriteParam

@Component
class ImageOptimiser(var awsS3Repository: AWSS3Repository) {

    fun optimiseImage(imagePath: String, imageType: ImageType): ByteArrayInputStream {
        /**
         * Optimises provided image. Changes jpeg quality and scales
         */
        var bufImage: BufferedImage? = ImageIO.read(awsS3Repository.downloadObject(imagePath))

        bufImage = changeCompressionQuality(bufImage, imageType)
        bufImage = scaleImage(bufImage, imageType)

        val byteArray = ByteArrayOutputStream()
        ImageIO.write(bufImage, imageType.imageFileType.fileType, byteArray)
        return ByteArrayInputStream(byteArray.toByteArray())
    }

    fun changeCompressionQuality(image: BufferedImage?, imageType: ImageType): BufferedImage? {
        /**
         * Changes compression quality of an image
         */
        val jpegParams = JPEGImageWriteParam(null)
        jpegParams.compressionMode = ImageWriteParam.MODE_EXPLICIT
        jpegParams.compressionQuality = imageType.quality
        val imageWriter = ImageIO.getImageWritersByFormatName(imageType.imageFileType.fileType).next()
        val ba = ByteArrayOutputStream()
        val ios = ImageIO.createImageOutputStream(ba)
        imageWriter.output = ios
        imageWriter.write(null, IIOImage(image, null, null), jpegParams)
        return ImageIO.read(ByteArrayInputStream(ba.toByteArray()))
    }

    fun scaleImage(image: BufferedImage?, imageType: ImageType): BufferedImage? {
        /**
         * Scales an image
         */
        if (imageType.imageScaleType == ImageScaleType.CROP) {
            return image?.getSubimage(0, 0, imageType.width, imageType.height)
        }
        return image
    }

}