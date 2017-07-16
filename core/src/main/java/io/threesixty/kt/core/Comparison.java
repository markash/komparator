package io.threesixty.kt.core;

import io.threesixty.kt.core.result.DifferenceRecord;
import io.threesixty.kt.core.result.MatchRecord;
import io.threesixty.kt.core.result.ResultRecord;
import io.threesixty.kt.core.result.UnMatchRecord;
import org.jooq.lambda.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class Comparison {

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
                .innerJoin(Seq.ofType(target.stream(), DataRecord.class), DataRecord::equals)
                .map(t -> MatchRecord.differences(t.v1, t.v2, attributeMapping == null ? new AttributeNameJoiner() : new AttributeMappingJoiner(attributeMapping)));

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
                .leftOuterJoin(Seq.ofType(target.stream(), DataRecord.class), DataRecord::equals)
                .filter(t -> t.v2 == null)
                .map(t -> UnMatchRecord.sourceUnmatched(t.v1))
                .forEach(results::add);

        Seq.ofType(target.stream(), DataRecord.class)
                .leftOuterJoin(Seq.ofType(source.stream(), DataRecord.class), DataRecord::equals)
                .filter(t -> t.v2 == null)
                .map(t -> UnMatchRecord.targetUnmatched(t.v1, attributeMapping))
                .forEach(results::add);

        results.sort(ResultRecord::compareTo);
        return results;
    }

    public List<DifferenceRecord> toDifferenceRecord(final List<ResultRecord> records) {
        return Seq.ofType(records.stream(), ResultRecord.class)
                .map(DifferenceRecord::new)
                .collect(Collectors.toList());
    }

    /**
     * Convert the ResultRecords into a list map object where each map contains the attribute name and the difference for that attribute.<br/>
     * The type of difference between the source and target is stored in the resultType key:-
     * <ol>
     *     <li>EQUAL</li>
     *     <li>MISMATCH</li>
     *     <li>SOURCE_UNMATCHED</li>
     *     <li>TARGET_UNMATCHED</li>
     * </ol>
     * Example of the contents of the list with two objects that were compared:-
     * <table>
     * <tr>
     *     <td>resultType</td><td>MISMATCH</td>
     *     <td>Id</td><td>1</td>
     *     <td>Name</td><td>Mark (Marcus)</td>
     * </tr>
     * <tr>
     *     <td>resultType</td><td>TARGET_UNMATCHED</td>
     *     <td>Id</td><td>2</td>
     *     <td>Name</td><td>Shadow</td>
     * </tr>
     * </table>
     *
     * @param records
     * @return
     */
    public List<Map<String, Object>> toDifferenceMap(final List<ResultRecord> records) {
        return Seq.ofType(toDifferenceRecord(records).stream(), DifferenceRecord.class)
                .map(DifferenceRecord::toMap)
                .collect(Collectors.toList());
    }
}
