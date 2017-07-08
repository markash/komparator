package io.threesixty.kt.core;

import org.jooq.lambda.tuple.Tuple2;

import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class MatchRecord extends AbstractResultRecord {
    private DataRecord sourceRecord;
    private DataRecord targetRecord;
    private List<Tuple2<Attribute, Attribute>> differences;

    protected MatchRecord(final DataRecord sourceRecord,
                          final DataRecord targetRecord,
                          final List<Tuple2<Attribute, Attribute>> differences,
                          final ResultType resultType) {
        super(resultType);
        this.sourceRecord = sourceRecord;
        this.targetRecord = targetRecord;
        this.differences = differences;
    }

    @Override
    public Key getKey() { return getSourceRecord().getKey(); }
    public DataRecord getSourceRecord() { return sourceRecord; }
    public DataRecord getTargetRecord() { return targetRecord; }
    public List<Tuple2<Attribute, Attribute>> getDifferences() { return differences; }

    /**
     * Compares the source attributes to the target attributes using a attribute name joiner that matches attributes
     * that match (i.e. Age -> AGE) but not mismatched attributes (i.e. Name -> FirstName)
     * @param source The source data record
     * @param target The target data record
     * @return The match record results
     */
    public static MatchRecord compare(final DataRecord source, final DataRecord target) {
        return compare(source, target, new AttributeNameJoiner());
    }

    public static MatchRecord compare(final DataRecord source, final DataRecord target, final AttributeJoiner attributeJoiner) {
        List<Tuple2<Attribute, Attribute>> differences = source.compareTo(target, attributeJoiner);
        return new MatchRecord(source, target, differences, differences.size() == 0 ? ResultType.EQUAL : ResultType.MISMATCH);
    }

    public boolean hasDifferences() {
        return this.differences.size() > 0;
    }

    @Override
    public String toString() {
        return "MatchRecord{" +
                "sourceRecord=" + sourceRecord +
                ", targetRecord=" + targetRecord +
                ", result=" + getResultType() +
                ", differences=" + differences +
                '}';
    }
}
