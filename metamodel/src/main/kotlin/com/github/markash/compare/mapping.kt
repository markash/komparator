package com.github.markash.compare

import org.apache.metamodel.MetaModelHelper
import org.apache.metamodel.data.DataSet
import org.apache.metamodel.data.Row
import org.apache.metamodel.query.FilterItem
import org.apache.metamodel.query.OperatorType
import org.apache.metamodel.query.SelectItem
import org.apache.metamodel.schema.Column
import java.util.*

data class ColumnMapping(val left: org.apache.metamodel.schema.Column, val right: Column, val key: Boolean) {

    fun applyTo(row: Row): Difference {
        return apply(this, row)
    }

    private fun apply(mapping: ColumnMapping, row: Row): Difference {
        val source = Pair(mapping.left, asString(row.getValue(mapping.left)))
        val target = Pair(mapping.right, asString(row.getValue(mapping.right)))

        return apply(source, target)
    }

    private fun apply(left: Pair<Column, Optional<String>>, right: Pair<Column, Optional<String>>): Difference {

        return Attribute.of(left).compare(Attribute.of(right))
    }

    private fun asString(`object`: Any?): Optional<String> {
        return if (`object` is String) {
            Optional.of((`object` as String?)!!)
        } else if (`object` != null) {
            Optional.of(`object`.toString())
        } else {
            Optional.empty()
        }
    }
}

data class Mapping(val items: List<ColumnMapping>) {

    fun join(): Join {

        val joinItems = items
                .filter { mapping -> mapping.key }
                .map { mapping -> FilterItem(SelectItem(mapping.left), OperatorType.EQUALS_TO, SelectItem(mapping.right)) }

        return Join(joinItems)
    }

    fun leftJoin(dataSets: Pair<DataSet, DataSet>): ResultSet {

        return join().left(dataSets, this)
    }

    fun rightJoin(dataSets: Pair<DataSet, DataSet>): ResultSet {

        return join().right(dataSets, this)
    }
}

enum class JoinType {
    LEFT,
    RIGHT
}

data class Join(val items: List<FilterItem>) {

    fun left(dataSets: Pair<DataSet, DataSet>, mapping: Mapping) = join(dataSets, JoinType.LEFT, items, mapping)

    fun right(dataSets: Pair<DataSet, DataSet>, mapping: Mapping) = join(dataSets, JoinType.RIGHT, items, mapping)

    companion object {
        private fun join (dataSets: Pair<DataSet, DataSet>, type: JoinType, items: List<FilterItem>, mapping: Mapping): ResultSet {

            return when(type) {
                JoinType.LEFT -> ResultSet(dataSets.run { MetaModelHelper.getLeftJoin(this.first, this.second, items.toTypedArray()) }, mapping)
                JoinType.RIGHT -> ResultSet(dataSets.run { MetaModelHelper.getRightJoin(this.first, this.second, items.toTypedArray()) }, mapping)
            }
        }
    }

}