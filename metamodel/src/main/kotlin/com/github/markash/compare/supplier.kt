package com.github.markash.compare

import java.io.File
import java.net.URISyntaxException

class Supplier {

    companion object {
        @Throws(URISyntaxException::class)
        fun file(resource: String): File {
            return File(Supplier::class.java.javaClass.getResource(resource).toURI())
        }
    }
}