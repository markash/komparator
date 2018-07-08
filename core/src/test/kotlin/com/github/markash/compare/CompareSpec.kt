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
            val def = DataDefinition()
            def.add("ID", DataType.IntegerType, true)

            val left = parse(map, def)
            val difference = left[0].compare(Some(right))

            it(description = "should be equal") {
                difference.result.should.be.equal(ResultType.EQUAL)
                difference.left.should.be.equal(left[0].value)
                difference.right.should.be.equal(right.value)
            }
        }
    }
})