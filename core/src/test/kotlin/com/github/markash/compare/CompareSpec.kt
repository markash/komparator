package com.github.markash.compare

import arrow.core.Some
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object CompareSpec: Spek({

    given(description = "two attributes with different keys and same values") {
        val left = Attribute.from("1", "ID", "Mark")
        val right = Attribute.from("2", "ID", "Mark")

        on(description = "compare") {

            val difference = left.compare(Some(right))

            it(description = "should be mismatched") {
                difference.result.should.be.equal(ResultType.MISMATCH)
                difference.left.should.be.equal(left.value)
                difference.right.should.be.equal(right.value)
            }
        }
    }

    given(description = "two attributes with the same key and value") {
        val left = Attribute.from("1", "ID", "Mark")
        val right = Attribute.from("1", "ID", "Mark")

        on(description = "compare") {

            val difference = left.compare(Some(right))

            it(description = "should be equal") {
                difference.result.should.be.equal(ResultType.EQUAL)
                difference.left.should.be.equal(left.value)
                difference.right.should.be.equal(right.value)
            }
        }
    }

    given(description = "two attributes with the same key but different values") {

        val left = Attribute.from("1", "ID", "Mark")
        val right = Attribute.from("1", "ID", "Markus")

        on(description = "compare") {

            val difference = left.compare(Some(right))

            it(description = "should be unmatched") {
                difference.result.should.be.equal(ResultType.UNEQUAL)
                difference.left.should.be.equal(left.value)
                difference.right.should.be.equal(right.value)
            }
        }
    }

    given(description = "two attributes with same row id but different column names") {
        val left = Attribute.from("1", "ID", "Mark")
        val right = Attribute.from("1", "Name", "Mark")

        on(description = "compare") {

            val difference = left.compare(Some(right))

            it(description = "should be mismatched because the key is on row id and column") {
                difference.result.should.be.equal(ResultType.MISMATCH)
                difference.left.should.be.equal(left.value)
                difference.right.should.be.equal(right.value)
            }
        }
    }

    given(description = "two attributes with same row id but different value data types") {

        val map = mapOf("ID" to "1")
        val right = Attribute.from("1", "ID", 1)

        on(description = "compare") {
            val def = Schema()
            def.int("ID", true)

            val left = parse(map, def)
            val difference = left[0].compare(Some(right))

            it(description = "should be equal") {
                difference.result.should.be.equal(ResultType.EQUAL)
                difference.left.should.be.equal(left[0].value)
                difference.right.should.be.equal(right.value)
            }
        }
    }

    given(description = "two attribute sets with a common schema") {

        val schema = Schema()
                .int("ID", true)
                .varchar("Name", false)
                .long("Age", false)

        val source = schema.convert(
                listOf(
                        mapOf("ID" to "1", "Name" to "Mark", "Age" to "43"),
                        mapOf("ID" to "2", "Name" to "Natasja", "Age" to "41"),
                        mapOf("ID" to "3", "Name" to "Dillon", "Age" to "18"),
                        mapOf("ID" to "4", "Name" to "Crystal", "Age" to "16")
                )
        )

        val reference = schema.convert(
                listOf(
                        mapOf("ID" to "1", "Name" to "Markus", "Age" to "44"),
                        mapOf("ID" to "2", "Name" to "Natasja", "Age" to "42"),
                        mapOf("ID" to "3", "Name" to "Dylon", "Age" to "18"),
                        mapOf("ID" to "5", "Name" to "Crystal", "Age" to "17")
                )
        )

        on(description = "comparison") {

            val difference = source.compare(reference)

            it(description = "") {

                difference.size.should.be.equal(7)

                //TODO Complete tests
            }
        }
    }
})