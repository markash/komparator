package io.threesixty.compare;

import io.threesixty.compare.reader.JdbcDataRecordProvider;
import io.threesixty.compare.reader.StreamDataRecordProvider;
import io.threesixty.compare.reader.ReaderConfiguration;
import io.threesixty.compare.util.ReaderSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReaderConfiguration.class})
public class TestPersonReader {

    @Autowired
    private JdbcTemplate templateSuppler;
	
//    @Test
//    public void testReader() throws Exception {
//
//        try (
//                InputStreamReader mapReader = new InputStreamReader(this.getClass().getResourceAsStream("/person.pzmap.xml"));
//                InputStreamReader dataReader = new InputStreamReader(this.getClass().getResourceAsStream("/source-persons.csv"))) {
//
//            FileDataRecordReader reader = new FileDataRecordReader();
//            List<DataRecord> persons = reader.read(mapReader, dataReader);
//            persons.forEach(person -> System.out.println("person = " + person));
//            Assert.assertEquals("Array should contain 3 persons", 3, persons.size());
//        }
//    }

//    @Test
//    public void testMatching() throws Exception {
//
//        FileDataRecordReader reader = new FileDataRecordReader();
//        List<DataRecord> sourcePersons = reader.readWithClose(this.getClass().getResourceAsStream("/person.pzmap.xml"), this.getClass().getResourceAsStream("/source-persons.csv"));
//        List<DataRecord> targetPersons = reader.read(this.getClass().getResourceAsStream("/target-persons.pzmap.xml"), this.getClass().getResourceAsStream("/target-persons.csv"));
//
//        AttributeMapping attributeMapping = new AttributeMapping();
//        attributeMapping.addMapping("ID", "ID");
//        attributeMapping.addMapping("FIRSTNAME", "NAME");
//        attributeMapping.addMapping("AGE", "AGE");
//
//        List<ResultRecord> results = new Comparison().compare(sourcePersons, targetPersons, attributeMapping);
//        results.forEach(r -> System.out.println("r = " + r));
//    }

    @Test
    public void testMatching() throws Exception {

	    DataRecordConfiguration sourceConfig =
                new DataRecordConfiguration("source-person", DataRecordFileType.DELIMITED)
                .withId("ID", Long.class)
                .withColumn("FIRSTNAME", String.class)
                .withColumn("AGE", Long.class);

        DataRecordConfiguration targetConfig = new DataRecordConfiguration("target-person", DataRecordFileType.DELIMITED)
                .withId("ID", Long.class)
                .withColumn("AGE", Long.class)
                .withColumn("NAME", String.class)
                .withColumn("SURNAME", String.class);

        AttributeMapping attributeMapping = new AttributeMapping("person-mapping")
                .source(sourceConfig)
                .mapTo(targetConfig, "ID", "ID")
                .mapTo(targetConfig, "FIRSTNAME", "NAME")
                .mapTo(targetConfig, "AGE", "AGE");

        DataRecordSet sourcePersons = new StreamDataRecordProvider(sourceConfig).provide(ReaderSupplier.forResource("/source-persons.csv"));
        DataRecordSet targetPersons = new StreamDataRecordProvider(sourceConfig).provide(ReaderSupplier.forResource("/target-persons.csv"));

        new Comparison()
                .compare(sourcePersons, targetPersons, attributeMapping)
                .forEach(r -> System.out.println("r = " + r));
    }

    @Test
    public void testDatabaseMatching() throws Exception {

        DataRecordConfiguration sourceConfig =
                new DataRecordConfiguration("source-person", DataRecordFileType.DELIMITED)
                        .withId("ID", Long.class)
                        .withColumn("FIRSTNAME", String.class)
                        .withColumn("AGE", Long.class);

        DataRecordConfiguration targetConfig = new DataRecordConfiguration("target-person", DataRecordFileType.DELIMITED)
                .withId("ID", Long.class)
                .withColumn("AGE", Long.class)
                .withColumn("NAME", String.class)
                .withColumn("SURNAME", String.class);

        AttributeMapping attributeMapping = new AttributeMapping("person-mapping")
                .source(sourceConfig)
                .mapTo(targetConfig, "ID", "ID")
                .mapTo(targetConfig, "FIRSTNAME", "NAME")
                .mapTo(targetConfig, "AGE", "AGE");


        DataRecordSet sourcePersons = new StreamDataRecordProvider(sourceConfig).provide(ReaderSupplier.forResource("/source-persons.csv"));
        DataRecordSet targetPersons = new JdbcDataRecordProvider(targetConfig, "SELECT * FROM TARGET_PERSON").provide(templateSuppler);

        new Comparison()
                .compare(sourcePersons, targetPersons, attributeMapping)
                .forEach(r -> System.out.println("r = " + r));
    }
}
