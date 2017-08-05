package io.threesixty.kt.ui.component;

import io.threesixty.compare.DataRecord;
import io.threesixty.compare.DataRecordSet;

import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface DataRecordProvider {
    void setDataRecordSet(DataRecordSet recordSet);
    List<DataRecord> getDataRecords();
}
