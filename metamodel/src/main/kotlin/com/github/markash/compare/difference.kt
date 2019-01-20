package com.github.markash.compare

import org.apache.metamodel.schema.Column
import java.util.*

enum class DifferenceType {
    UNKNOWN,
    EQUAL,
    UNEQUAL,
    MISMATCH,
    SOURCE_UNMATCHED,
    TARGET_UNMATCHED
}

data class Attribute(val name: String, val value: Optional<String>) {

    fun compare(other: Attribute): Difference {

        val type = when (Pair(value.isPresent, other.value.isPresent)) {
            Pair(true, true) -> DifferenceType.MISMATCH
            Pair(true, false) -> DifferenceType.TARGET_UNMATCHED
            Pair(false, true) -> DifferenceType.SOURCE_UNMATCHED
            else -> DifferenceType.UNKNOWN
        }

        val difference = (value.orElse("") != other.value.orElse(""))

        return if (difference)
            Difference(this, other, type)
        else
            Difference(this, other, DifferenceType.EQUAL)
    }

    companion object {
        fun of(value: Pair<Column, Optional<String>>) = Attribute(value.first.name, value.second)
    }
}

data class Difference(val first: Attribute, val second: Attribute, val type: DifferenceType)

//val visitor = MarkupCommandVisitor()
//        StringsComparator(source.orElse(""), target.orElse("")).script.visit(visitor)
//        return visitor.markup