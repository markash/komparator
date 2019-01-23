package com.github.markash.compare

import org.apache.metamodel.data.DataSet

data class ResultSet(val dataSet: DataSet, val mapping: Mapping) {

    fun compare(block: (List<Difference>) -> Unit, filter: (Difference) -> Boolean) {

        while (dataSet.next()) {

            val differences = mapping
                    .items
                    .map { mapping ->  mapping.applyTo(dataSet.row) }
                    .filter { x -> filter.invoke(x) }

            if (differences.isNotEmpty()) {
                block.invoke(differences)
            }
        }
    }
}