package com.github.markash.compare

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import java.time.LocalDateTime

enum class ResultType {
    UNKNOWN,
    EQUAL,
    UNEQUAL,
    MISMATCH,
    SOURCE_UNMATCHED,
    TARGET_UNMATCHED
}


sealed class Value

data class Difference(val left: Any, val right: Option<Any>, val result: ResultType): Value() {

    companion object {
        fun from(pair: Pair<Attribute<Any>, Option<Attribute<Any>>>): Difference = pair.first.compare(pair.second)

        fun isResultType(difference: Option<Difference>, resultType: ResultType): Boolean {

            return difference.fold(ifEmpty = {false}, ifSome = { diff -> diff.result == resultType})
        }
    }
}

data class Column(val family: String, val qualifier: String, val visibility: String): Value()

data class Key(val rowId: String, val column: Column, val timestamp: LocalDateTime): Value()

data class Attribute<T: Any>(val key: Key, val value: Option<T>): Value() {

    fun isEmpty(): Boolean = value.isEmpty()

    fun isNonEmpty(): Boolean = !isEmpty()

    fun <T: Any> compare(other: Option<Attribute<T>>) = Difference(this.value, (other as? Some)?.t?.value ?: None, resultType(other))

    inline fun <reified T:Any> valueOrElse(): T? = value.fold(ifEmpty = { null }, ifSome = { it }) as T?

    private fun <T: Any> resultType(other: Option<Attribute<T>>): ResultType = other.map { resultType(it) }.getOrElse { ResultType.MISMATCH }

    private fun <T: Any> resultType(other: Attribute<T>) = when {
        this == other -> ResultType.EQUAL
        this.key == other.key -> ResultType.UNEQUAL
        else -> ResultType.MISMATCH
    }

    companion object AttributeObject {
        public const val defaultVisibility = ""
        public const val defaultQualifier = ""
        public val defaultTimestamp = LocalDateTime.MIN

        private fun isKey(value: String): Boolean = value.endsWith("!")
        private fun columnName(value: String) = if (isKey(value)) value.dropLast(1) else value


        fun from(
                rowId: String,
                entry: Map.Entry<String, Any>): Attribute<Any> {

            return from(rowId, defaultQualifier, defaultVisibility, defaultTimestamp, entry)
        }

        fun <T: Any> from(
                rowId: String,
                column: String,
                value: T): Attribute<T> {

            return from(rowId, defaultQualifier, defaultVisibility, defaultTimestamp, column, value)
        }

        fun <T: Any> from(
                rowId: String,
                column: String,
                qualifier: String,
                value: T): Attribute<T> {

            return from(rowId, qualifier, defaultVisibility, defaultTimestamp, column, value)
        }

        private fun <T: Any> from(
                rowId: String,
                qualifier: String,
                visibility: String,
                timestamp: LocalDateTime,
                entry: Map.Entry<String, T>): Attribute<T> {


            return Attribute(
                    key = Key(
                            rowId = rowId,
                            column = Column(
                                    family = columnName(entry.key),
                                    qualifier = qualifier,
                                    visibility = visibility),
                            timestamp = timestamp),
                    value = wrap(entry.value))
        }

        private fun <T: Any> from(
                rowId: String,
                qualifier: String,
                visibility: String,
                timestamp: LocalDateTime,
                family: String,
                value: T): Attribute<T> {

            return Attribute(
                    key = Key(
                            rowId = rowId,
                            column = Column(
                                    family = columnName(family),
                                    qualifier = qualifier,
                                    visibility = visibility),
                            timestamp = timestamp),
                    value = wrap(value))
        }

        private fun <T: Any> wrap(value: T): Option<T> {
            return if (value is Option<*>) {
                (value as Option<T>)
            } else {
                Option.just(value)
            }
        }
    }
}

fun Option<Difference>.isResultType(resultType: ResultType): Boolean {
    return Difference.isResultType(this, resultType)
}

private inline fun <T : Any, U : Any> List<T>.joinBy(collection: List<U>, filter: (Pair<T, U>) -> Boolean): List<Pair<T, List<U>>> = map { t ->
    val filtered = collection.filter { filter(Pair(t, it)) }
    Pair(t, filtered)
}

//data class ResultRecord (val left: DataRecord, val right: DataRecord, val difference: List<Difference>, val result: ResultType): Value()