package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordSet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface DataRecordProvider<T> {

    Stream<DataRecord> fetch(final Supplier<T> supplier) throws Exception;

    default DataRecordSet provide(final Supplier<T> supplier) throws Exception {
        return fetch(supplier).collect(toDataSet());
    }

    default Collector<io.threesixty.kt.core.DataRecord, DataRecordSet, DataRecordSet> toDataSet() {
        return new Collector<io.threesixty.kt.core.DataRecord, DataRecordSet, DataRecordSet>() {
            @Override
            public Supplier<DataRecordSet> supplier() {
                return DataRecordSet::new;
            }
            @Override
            public BiConsumer<DataRecordSet, io.threesixty.kt.core.DataRecord> accumulator() {
                return (builder, t) -> builder.add(t);
            }

            @Override
            public BinaryOperator<DataRecordSet> combiner() {
                return (left, right) -> left.addAll(right);
            }
            @Override
            public Function<DataRecordSet, DataRecordSet> finisher() {
                return (builder) -> builder;
            }
            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.UNORDERED);
            }
        };
    }
}
