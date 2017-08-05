package io.threesixty.compare.reader;

import io.threesixty.compare.DataRecord;
import io.threesixty.compare.DataRecordConfiguration;
import io.threesixty.compare.DataRecordFileType;
import io.threesixty.compare.DataRecordSet;
import io.threesixty.compare.util.ReaderSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReaderConfiguration.class})
public class TestStreamDataRecordProvider {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testStreamProvider() throws Exception {
        DataRecordConfiguration sourceConfig =
                new DataRecordConfiguration("source-person", DataRecordFileType.DELIMITED)
                        .withId("ID", Long.class)
                        .withColumn("FIRSTNAME", String.class)
                        .withColumn("AGE", Long.class);

        StreamDataRecordProvider provider = new StreamDataRecordProvider(sourceConfig);
        Stream<DataRecord> results = provider.fetch(ReaderSupplier.forResource("/source-persons.csv"));
        results.forEach(System.out::println);
    }

    @Test
    public void testJdbcProvider() throws Exception {
        DataRecordConfiguration targetConfig = new DataRecordConfiguration("target-person", DataRecordFileType.DELIMITED)
                .withId("ID", Long.class)
                .withColumn("AGE", Long.class)
                .withColumn("NAME", String.class)
                .withColumn("SURNAME", String.class);

        JdbcDataRecordProvider provider = new JdbcDataRecordProvider(targetConfig, "SELECT * FROM TARGET_PERSON");
        DataRecordSet recordSet = provider.provide(jdbcTemplate);
        recordSet.getRecords().forEach(System.out::println);
    }
}
