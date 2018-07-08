package com.github.markash.compare

//import io.threesixty.compare.DataRecordColumn
//import io.threesixty.compare.DataRecordConfiguration
//import org.jooq.lambda.Seq
//import org.jooq.lambda.tuple.Tuple2
//import java.util.*
//import java.util.function.BiPredicate
//import java.util.stream.Collectors
//import java.util.stream.Stream

///**
// * @author Mark P Ashworth (mp.ashworth@gmail.com)
// */
//class AttributeMapping (val name: String) {
//
//    private var attributeMap: MutableMap<String, String> = HashMap()
//
//    fun mapBetween(source: Stream<String>, target: Stream<String>): AttributeMapping {
//
//                Seq.seq(source)
//                        .innerJoin(Seq.seq(target), BiPredicate<String, String> { obj, anotherString -> obj.equals(anotherString, ignoreCase = true) }
//                        .collect<Map<String, String>, Any>(Collectors.toMap<Tuple2<String, String>, String, String>({ tuple -> tuple.v1 }) { tuple -> tuple.v2 })
//                        .forEach(item -> attributeMap.put(item.key, item.value))
//        return this
//    }
//
//    /**
//     * Add the source column definitions
//     * @param configuration The column configuration
//     * @return AttributeMapping
//     */
//    fun source(configuration: DataRecordConfiguration): AttributeMapping {
//        return source(configuration.columns)
//    }
//
//    /**
//     * Add the source column definitions
//     * @param columnDefinitions The column definitions
//     * @return AttributeMapping
//     */
//    fun source(columnDefinitions: List<DataRecordColumn>): AttributeMapping {
//        attributeMap.clear()
//        columnDefinitions.forEach { e -> attributeMap[e.name] = null }
//        return this
//    }
//
//    fun mapTo(configuration: DataRecordConfiguration): AttributeMapping {
//        return mapTo(configuration.columns)
//    }
//
//    fun mapTo(columnDefinitions: List<DataRecordColumn>): AttributeMapping {
//        columnDefinitions.forEach(Consumer<DataRecordColumn> { this.autoMap(it) })
//        return this
//    }
//
//    //Change to mapBetween
//    fun mapTo(source: String, target: String): AttributeMapping {
//        if (this.attributeMap.containsKey(source)) {
//            this.attributeMap.replace(source, target)
//        } else {
//            this.attributeMap[source] = target
//        }
//        return this
//    }
//
//    fun mapTo(configuration: DataRecordConfiguration, source: String, target: String): AttributeMapping {
//        val sourceKey = getSource(source)
//        val targetKey = configuration.getColumn(target)
//
//        return if (sourceKey.isPresent && targetKey.isPresent) {
//            mapTo(sourceKey.get(), targetKey.get().name)
//        } else this
//    }
//
//    fun mapTo(columnDefinitions: List<DataRecordColumn>, source: String, target: String): AttributeMapping {
//
//        val sourceKey = getSource(source)
//        val targetKey = columnDefinitions.stream().filter { e -> e.name == target }.findFirst()
//
//        return if (sourceKey.isPresent && targetKey.isPresent) {
//            mapTo(sourceKey.get(), targetKey.get().name)
//        } else this
//    }
//
//    /**
//     * Map the source to the target data record definition
//     * @param target The target column which is matched to the source by name
//     */
//    private fun autoMap(target: String) {
//        this.attributeMap.keys.stream()
//                .filter { source -> source == target }
//                .forEach { source -> mapTo(source, target) }
//    }
//
//    /**
//     * Map the source to the target data record definition
//     * @param target The target column which is matched to the source by name
//     */
//    private fun autoMap(target: DataRecordColumn) {
//        this.attributeMap.keys.stream()
//                .filter { source -> source == target.name }
//                .forEach { source -> mapTo(source, target.name) }
//    }
//
//    /**
//     * Adds a mapping between a data record attributes
//     * @param sourceAttributeName The attribute name of the source data record
//     * @param targetAttributeName The attribute name of the target data record
//     */
//    fun addMapping(sourceAttributeName: String, targetAttributeName: String) {
//        this.attributeMap[sourceAttributeName] = targetAttributeName
//    }
//
//    /**
//     * Returns the target data record attribute name for the source attribute name
//     * @param sourceAttributeName The source attribute name
//     * @return The attribute name
//     */
//    fun getMappingForSource(sourceAttributeName: String): String? {
//        return if (attributeMap.containsKey(sourceAttributeName)) {
//            attributeMap[sourceAttributeName]
//        } else null
//    }
//
//    /**
//     * Returns the source data record attribute name for the source attribute name
//     * @param targetAttributeName The source attribute name
//     * @return The attribute name
//     */
//    fun getMappingForTarget(targetAttributeName: String): String? {
//
//        return attributeMap.entries.stream()
//                .filter { entry -> entry.value == targetAttributeName }
//                .map<String>(Function<Entry<String, String>, String> { it.key })
//                .findFirst()
//                .orElse(null)
//    }
//
//    private fun getSource(name: String): Optional<String> {
//        return this.attributeMap.keys.stream().filter { e -> e == name }.findFirst()
//    }
//
//    override fun toString(): String? {
//        return this.name
//    }
//}