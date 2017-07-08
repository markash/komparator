package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
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
        Key otherKey = other != null && other.getKey() != null ? other.getKey() : new Key();
        return getKey().compareTo(otherKey);
    }
}
