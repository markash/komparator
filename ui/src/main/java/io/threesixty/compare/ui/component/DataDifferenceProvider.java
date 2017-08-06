package io.threesixty.compare.ui.component;

import io.threesixty.compare.result.DifferenceRecord;

import java.util.List;

public interface DataDifferenceProvider {
    void setAttributeMapping();
    void setDataDifferences(List<DifferenceRecord> dataDifferences);
}
