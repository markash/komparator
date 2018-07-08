package com.github.markash.compare

import arrow.core.Option


abstract class DataType<A> {

    abstract fun convert(value: String): Option<A>

    companion object {
        val LongType: DataType<Long> =
                object: DataType<Long>() {
                    override fun convert(value: String): Option<Long> {
                        return Option.fromNullable(value.toLongOrNull())
                    }
                }

        val StringType: DataType<String> =
                object: DataType<String>() {
                    override fun convert(value: String): Option<String> {
                        return Option.just(value)
                    }
                }

        val IntegerType: DataType<Int> =
                object: DataType<Int>() {
                    override fun convert(value: String): Option<Int> {
                        val x = Option.fromNullable(value.toIntOrNull())
                        System.out.println(x)
                        return x
                    }
                }
    }
}
