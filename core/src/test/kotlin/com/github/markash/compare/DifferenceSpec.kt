package com.github.markash.compare

import arrow.core.Option
import arrow.core.Some
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object DifferenceSpec: Spek({

    given(description = "two lists of 3 attributes with 2 differences") {

        val left = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Mark", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 41)
        ))

        val right = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Markus", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 42)
        ))

        on(description = "comparison") {

            val isEqual: (Option<Difference>) -> Boolean = { it.fold({false}, { diff -> diff.result == ResultType.EQUAL}) }

            val diff = comparison(left, right).partition { attr -> isEqual(attr.value) }

            it("should contain have 2 differences") {
                val mismatch = diff.second

                val differences = listOf(
                        Attribute.from("1", "Name", Difference("Mark", Some("Markus"), ResultType.UNEQUAL)),
                        Attribute.from("2", "Age", Difference(41, Some(42), ResultType.UNEQUAL))
                )
                mismatch.should.size.be.equal(2)
                mismatch.should.contain(differences[0])
                mismatch.should.contain(differences[1])
            }

            it ("should contain 4 attributes that are equal") {
                val equal = diff.first
                val similar = listOf(
                        Attribute.from("1", "Age", Difference(43, Some(43), ResultType.EQUAL)),
                        Attribute.from("2", "Name", Difference("Natasja", Some("Natasja"), ResultType.EQUAL))
                )
                equal.should.size.be.equal(4)
                equal.should.contain(similar[0])
                equal.should.contain(similar[1])
            }
        }
    }
})