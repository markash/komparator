package io.threesixty.compare.result;

import io.threesixty.compare.DataRecord;

import java.io.Serializable;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface ResultRecord extends Comparable<ResultRecord>, Serializable {
    /**
     * The result type, i.e. EQUAL, MISMATCH, TARGET_UNMATCHED or SOURCE_UNMATCHED
     * @return The result type
     */
    ResultType getResultType();
    /**
     * The record of the result
     * @return The record
     */
    DataRecord getRecord();
}
