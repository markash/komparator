package io.threesixty.compare.excel.reader;

import io.threesixty.compare.Comparison;
import io.threesixty.compare.DataRecord;
import io.threesixty.compare.result.ResultRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelDataRecordProviderTest {

    @Test
    public void testRead() throws Exception {

        ExcelDataRecordProvider provider =
                new ExcelDataRecordProvider(FilloExt.getSqlFor("Sheet1"));

        List<DataRecord> records =
                provider.fetch(FilloExt.getConnectionForResource("/people-dataset-01.xlsx"))
                .collect(Collectors.toList());

        records.forEach(a -> a.promoteToKey("Id"));
        records.forEach(System.out::println);

        Assert.assertEquals("There should be 4 people in data set", 4, records.size());
    }

    @Test
    public void testCompare() throws Exception {

        ExcelDataRecordProvider provider =
                new ExcelDataRecordProvider(FilloExt.getSqlFor("Sheet1"));

        List<DataRecord> records1 =
                provider.fetch(FilloExt.getConnectionForResource("/people-dataset-01.xlsx"))
                        .collect(Collectors.toList());

        List<DataRecord> records2 =
                provider.fetch(FilloExt.getConnectionForResource("/people-dataset-02.xlsx"))
                        .collect(Collectors.toList());

        records1.forEach(a -> a.promoteToKey("Id"));
        records2.forEach(a -> a.promoteToKey("Id"));

        List<ResultRecord> result = new Comparison()
                .compare(records1, records2)
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }
}
