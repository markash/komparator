package com.github.markash.compare

import com.github.markash.compare.Connection.Companion.fileDataContext
import org.apache.metamodel.DataContext
import org.apache.metamodel.UpdateableDataContext
import org.apache.metamodel.data.DataSet
import org.apache.metamodel.factory.DataContextFactoryRegistryImpl
import org.apache.metamodel.factory.DataContextPropertiesImpl
import java.net.URI
import java.util.*

interface Connection {

    fun connect(): DataContext

    fun materialize(dataContext: DataContext): DataSet

    companion object {
        /**
         * Creates an updateable context using the type and resource properties
         * @param type The type of data context (i.e. cvs or excel)
         * @param uri The file uri
         */
        internal fun fileDataContext(type: String, uri: URI): UpdateableDataContext {
            Objects.requireNonNull(uri)

            val properties = DataContextPropertiesImpl()
            properties.put("type", type)
            properties.put("resource", uri)
            return DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties) as UpdateableDataContext
        }

        /**
         * Materializes the data set by selecting all from the default schema table
         * @param dataContext The data context to materialize from
         * @return The data set
         */
        fun materializeDefault(dataContext: DataContext): DataSet {
            Objects.requireNonNull(dataContext)
            val query = dataContext.query().from(dataContext.defaultSchema.getTable(0)).selectAll().toQuery()
            return dataContext.executeQuery(query)
        }
    }
}

sealed class AbstractConnection : Connection

data class CsvConnection(val uri: URI, val materializeFunction: (DataContext) -> DataSet) : AbstractConnection() {

    override fun connect(): DataContext = fileDataContext("csv", uri)

    override fun materialize(dataContext: DataContext): DataSet = materializeFunction.invoke(dataContext)
}

data class ExcelConnection(val uri: URI, val materializeFunction: (DataContext) -> DataSet) : AbstractConnection() {

    override fun connect(): DataContext = fileDataContext("excel", uri)

    override fun materialize(dataContext: DataContext): DataSet = materializeFunction.invoke(dataContext)
}

data class PojoConnection(val x: String, val materializeFunction: (DataContext) -> DataSet) : AbstractConnection() {

    override fun connect(): DataContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun materialize(dataContext: DataContext): DataSet = materializeFunction.invoke(dataContext)
}





