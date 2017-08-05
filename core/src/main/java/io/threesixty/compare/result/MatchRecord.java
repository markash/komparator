package io.threesixty.compare.result;

import io.threesixty.compare.Attribute;
import io.threesixty.compare.AttributeJoiner;
import io.threesixty.compare.AttributeNameJoiner;
import io.threesixty.compare.DataRecord;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class MatchRecord extends AbstractResultRecord {
    private final DataRecord targetRecord;
    private final List<Tuple2<Attribute, Attribute>> differences;

    protected MatchRecord(final DataRecord sourceRecord,
                          final DataRecord targetRecord,
                          final List<Tuple2<Attribute, Attribute>> differences,
                          final ResultType resultType) {
        super(resultType, sourceRecord);
        this.targetRecord = targetRecord;
        this.differences = differences;
    }

    public DataRecord getTargetRecord() { return targetRecord; }
    public List<Tuple2<Attribute, Attribute>> getDifferences() { return differences; }

    /**
     * Compares the source attributes to the target attributes using a attribute name joiner that matches attributes
     * that match (i.e. Age -> AGE) but not mismatched attributes (i.e. Name -> FirstName)
     * @param source The source data record
     * @param target The target data record
     * @return The match record results
     */
    public static MatchRecord differences(final DataRecord source, final DataRecord target) {
        return differences(source, target, new AttributeNameJoiner());
    }

    /**
     * Compares the source attributes to the target attributes using a attribute mapping joiner that matches attributes
     * according to a mapping. Note that only attributes that are mapped are compared for differences.
     * @param source The source data record
     * @param target The target data record
     * @return The match record results
     */
    public static MatchRecord differences(final DataRecord source, final DataRecord target, final AttributeJoiner attributeJoiner) {
        List<Tuple2<Attribute, Attribute>> differences = source.difference(target, attributeJoiner);
        return new MatchRecord(source, target, differences, differences.size() == 0 ? ResultType.EQUAL : ResultType.MISMATCH);
    }

    /**
     * Whether there are differences in the matched record
     * @return True for differences
     */
    public boolean hasDifferences() {
        return this.differences.size() > 0;
    }

    @Override
    public String toString() {
        return "MatchRecord{" +
                "sourceRecord=" + getRecord() +
                ", targetRecord=" + targetRecord +
                ", result=" + getResultType() +
                ", differences=" + differences +
                '}';
    }
}
