package com.github.markash.compare

import arrow.core.None
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

            val diff = comparison(left, right).partition { attr -> attr.value.isResultType(ResultType.EQUAL) }

            it("should contain have 2 differences") {
                val mismatch = diff.second

                val differences = listOf(
                        Attribute.from("1", "Name", Difference(Some("Mark"), Some("Markus"), ResultType.UNEQUAL)),
                        Attribute.from("2", "Age", Difference(Some(41), Some(42), ResultType.UNEQUAL))
                )
                mismatch.should.size.be.equal(2)
                mismatch.should.contain(differences[0])
                mismatch.should.contain(differences[1])
            }

            it ("should contain 4 attributes that are equal") {
                val equal = diff.first
                val similar = listOf(
                        Attribute.from("1", "Age", Difference(Some(43), Some(43), ResultType.EQUAL)),
                        Attribute.from("2", "Name", Difference(Some("Natasja"), Some("Natasja"), ResultType.EQUAL))
                )
                equal.should.size.be.equal(4)
                equal.should.contain(similar[0])
                equal.should.contain(similar[1])
            }
        }
    }

    given(description = "two lists of 3 attributes but the source (i.e. left) has an extra row") {

        val left = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Mark", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 41),
                mapOf("ID!" to 4, "Name" to "Crystal", "Age" to 16)
        ))

        val right = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Mark", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 41)
        ))

        on(description = "comparison") {

            val diff = comparison(left, right).partition { attr -> attr.value.isResultType(ResultType.EQUAL) }

            it(description = "should have 3 differences for each extra row") {

                val mismatch = diff.second

                val differences = listOf(
                        Attribute.from("4", "ID", Difference(Some(4), None, ResultType.MISMATCH)),
                        Attribute.from("4", "Name", Difference(Some("Crystal"), None, ResultType.MISMATCH)),
                        Attribute.from("4", "Age", Difference(Some(16), None, ResultType.MISMATCH))
                )
                mismatch.should.size.be.equal(3)
                mismatch.should.contain(differences[0])
                mismatch.should.contain(differences[1])
                mismatch.should.contain(differences[2])
            }

            it(description = "should have 6 equal attributes") {

                val equal = diff.first
                equal.should.size.be.equal(6)
            }
        }
    }

    given(description = "two lists of 3 attributes but the reference (i.e. right) has an extra row") {

        val left = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Mark", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 41)
        ))

        val right = parse(listOf(
                mapOf("ID!" to 1, "Name" to "Mark", "Age" to 43),
                mapOf("ID!" to 2, "Name" to "Natasja", "Age" to 41),
                mapOf("ID!" to 5, "Name" to "Dillon", "Age" to 18)
        ))

        on(description = "comparison") {

            val diff = comparison(left, right).partition { attr -> attr.value.isResultType(ResultType.EQUAL) }

            it(description = "should have 3 differences for each extra row") {

                val mismatch = diff.second

                val differences = listOf(
                        Attribute.from("5", "ID", Difference(Some(5), None, ResultType.MISMATCH)),
                        Attribute.from("5", "Name", Difference(Some("Dillon"), None, ResultType.MISMATCH)),
                        Attribute.from("5", "Age", Difference(Some(18), None, ResultType.MISMATCH))
                )
                mismatch.should.size.be.equal(3)
                mismatch.should.contain(differences[0])
                mismatch.should.contain(differences[1])
                mismatch.should.contain(differences[2])
            }

            it(description = "should have 6 equal attributes") {

                val equal = diff.first
                equal.should.size.be.equal(6)
            }
        }
    }
})