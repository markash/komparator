package com.github.markash.compare

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.higherkind
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.Reader

@higherkind
sealed class Operation<A>
data class Parse(val line: String): Operation<DataRecord>()
data class Compare(val left: DataRecord, val right: DataRecord, val attributeJoin: (Pair<Attribute<Any>, Attribute<Any>>) -> Boolean): Operation<ResultRecord>()

private inline fun <T : Any, U : Any> List<T>.joinBy(collection: List<U>, filter: (Pair<T, U>) -> Boolean): List<Pair<T, List<U>>> = map { t ->
    val filtered = collection.filter { filter(Pair(t, it)) }
    Pair(t, filtered)
}

fun List<Attribute<Any>>.leftJoin (collection: List<Attribute<Any>>): List<Pair<Attribute<Any>, Option<Attribute<Any>>>> {

    return this
            .joinBy(collection) { (l, r) -> l.key == r.key }
            .map { Pair(it.first, Option.fromNullable(it.second.firstOrNull()))}
}

public fun <K, V> entryOf(pair: Pair<K, V>): Map.Entry<K, V> = java.util.Collections.singletonMap(pair.first, pair.second).entries.first()


/**
 * In the following map entry, the key is suffixed with a ! char and therefore is a key attribute which is used
 * to determine the rowId of the record
 * "ID!" to "1"
 *
 * In the following the map entry key has no special character and is a value attribute
 * "Name" to "Mark"
 *
 * @param map The key-value pairs that comprise a single record and the keys that are suffixed by a `!` character
 *            are designated as the values that should be the rowId of the record.
 */
fun parse(map: Map<String, Any>): List<Attribute<Any>> {
    val keys = map.keys
            .filter { it.endsWith("!") }.toSet()

    val rowId = map.entries
            .filter { e -> keys.contains(e.key) }
            .map { e -> e.value }
            .map { value -> if (value is Option<*>) value.getOrElse{""} else value }
            .joinToString(separator = ":") { e -> e.toString() }

    return map.map { Attribute.from(rowId, it) }
}

fun parse(map: Map<String, Any>, definition: DataDefinition): List<Attribute<Any>> {

    val convertedMap = map.entries
            .map { entry -> definition.parseValue(entry.key, entry.value)}
            .toMap()

//    fun convertToValue(value : Any): Any {
//            if (value is Option<*>) {
//                when (value) {
//                    is Some<*> -> value.fold(ifEmpty = {""}, ifSome = { v -> if (v != null) v. else "" })
//                    is None -> ""
//                }
//            } else {
//                it
//            }
//    }

    val rowId = convertedMap.entries.asSequence()
            .filter { e ->  definition.isKey(e.key) }
            .map { e -> e.value }
            .map { value -> (value as? Option<*>)?.fold({""}, { it -> it.toString()}) ?: value }
            .joinToString(separator = ":") { e -> e.toString() }

    return convertedMap
            .map { Attribute.from(rowId, it) }
            .filter { attr -> attr.isNonEmpty() }
}

fun parse(list: List<Map<String, Any>>) = list.map { parse(it) }.flatten()

fun parse(reader: Reader): List<Attribute<Any>> {
    val csvParser =
            CSVParser(
                    reader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())

    return csvParser
            .map { csv -> csv.toMap() }
            .map { map -> parse(map)  }
            .flatten()
}

fun comparison(left: List<Attribute<Any>>, right: List<Attribute<Any>>): List<Attribute<Difference>> {

    return left
            .leftJoin(right)
            .map { pair -> Attribute(pair.first.key, Option.just(Difference.from(pair))) }
}


//fun comparison(pair: Pair<DataRecord, DataRecord>, join: (Pair<Attribute, Attribute>) -> Boolean): ResultRecord {
//    val differences = pair.first.attributes
//            .joinBy(pair.second.attributes, join)
//            .map { (a, b) -> Difference(name = a.name, left = a.value, right = b.firstOrNull()?.value) }
//            .filter { a -> a.different() }
//
//    val resultType = if (differences.filter { a -> !a.key }.count() > 0) ResultType.MISMATCH else ResultType.EQUAL
//
//    return ResultRecord(
//        left = pair.first,
//        right = pair.second,
//        difference = differences,
//        result = resultType)
//}