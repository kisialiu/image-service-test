package com.vova

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener

@SpringBootApplication
class ImageServiceApp : ApplicationListener<ApplicationReadyEvent> {
    /**
     * ImageService is used to serve images
     */

    override fun onApplicationEvent(p0: ApplicationReadyEvent) {
        print("ImageService is running")
    }

}

fun main(args: Array<String>) {
    runApplication<ImageServiceApp>(*args)
}
