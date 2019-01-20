package com.github.markash.compare

import org.apache.metamodel.data.DataSet

data class ResultSet(val dataSet: DataSet) {

    fun compare(columnMapping: Mapping, block: (List<Difference>) -> Unit, filter: (Difference) -> Boolean) {

        while (dataSet.next()) {

            val differences = columnMapping
                    .items
                    .map { mapping ->  mapping.applyTo(dataSet.row) }
                    .filter { x -> filter.invoke(x) }

            if (differences.isNotEmpty()) {
                block.invoke(differences)
            }
        }
    }
}