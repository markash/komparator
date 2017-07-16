package io.threesixty.kt.core.result;

import io.threesixty.kt.core.Key;

import java.io.Serializable;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface ResultRecord extends Comparable<ResultRecord>, Serializable {
    /**
     * The identifier of the result record that links to the Data Record
     * @return The key
     */
    Key getKey();

    /**
     * The result type, i.e. EQUAL, MISMATCH, TARGET_UNMATCHED or SOURCE_UNMATCHED
     * @return The result type
     */
    ResultType getResultType();
}
