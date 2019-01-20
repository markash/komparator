package com.github.markash.compare

import org.apache.metamodel.DataContext
import org.junit.Test

class AttributeTests {


    @Test
    @Throws(Exception::class)
    fun testCsv() {

        val source = CsvConnection(Supplier.file("/source-persons.csv").toURI())
        val target = CsvConnection(Supplier.file("/target-persons.csv").toURI())

        Comparison(source, target, this::mapping).compare(this::handleDifferences)

    }

    @Test
    @Throws(Exception::class)
    fun testExcel() {

        val source = CsvConnection(Supplier.file("/source-persons.csv").toURI())
        val target = ExcelConnection(Supplier.file("/target-persons.xls").toURI())

        Comparison(source, target, this::mapping).compare(this::handleDifferences)

    }

    private fun handleDifferences(differences: List<Difference>) {

        System.out.format("%15s %15s %15s %15s", "---------------", "---------------", "---------------", "---------------")
        System.out.println()
        differences.forEach { diff ->
            System.out.format(
                    "%15s %15s %15s %15s",
                    diff.first.name,
                    diff.first.value.orElse("null"),
                    diff.second.value.orElse("null"),
                    diff.type)
            System.out.println()
        }
    }

    private fun mapping(source: DataContext, target: DataContext): Mapping {

        val sourceColumns = source.defaultSchema.tables[0].columns
        val targetColumns = target.defaultSchema.tables[0].columns

        val items = sourceColumns
                .map { sourceColumn -> mapColumn(sourceColumn, targetColumns) }
                .flatMap { it.toList() }

        return Mapping(items)
    }

    private fun mapColumn(column: org.apache.metamodel.schema.Column, columns: List<org.apache.metamodel.schema.Column>): List<ColumnMapping> {

        return columns
                .filter { otherColumn -> otherColumn.name == column.name }
                .map { otherColumn -> ColumnMapping(column, otherColumn, column.name == "id") }
    }
}
