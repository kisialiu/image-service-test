package com.vova.image

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

enum class ImageType(val type: String,
                     val height: Int,
                     val width: Int,
                     val quality: Float,
                     val imageScaleType: ImageScaleType,
                     val fillColor: String,
                     val imageFileType: ImageFileType) {
    ORIGINAL("original", 0, 0, 1f, ImageScaleType.CROP, "black", ImageFileType.JPG),
    THUMBNAIL("thumbnail", 200, 200, .5f, ImageScaleType.CROP, "black", ImageFileType.JPG),
    DETAIL_LARGE("detail-large", 600, 600, .7f, ImageScaleType.FILL, "black", ImageFileType.JPG),
    DETAIL_SMALL("detail-small", 400, 400, .5f, ImageScaleType.SKEW, "black", ImageFileType.JPG);

    companion object {
        fun getImageTypeByTypeName(typeName: String): ImageType {
            try {
                return valueOf(typeName.toUpperCase())
            } catch (exception: IllegalArgumentException) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Provided image type doesn't exist: $typeName")
            }
        }
    }
}

enum class ImageFileType(val fileType: String) {
    PNG("png"),
    JPG("jpg")
}

enum class ImageScaleType {
    CROP, FILL, SKEW
}
