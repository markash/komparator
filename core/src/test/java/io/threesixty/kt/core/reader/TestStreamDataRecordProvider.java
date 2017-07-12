package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordConfiguration;
import io.threesixty.kt.core.DataRecordFileType;
import io.threesixty.kt.core.DataRecordSet;
import io.threesixty.kt.core.util.ReaderSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.Supplier;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReaderConfiguration.class})
public class TestStreamDataRecordProvider {

    @Autowired
    private Supplier<JdbcTemplate> templateSuppler;

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
        DataRecordSet recordSet = provider.provide(templateSuppler);
        recordSet.getRecords().forEach(System.out::println);
    }
}
