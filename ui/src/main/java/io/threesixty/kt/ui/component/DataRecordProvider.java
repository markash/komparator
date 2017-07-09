package io.threesixty.kt.ui.component;

import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordSet;

import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface DataRecordProvider {
    void setDataRecordSet(DataRecordSet recordSet);
    List<DataRecord> getDataRecords();
}
