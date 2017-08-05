package io.threesixty.compare.result;

import io.threesixty.compare.DataRecord;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public abstract class AbstractResultRecord implements ResultRecord {
    private final ResultType resultType;
    private final DataRecord record;

    public AbstractResultRecord(final ResultType resultType, final DataRecord record) {
        this.resultType = resultType;
        this.record = record;
    }
    /**
     * Whether the record is matched, unmatched or different
     * @return The result type
     */
    public ResultType getResultType() { return resultType; }
    /**
     * The record of the result
     * @return The record
     */
    public DataRecord getRecord() { return record; }

    @Override
    public int compareTo(final ResultRecord other) {
        String otherKey = other != null && other.getRecord() != null  ? other.getRecord().getKeyAsString() : "";
        String thisKey = getRecord() != null  ? getRecord().getKeyAsString() : "";
        return thisKey.compareTo(otherKey);
    }
}
