package com.github.markash.compare

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.syntax.collections.firstOption

abstract class BaseTable {
}

class DataDefinition: BaseTable() {
    var attributes: Set<Attribute<out DataType<out Any>>> = mutableSetOf()

    fun <T: Any> add(name: String, dataType: DataType<T>, key: Boolean): Attribute<DataType<T>> {
        val attribute = Attribute.from("0", name, if (key) "key" else Attribute.defaultQualifier, dataType)
        attributes += attribute
        return attribute
    }

    fun get(name: String): Option<Attribute<out DataType<out Any>>> {

        return attributes
                .filter { a -> a.key.column.family == name}
                .firstOption()
    }

    fun isKey(name: String): Boolean {

        return get(name).map { it.key.column.qualifier == "key" }.getOrElse { false }
    }

    /**
     * Parses the value when it is a String into the data type declared by the data definition
     * else return the value unchanged in a Pair<String, Any>
     */
    fun parseValue(name: String, value: Any): Pair<String, Any> {

        return Pair(name, if (value is String) parseString(name, value) else value)
    }

    /**
     * Parses the value when it is a String into the data type declared by the data definition
     * else return the value unchanged in a Pair<String, Any>
     */
    private fun parseString(name: String, value: String): Any {

        return get(name)
                .fold(
                        ifEmpty = { value },
                        ifSome =  { r -> parse(r.value, value) })
//                .map { column -> column.value.map { it.convert(value) } }
//                .getOrElse { value }
    }

    private fun parse(data: Option<DataType<out Any>>, value: String): Any = when (data) {
            is Some -> data.t.convert(value)
            is None -> value
    }

    companion object {
        private const val defaultRowId = "0"

        fun <T: Any> keyDefinition(name: String, dataType: DataType<T>) = columnDefinition("$name!", dataType)
        fun <T: Any> columnDefinition(name: String, dataType: DataType<T>) = Attribute.from(defaultRowId, name, dataType)
    }
}

data class DataRecord(val definition: DataDefinition): BaseTable() {


//    fun compareTo(other: DataRecord): ResultRecord {
//        return compareTo(other) { (a, b) -> a.name == b.name }
//    }
//
//    fun compareTo(other: DataRecord, attributeNameMap: Map<String, String>): ResultRecord {
//        return compareTo(other) { (a, b) ->  attributeNameMap[a.name] == b.name }
//    }
//
//    /**
//     * Compares this DataRecord to another to determine the differences
//     * @param other The other data record to compare to
//     * @param join The joining strategy, i.e. by name or attribute mapping
//     * @return A tuple of attributes that are different
//     */
//    fun compareTo(other: DataRecord, join: (Pair<Attribute, Attribute>) -> Boolean): ResultRecord {
//
//        val differences = attributes
//                .joinBy(other.attributes, join)
//                .map { (a, b) -> Difference(name = a.name, left = a.value, right = b.firstOrNull()?.value) }
//                .filter { a -> a.different() }
//
//        val resultType = if (differences.filter { a -> !a.key }.count() > 0) ResultType.MISMATCH else ResultType.EQUAL
//
//        return ResultRecord(
//                left = this,
//                right = other,
//                difference = differences,
//                result = resultType)
//    }
//
//    fun key(): Key = Key(this.attributes.filter { it.key } )
//
//    /**
//     * Demote the attribute from key to regular attribute
//     * @param attributeName The attribute name
//     * @return The new DataRecord with attribute set as a key
//     */
//    fun demote(attributeName: String): DataRecord {
//
//        return DataRecord(
//                this.attributes
//                .map { a ->
//                    if (a.name == attributeName)
//                        Attribute(name = a.name, value = a.value, key = false)
//                    else
//                        a
//                })
//    }
//
//    /**
//     * Promote the attribute from an attribute to a key attribute
//     * @param attributeName The attribute name
//     * @return The new DataRecord with attribute set as a key
//     */
//    fun promote(attributeName: String): DataRecord {
//        return DataRecord(
//                this.attributes
//                        .map { a ->
//                            if (a.name == attributeName)
//                                Attribute(name = a.name, value = a.value, key = true)
//                            else
//                                a
//                        })
//    }
}

//data class Difference(val name: String, val left: Any?, val right: Any?, val key: Boolean = false): Value() {
//    fun different(): Boolean = !(left == null && right == null) && !(left?.equals(right) ?: true)
//}

