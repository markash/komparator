package com.github.markash.compare

import arrow.core.None
import arrow.core.Some
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.StringReader

object ReadSpec: Spek({
    given(description = "a map of key-values representing a single record") {
        val map = mapOf(
                "ID!" to "1",
                "Name" to "Mark",
                "Age" to "43")

        on(description = "parsing") {
            val attributes = parse(map)

            it(description = "should contain 3 attributes for 1 row") {
                attributes.should.size.equal(3)
            }
            it(description = "should contain the expected 6 attributes") {
                attributes.should.contain(Attribute.from("1", entryOf("ID" to "1")))
                attributes.should.contain(Attribute.from("1", entryOf("Name" to "Mark")))
                attributes.should.contain(Attribute.from("1", entryOf("Age" to "43")))
            }
        }
    }

    given(description = "a comma-separated value string") {

        val reader = StringReader("ID!,Name,Age\r\n1,Mark,43\n\r2,Markus,44")

        on("parsing") {
            reader.reset()
            val attributes = parse(reader)

            it(description = "should contain 6 attributes for 2 rows") {
                attributes.should.size.equal(6)
            }
            it(description = "should contain the expected 6 attributes") {
                attributes.should.contain(Attribute.from("1", entryOf("ID" to "1")))
                attributes.should.contain(Attribute.from("1", entryOf("Name" to "Mark")))
                attributes.should.contain(Attribute.from("1", entryOf("Age" to "43")))
                attributes.should.contain(Attribute.from("2", entryOf("ID" to "2")))
                attributes.should.contain(Attribute.from("2", entryOf("Name" to "Markus")))
                attributes.should.contain(Attribute.from("2", entryOf("Age" to "44")))
            }
        }

        on("subtraction") {
            println("subtraction")
        }
    }

    given(description = "a string value and an integer data definition") {
        val value = "1"

        val record = Schema()
        record.int("ID", false)

        on(description = "parsing") {

            val column: Pair<String, Any> = record.parseValue("ID", value)

            it(description = "should be equal to Some(1)") {

                column.second.should.equal(Some(1))
            }
        }
    }

    given(description = "data definition") {

        val schema = Schema()
                .int("ID", true)

        on(description = "parsing") {

            val attribute = schema.get("ID")

            it(description = "should have 1 int definition") {

                schema.size().should.be.equal(1)
                attribute.orNull()?.key?.rowId?.should?.be?.equal("0")
                attribute.orNull()?.key?.column?.family?.should?.be?.equal("ID")
                attribute.orNull()?.key?.column?.qualifier?.should?.be?.equal("key")
                attribute.orNull()?.value?.orNull()?.should?.be?.equal(IntType())
            }
        }
    }

    given(description = "a map with a string value and an integer data definition") {

        val map = mapOf("ID" to "1")

        val definition = Schema()
                .int("ID", true)
                .varchar("Name", false)
                .int("Age", false)

        on(description = "parsing") {

            val attributes = definition.convert(listOf(map))

            it(description = "should have 1 attribute with a value of Some(1)") {
                attributes.size().should.be.equal(1)
                attributes[0].value.should.be.equal(Some(1))
            }
        }
    }

    given(description = "two differences that have the same value") {
        val diff01 = Difference(Some(41), Some(42), ResultType.UNEQUAL)
        val diff02 = Difference(Some(41), Some(42), ResultType.UNEQUAL)

        on(description = "on equals") {

            val equals = diff01 == diff02

            it("should be true") {
                equals.should.be.`true`
            }
        }
    }

    given(description = "two differences that have the same none value") {
        val diff01 = Difference(Some(41), None, ResultType.MISMATCH)
        val diff02 = Difference(Some(41), None, ResultType.MISMATCH)

        on(description = "on equals") {

            val equals = diff01 == diff02

            it("should be true") {
                equals.should.be.`true`
            }
        }
    }

    given(description = "a type and a string value") {

        val type = IntType
        val value = "1"

        on(description = "convert") {
            val result = type.convert(value)

            it(description = "should be an int") {
                result.orNull()?.should?.be?.equal(1)
            }
        }
    }

    given(description = "a type and a string value that is not a valid integer") {

        val type = IntType
        val value = "1er"

        on(description = "convert") {
            val result = type.convert(value)

            it(description = "should be null") {
                result.orNull()?.should?.be?.`null`
            }
        }
    }
})