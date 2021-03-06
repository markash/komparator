package com.github.markash.compare

import org.apache.metamodel.DataContext

class Comparison(private val source: Connection, private val target: Connection, mapping: (DataContext, DataContext) -> Mapping) {

    private val dataContexts: Pair<DataContext, DataContext> by lazy { Pair(source.connect(), target.connect()) }

    private val mapping: Mapping by lazy { dataContexts.run { mapping.invoke(this.first, this.second) } }

    fun compare(resultsFunction: (List<Difference>) -> Unit) {

        mapping
                .leftJoin(dataSets = dataContexts.run { Pair(source.materialize(this.first), target.materialize(this.second)) })
                .compare(resultsFunction) { true }

        mapping
                .rightJoin(dataSets = dataContexts.run { Pair(source.materialize(this.first), target.materialize(this.second)) })
                .compare(resultsFunction) { difference -> difference.type == DifferenceType.SOURCE_UNMATCHED }

    }

    companion object {
//
//        fun compare(left: Connection, right: Connection, mapping: (DataContext, DataContext) -> Mapping, block: (List<Difference>) -> Unit) {
//
//
//
//            val connections = Pair(first = left, second = right)
//
//            val dataContexts = connections.run { Pair(first = this.first.connect(), second = this.second.connect()) }
//
//            val columnMapping = dataContexts.run { mapping.invoke(this.first, this.second) }
//
//
//
//            val all: (Difference) -> Boolean = { true }
//
//            val targetUnmatched : (Difference) -> Boolean = {   }
//
//            compareDataSet(leftResult, columnMapping, block, all)
//
//            compareDataSet(rightResult, columnMapping, block, targetUnmatched)
//        }
//
//
//        private fun dataSet(dataContext: DataContext): DataSet {
//            Objects.requireNonNull(dataContext)
//            val query = dataContext.query().from(dataContext.defaultSchema.getTable(0)).selectAll().toQuery()
//            return dataContext.executeQuery(query)
//        }
    }
}


//fun connect(connections: Pair<Connection, Connection> ): Pair<DataContext, DataContext> {
//
//    return connections.run { Pair(first = this.first.connect(), second = this.second.connect()) }
//}

//        private fun leftJoin(dataSets: Pair<DataSet, DataSet>, columnJoining: List<FilterItem>): DataSet {
//
//            return dataSets.run { MetaModelHelper.getLeftJoin(this.first, this.second, columnJoining.toTypedArray()) }
//        }
//
//        private fun rightJoin(dataSets: Pair<DataSet, DataSet>, columnJoining: List<FilterItem>): DataSet {
//
//            return dataSets.run { MetaModelHelper.getLeftJoin(this.second, this.first, columnJoining.toTypedArray()) }
//        }

//        private fun columnJoining(columnMapping: List<Mapping>): List<FilterItem> {
//
//            return columnMapping
//                    .filter { m -> m.key }
//                    .map { m -> FilterItem(SelectItem(m.left), OperatorType.EQUALS_TO, SelectItem(m.right)) }
//}