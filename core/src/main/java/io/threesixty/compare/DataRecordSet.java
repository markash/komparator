package io.threesixty.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecordSet {
    private List<DataRecordColumn> columns = new ArrayList<>();
    private List<DataRecord> records = new ArrayList<>();

    public DataRecordSet() { }

    public DataRecordSet(List<DataRecord> records) {
        this.records = records;
        this.columns = inferFrom(records);
    }

    public DataRecordSet(List<DataRecordColumn> columns, List<DataRecord> records) {
        this.columns = columns;
        this.records = records;
    }

    public void add(final DataRecord dataRecord) { this.records.add(dataRecord); }
    //public void add(final DataRecordColumn column) { this.columns.add(column); }

    public DataRecordSet addAll(final DataRecordSet dataRecordSet) {
        this.columns.addAll(dataRecordSet.columns);
        this.records.addAll(dataRecordSet.records);
        return this;
    }

    public List<DataRecordColumn> getColumns() { return columns; }
    public List<DataRecord> getRecords() { return records; }

    /**
     * Converts the Records into a List of Maps of the DataRecord key and value attributes
     * @return A list where the DataRecord has been converted into a Map<String, Object>
     */
    public List<Map<String, Object>> toDataRecordMap() {
        return getRecords().stream().map(DataRecord::toMap).collect(Collectors.toList());
    }

    /**
     * Infer the data record column from the first data record's attributes
     * @param records The data records
     * @return The data record columns
     */
    private List<DataRecordColumn> inferFrom(final List<DataRecord> records) {
        return records.stream()
                .limit(1)
                .map(DataRecord::getAttributes)
                .flatMap(stream -> stream)
                .map(attribute -> new DataRecordColumn(attribute.getName(), String.class, attribute.isKey()))
                .collect(Collectors.toList());
    }
}
