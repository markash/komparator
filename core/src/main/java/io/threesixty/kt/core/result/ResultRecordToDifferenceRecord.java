package io.threesixty.kt.core.result;

import java.util.function.Function;

public class ResultRecordToDifferenceRecord implements Function<ResultRecord, DifferenceRecord> {

    @Override
    public DifferenceRecord apply(final ResultRecord resultRecord) {
        return new DifferenceRecord(resultRecord);
    }
}
