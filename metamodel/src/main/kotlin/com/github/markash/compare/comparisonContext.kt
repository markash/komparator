package com.github.markash.compare

import org.apache.metamodel.DataContext

class CompareContext (val first: Connection, val second: Connection, mapping: (DataContext, DataContext) -> Mapping) {

    val dataContexts: Pair<DataContext, DataContext> by lazy { Pair(first.connect(), second.connect()) }

    val mapping: Mapping by lazy { dataContexts.run { mapping.invoke(this.first, this.second) } }
}