package io.threesixty.kt.core;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 */
public interface ResultRecord extends Comparable<ResultRecord>, Serializable {
    /**
     * The identifier of the result record that links to the Data Record
     * @return The identifier
     */
    Id2<Long> getId();

    /**
     * The result type, i.e. EQUAL, MISMATCH, TARGET_UNMATCHED or SOURCE_UNMATCHED
     * @return The result type
     */
    ResultType getResultType();
}
