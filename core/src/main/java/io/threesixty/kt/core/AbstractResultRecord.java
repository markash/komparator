package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth
 */
public abstract class AbstractResultRecord implements ResultRecord {
    private ResultType resultType;

    public AbstractResultRecord(ResultType resultType) {
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }

    @Override
    public int compareTo(final ResultRecord other) {
        Long otherId = other != null ? other.getId().getValue() : Long.MIN_VALUE;
        return getId().getValue().compareTo(otherId);
    }
}
