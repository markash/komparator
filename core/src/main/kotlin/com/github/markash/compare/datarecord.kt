package com.github.markash.compare

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.syntax.collections.firstOption

class AttributeSet(val attributes: List<Attribute<Any>> = listOf()) {

    /**
     * The number of attributes across all rows
     */
    fun size() = attributes.size

    /**
     * The number of records
     * @return The number of distinct row id
     */
    fun length() = attributes.distinctBy { a -> a.key.rowId }.count()

    /**
     * Compare this attribute set to the other
     * @param other The other attribute set to compare to
     * @return The list of differences
     */
    fun compare(other: AttributeSet): List<Attribute<Difference>> = comparison(attributes, other.attributes)

    /**
     * Get the specified attribute by index
     * @param index The index of the attribute
     */
    operator fun get(index: Int) = attributes[index]

    operator fun plus(attributeSet: AttributeSet): AttributeSet {

        attributes + attributeSet.attributes
        return this
    }
}

class Schema {
    private val _definitions = ArrayList<Attribute<Type>>()

    private fun internalAddAttribute(name: String, dataType: Type, key: Boolean): Attribute<Type> {
        val attribute = Attribute.from("0", name, if (key) "key" else Attribute.defaultQualifier, dataType)
        _definitions += attribute
        return attribute
    }

    /**
     * A integer attribute
     * @param name The name of the attribute
     * @param key Whether the attribute is part of the key
     * @return The data definition
     */
    fun int(name: String, key: Boolean): Schema {

        internalAddAttribute(name, IntType(), key)
        return this
    }

    /**
     * A long attribute
     * @param name The name of the attribute
     * @param key Whether the attribute is part of the key
     * @return The data definition
     */
    fun long(name: String, key: Boolean): Schema {

        internalAddAttribute(name, LongType(), key)
        return this
    }

    /**
     * A string attribute
     * @param name The name of the attribute
     * @param key Whether the attribute is part of the key
     * @return The data definition
     */
    fun varchar(name: String, key: Boolean): Schema {

        internalAddAttribute(name, StringType(), key)
        return this
    }

    /**
     * Converts a list a maps into a data record
     * @param list The list of maps
     */
    fun convert(list: List<Map<String, Any>>): AttributeSet {

        return AttributeSet(parse(list, this))
    }

    /**
     * Get the attribute by name
     * @param name The name of the attribute
     */
    fun get(name: String): Option<Attribute<Type>> {

        return _definitions
                .filter { a -> a.key.column.family == name}
                .firstOption()
    }

    /**
     * Whether the attribute is part of the key
     */
    fun isKey(name: String): Boolean = get(name).map { it.key.column.qualifier == "key" }.getOrElse { false }

    /**
     * The number of data definitions
     * @return Number of data definitions
     */
    fun size() = _definitions.size

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
                        ifSome =  { r -> parseString(r.value, value) })
    }

    private fun parseString(dataType: Option<Type>, value: String): Any = when (dataType) {
        is Some -> when(dataType.t) {
                    is IntType -> IntType.convert(value)
                    is LongType -> LongType.convert(value)
                    is StringType -> StringType.convert(value)
                    else -> value
        }
        is None -> value
    }

//    companion object {
//        private const val defaultRowId = "0"
//
//        fun <T: Any> keyDefinition(name: String, dataType: DataType<T>) = columnDefinition("$name!", dataType)
//        fun <T: Any> columnDefinition(name: String, dataType: DataType<T>) = Attribute.from(defaultRowId, name, dataType)
//    }
}

//data class DataRecord2(val definition: Schema) {


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
//}

//data class Difference(val name: String, val left: Any?, val right: Any?, val key: Boolean = false): Value() {
//    fun different(): Boolean = !(left == null && right == null) && !(left?.equals(right) ?: true)
//}

