package io.threesixty.kt.core;

import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jooq.lambda.Seq;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth
 */
@FullOGNL
@RunWith(ConcordionRunner.class)
public class ComparisonFixture {

    public List<Map<String, Object>> compare(final String source, final String target) {
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
        DataRecordSet sourcePersons = reader.read(sourceConfig, this.getClass().getResourceAsStream("/" + source));
        DataRecordSet targetPersons = reader.read(targetConfig, this.getClass().getResourceAsStream("/" + target));

        ComparisonService service = new ComparisonService();
        return Seq.ofType(service.convert(service.compare(sourcePersons, targetPersons, attributeMapping, true)).stream(), DifferenceRecord.class)
                    .map(differenceRecord -> differenceRecord.toMap())
                    .collect(Collectors.toList());
    }
}
