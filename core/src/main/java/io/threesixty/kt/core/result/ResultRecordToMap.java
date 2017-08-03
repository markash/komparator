package io.threesixty.kt.core.result;

import java.util.Map;
import java.util.function.Function;

public class ResultRecordToMap implements Function<ResultRecord, Map<String, Object>> {

    private ResultRecordToDifferenceRecord function = new ResultRecordToDifferenceRecord();

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
     * @param record The result record of the comparison
     * @return The map of the result record
     */
    public Map<String, Object> apply(final ResultRecord record) {
        return function.apply(record).toMap();
    }
}
