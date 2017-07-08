package io.threesixty.kt.core;

import io.threesixty.kt.core.reader.FileDataRecordReader;
import io.threesixty.kt.core.reader.HsqlJdbcDataRecordReader;
import io.threesixty.kt.core.reader.ReaderConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReaderConfiguration.class})
public class TestPersonReader {
	private EmbeddedDatabase db;

	@Autowired
    private HsqlJdbcDataRecordReader jdbcReader;

	@Before
    public void setUp() {
    	db = new EmbeddedDatabaseBuilder()
    		.setType(EmbeddedDatabaseType.HSQL)
    		.addScript("schema.sql")
    		.addScript("test-data.sql")
    		.build();
    }
	
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
//        List<ResultRecord> results = new ComparisonService().compare(sourcePersons, targetPersons, attributeMapping);
//        results.forEach(r -> System.out.println("r = " + r));
//    }

    @Test
    public void testMatching() throws Exception {

	    DataRecordConfiguration sourceConfig =
                new DataRecordConfiguration("source-person", DataRecordFileType.DELIMITED)
                .withColumn("ID", Long.class)
                .withColumn("FIRSTNAME", String.class)
                .withColumn("AGE", Long.class);

        DataRecordConfiguration targetConfig = new DataRecordConfiguration("target-person", DataRecordFileType.DELIMITED)
                .withColumn("ID", Long.class)
                .withColumn("AGE", Long.class)
                .withColumn("NAME", String.class)
                .withColumn("SURNAME", String.class);

        AttributeMapping attributeMapping = new AttributeMapping("person-mapping")
                .source(sourceConfig)
                .mapTo(targetConfig, "ID", "ID")
                .mapTo(targetConfig, "FIRSTNAME", "NAME")
                .mapTo(targetConfig, "AGE", "AGE");

        FileDataRecordReader reader = new FileDataRecordReader();
        DataRecordSet sourcePersons = reader.read(sourceConfig, this.getClass().getResourceAsStream("/source-persons.csv"));
        DataRecordSet targetPersons = reader.read(targetConfig, this.getClass().getResourceAsStream("/target-persons.csv"));

        List<ResultRecord> results = new ComparisonService().compare(sourcePersons, targetPersons, attributeMapping);
        results.forEach(r -> System.out.println("r = " + r));
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


        FileDataRecordReader fileReader = new FileDataRecordReader();
        DataRecordSet sourcePersons = fileReader.read(sourceConfig, this.getClass().getResourceAsStream("/source-persons.csv"));
        DataRecordSet targetPersons = jdbcReader.read(targetConfig);

        List<ResultRecord> results = new ComparisonService().compare(sourcePersons, targetPersons, attributeMapping);
        results.forEach(r -> System.out.println("r = " + r));
    }
}
