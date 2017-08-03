package io.threesixty.kt.core;

import io.threesixty.kt.core.result.DifferenceRecord;
import io.threesixty.kt.core.result.MatchRecord;
import io.threesixty.kt.core.result.ResultRecord;
import io.threesixty.kt.core.result.UnMatchRecord;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class Comparison {

    public Stream<ResultRecord> compare(final List<DataRecord> source, final List<DataRecord> target) {
        return compare(source, target, null, false);
    }

    public Stream<ResultRecord> compare(final DataRecordSet source, final DataRecordSet target, final AttributeMapping attributeMapping) {
        return compare(source, target, attributeMapping, false);
    }

    public Stream<ResultRecord> compare(final DataRecordSet source, final DataRecordSet target, final AttributeMapping attributeMapping, final boolean includeEqualRecord) {
        return compare(source.getRecords(), target.getRecords(), attributeMapping, includeEqualRecord);
    }

    public Stream<ResultRecord> compare(final List<DataRecord> source, final List<DataRecord> target, final AttributeMapping attributeMapping) {
        return compare(source, target, attributeMapping, false);
    }

    @SuppressWarnings("unchecked")
    private Stream<ResultRecord> compare(
            final List<DataRecord> source,
            final List<DataRecord> target,
            final AttributeMapping attributeMapping,
            final boolean includeEqualRecord) {

        AttributeJoiner attributeMappingJoiner = attributeMapping == null ? new AttributeNameJoiner() : new AttributeMappingJoiner(attributeMapping);

//        Seq<MatchRecord> matchedRecords =
//                Seq.seq(source.stream())
//                .innerJoin(Seq.seq(target.stream()), DataRecord::equals)
//                .map(t -> MatchRecord.differences(t.v1, t.v2, attributeMappingJoiner));
//
//        List<ResultRecord> results = includeEqualRecord
//                ? matchedRecords.collect(Collectors.toList())
//                : matchedRecords.filter(MatchRecord::hasDifferences).collect(Collectors.toList())
//                ;
//
//        Seq.ofType(source.stream(), DataRecord.class)
//                .leftOuterJoin(Seq.ofType(target.stream(), DataRecord.class), DataRecord::equals)
//                .filter(t -> t.v2 == null)
//                .map(t -> UnMatchRecord.sourceUnmatched(t.v1))
//                .forEach(results::add);
//
//        Seq.ofType(target.stream(), DataRecord.class)
//                .leftOuterJoin(Seq.ofType(source.stream(), DataRecord.class), DataRecord::equals)
//                .filter(t -> t.v2 == null)
//                .map(t -> UnMatchRecord.targetUnmatched(t.v1, attributeMapping))
//                .forEach(results::add);

        return (Stream<ResultRecord>) Stream.of(
                matched(source, target, attributeMappingJoiner, includeEqualRecord),
                unmatched(source, target, t -> UnMatchRecord.sourceUnmatched(t.v1)),
                unmatched(target, source, t -> UnMatchRecord.targetUnmatched(t.v1, attributeMapping)))
                .flatMap(stream -> stream)
                .sorted(ResultRecord::compareTo);
    }

    private Stream<MatchRecord> matched (
            final List<DataRecord> source,
            final List<DataRecord> target,
            final AttributeJoiner attributeMappingJoiner,
            final boolean includeEqualRecord) {

        Seq<MatchRecord> matchedRecords =
                Seq.seq(source.stream())
                        .innerJoin(Seq.seq(target.stream()), DataRecord::equals)
                        .map(t -> MatchRecord.differences(t.v1, t.v2, attributeMappingJoiner));

        return includeEqualRecord
                ? matchedRecords.stream()
                : matchedRecords.filter(MatchRecord::hasDifferences).stream()
                ;
    }

    /**
     * Return the unmatched data records by left outer joining the source to the target where the keys match.
     * The map function is used to convert the tuple of data records into an un-matched record
     * @param source The source data records
     * @param target The target data records
     * @param mapFunction The function to map a tuple of data records into an un-matched record
     * @return A stream of un-matched data records
     */
    private Stream<ResultRecord> unmatched (
            final List<DataRecord> source,
            final List<DataRecord> target,
            final Function<Tuple2<DataRecord, DataRecord>, ResultRecord> mapFunction) {
        return Seq.seq(source.stream())
                .leftOuterJoin(Seq.seq(target.stream()), DataRecord::equals)
                .filter(t -> t.v2 == null)
                .map(mapFunction);
    }

    private List<DifferenceRecord> toDifferenceRecord(final List<ResultRecord> records) {
        return Seq.ofType(records.stream(), ResultRecord.class)
                .map(DifferenceRecord::new)
                .collect(Collectors.toList());
    }
}
