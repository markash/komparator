package io.threesixty.compare;

import io.threesixty.compare.result.ResultRecord;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecordSetComparison implements Serializable {
    private final DataRecordSet source;
    private final DataRecordSet target;
    private final AttributeMapping attributeMapping;
    private final boolean includeEqualRecord;
    private List<ResultRecord> results;

    public DataRecordSetComparison(DataRecordSet source, DataRecordSet target, AttributeMapping attributeMapping, boolean includeEqualRecord) {
        this.source = source;
        this.target = target;
        this.attributeMapping = attributeMapping;
        this.includeEqualRecord = includeEqualRecord;
    }

    public DataRecordSet getSource() { return source; }
    public DataRecordSet getTarget() { return target; }
    public AttributeMapping getAttributeMapping() { return attributeMapping; }
    public boolean isIncludeEqualRecord() { return includeEqualRecord; }
    public List<ResultRecord> getResults() { return results; }
}
