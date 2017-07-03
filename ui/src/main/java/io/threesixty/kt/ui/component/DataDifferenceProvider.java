package io.threesixty.kt.ui.component;

import io.threesixty.kt.core.DifferenceRecord;

import java.util.List;

public interface DataDifferenceProvider {
    void setAttributeMapping();
    void setDataDifferences(List<DifferenceRecord> dataDifferences);
}