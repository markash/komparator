package com.github.markash.compare

import java.io.File
import java.net.URI
import java.net.URISyntaxException

class Supplier {

    companion object {
        @Throws(URISyntaxException::class)
        fun file(resource: String): File {
            return File(uri(resource))
        }

        @Throws(URISyntaxException::class)
        fun uri(resource: String): URI {
            return Supplier::class.java.javaClass.getResource(resource).toURI()
        }
    }
}