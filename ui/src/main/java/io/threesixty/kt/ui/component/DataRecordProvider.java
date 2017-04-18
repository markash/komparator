package io.threesixty.kt.ui.component;

import io.threesixty.kt.core.DataRecord;

import java.util.List;

/**
 * @author Mark P Ashworth
 */
public interface DataRecordProvider {
    List<DataRecord> getDataRecords();
}
