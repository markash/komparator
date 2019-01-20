package com.github.markash.compare

import org.apache.metamodel.DataContext
import org.apache.metamodel.UpdateableDataContext
import org.apache.metamodel.factory.DataContextFactoryRegistryImpl
import org.apache.metamodel.factory.DataContextPropertiesImpl
import java.io.File
import java.net.URI
import java.util.*

interface Connection {

    fun connect(): DataContext
}

sealed class AbstractConnection : Connection

data class CsvConnection(val uri: URI) : AbstractConnection() {

    override fun connect(): DataContext = getContext(uri)

    companion object {
        private fun getContext(file: File): UpdateableDataContext {
            Objects.requireNonNull(file)
            return getContext(file.toURI())
        }

        private fun getContext(uri: URI): UpdateableDataContext {
            Objects.requireNonNull(uri)

            val properties = DataContextPropertiesImpl()
            properties.put("type", "csv")
            properties.put("resource", uri)
            return DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties) as UpdateableDataContext
        }
    }
}

data class ExcelConnection(val uri: URI) : AbstractConnection() {

    override fun connect(): DataContext = getContext(uri)

    companion object {
        private fun getContext(uri: URI): UpdateableDataContext {
            Objects.requireNonNull(uri)

            val properties = DataContextPropertiesImpl()
            properties.put("type", "excel")
            properties.put("resource", uri)
            return DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties) as UpdateableDataContext
        }
    }
}

data class PojoConnection(val x: String) : AbstractConnection() {

    override fun connect(): DataContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





