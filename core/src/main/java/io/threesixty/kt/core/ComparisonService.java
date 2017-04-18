package io.threesixty.kt.core;

import org.jooq.lambda.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth
 */
public class ComparisonService {

    public List<ResultRecord> compare(final List<DataRecord> source, final List<DataRecord> target) {
        return compare(source, target, null, false);
    }

    public List<ResultRecord> compare(final DataRecordSet source, final DataRecordSet target, final AttributeMapping attributeMapping) {
        return compare(source, target, attributeMapping, false);
    }

    public List<ResultRecord> compare(final DataRecordSet source, final DataRecordSet target, final AttributeMapping attributeMapping, final boolean includeEqualRecord) {
        return compare(source.getRecords(), target.getRecords(), attributeMapping, includeEqualRecord);
    }

    public List<ResultRecord> compare(final List<DataRecord> source, final List<DataRecord> target, final AttributeMapping attributeMapping) {
        return compare(source, target, attributeMapping, false);
    }

    public List<ResultRecord> compare(final List<DataRecord> source, final List<DataRecord> target, final AttributeMapping attributeMapping, final boolean includeEqualRecord) {

        List<ResultRecord> results = new ArrayList<>();

        Seq<MatchRecord> matchedRecords =
                Seq.ofType(source.stream(), DataRecord.class)
                .innerJoin(Seq.ofType(target.stream(), DataRecord.class), (left, right) -> left.id.equals(right.id))
                .map(t -> MatchRecord.compare(t.v1, t.v2, attributeMapping == null ? new AttributeNameJoiner() : new AttributeMappingJoiner(attributeMapping)));

        /* Filter out equal records if requested */
        if (includeEqualRecord) {
            matchedRecords
                    .forEach(results::add);
        } else {
            matchedRecords
                    .filter(MatchRecord::hasDifferences)
                    .forEach(results::add);
        }

        Seq.ofType(source.stream(), DataRecord.class)
                .leftOuterJoin(Seq.ofType(target.stream(), DataRecord.class), (left, right) -> left.id.equals(right.id))
                .filter(t -> t.v2 == null)
                .map(t -> UnMatchRecord.sourceUnmatched(t.v1))
                .forEach(results::add);

        Seq.ofType(target.stream(), DataRecord.class)
                .leftOuterJoin(Seq.ofType(source.stream(), DataRecord.class), (left, right) -> left.id.equals(right.id))
                .filter(t -> t.v2 == null)
                .map(t -> UnMatchRecord.targetUnmatched(t.v1, attributeMapping))
                .forEach(results::add);

        results.sort(ResultRecord::compareTo);
        return results;
    }

    public List<DifferenceRecord> convert(final List<ResultRecord> records) {
        return Seq.ofType(records.stream(), ResultRecord.class)
                .map(DifferenceRecord::new)
                .collect(Collectors.toList());
    }
}
