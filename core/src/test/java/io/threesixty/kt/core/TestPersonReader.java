package io.threesixty.kt.core;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

/**
 * @author Mark P Ashworth
 */

public class TestPersonReader {
	private EmbeddedDatabase db;
	
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
//            DataRecordReader reader = new DataRecordReader();
//            List<DataRecord> persons = reader.read(mapReader, dataReader);
//            persons.forEach(person -> System.out.println("person = " + person));
//            Assert.assertEquals("Array should contain 3 persons", 3, persons.size());
//        }
//    }

//    @Test
//    public void testMatching() throws Exception {
//
//        DataRecordReader reader = new DataRecordReader();
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

	    DataRecordConfiguration sourceConfig = new DataRecordConfiguration();
        sourceConfig.setFileType(DataRecordFileType.DELIMITED);
        sourceConfig.addColumn(new DataRecordColumn("ID", Long.class));
        sourceConfig.addColumn(new DataRecordColumn("FIRSTNAME", String.class));
        sourceConfig.addColumn(new DataRecordColumn("AGE", Long.class));

        DataRecordConfiguration targetConfig = new DataRecordConfiguration();
        targetConfig.setFileType(DataRecordFileType.DELIMITED);
        targetConfig.addColumn(new DataRecordColumn("ID", Long.class));
        targetConfig.addColumn(new DataRecordColumn("AGE", Long.class));
        targetConfig.addColumn(new DataRecordColumn("NAME", String.class));
        targetConfig.addColumn(new DataRecordColumn("SURNAME", String.class));

        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.addMapping("ID", "ID");
        attributeMapping.addMapping("FIRSTNAME", "NAME");
        attributeMapping.addMapping("AGE", "AGE");

        DataRecordReader reader = new DataRecordReader();
        DataRecordSet sourcePersons = reader.read(sourceConfig, this.getClass().getResourceAsStream("/source-persons.csv"));
        DataRecordSet targetPersons = reader.read(targetConfig, this.getClass().getResourceAsStream("/target-persons.csv"));

        List<ResultRecord> results = new ComparisonService().compare(sourcePersons, targetPersons, attributeMapping);
        results.forEach(r -> System.out.println("r = " + r));
    }

//    @Test
//    public void testDatabaseMatching() throws Exception {
//
//    	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
//    	List<DataRecord> sourcePersons = template.query("SELECT * FROM PERSON", new RowMapper<DataRecord>() {
//			@Override
//			public DataRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
//				return new DataRecord(
//						new Id2<Long>("ID", (Long) rs.getLong("ID")),
//						new Attribute<String>("FIRSTNAME", rs.getString("NAME")),
//						new Attribute<String>("AGE", rs.getString("AGE")));
//			}
//    	});
//
//
//    	DataRecordReader reader = new DataRecordReader();
//        List<DataRecord> targetPersons = reader.read(this.getClass().getResourceAsStream("/person.pzmap.xml"), this.getClass().getResourceAsStream("/target-persons.csv"));
//
//        List<ResultRecord> results = new ComparisonService().compare(sourcePersons, targetPersons);
//        results.forEach(r -> System.out.println("r = " + r));
//
//    }
}
