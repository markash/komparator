package io.threesixty.kt.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 */
public class DataRecordSet {
    private List<DataRecordColumn> columns = new ArrayList<>();
    private List<DataRecord> records = new ArrayList<>();

    public DataRecordSet(List<DataRecordColumn> columns, List<DataRecord> records) {
        this.columns = columns;
        this.records = records;
    }

    public List<DataRecordColumn> getColumns() { return columns; }
    public List<DataRecord> getRecords() { return records; }
}
