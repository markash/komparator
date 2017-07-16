package io.threesixty.kt.core;

import io.threesixty.kt.core.reader.StreamDataRecordProvider;
import io.threesixty.kt.core.result.ResultRecord;
import io.threesixty.kt.core.util.ReaderSupplier;
import org.concordion.api.ConcordionResources;
import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mark P Ashworth
 */
@FullOGNL
@RunWith(ConcordionRunner.class)
@ConcordionResources( value = { "person-mapping.png", "invoice-mapping.png" } )
public class DataRecordComparisonFixture {

    public List<Map<String, Object>> compare(final String source, final String target) throws Exception {
        DataRecordConfiguration sourceConfig =
                new DataRecordConfiguration("source", DataRecordFileType.DELIMITED)
                        .withId("ID", Long.class)
                        .withColumn("FIRSTNAME", String.class)
                        .withColumn("AGE", Long.class);

        DataRecordConfiguration targetConfig =
                new DataRecordConfiguration("target", DataRecordFileType.DELIMITED)
                        .withId("ID", Long.class)
                        .withColumn("AGE", Long.class)
                        .withColumn("NAME", String.class)
                        .withColumn("SURNAME", String.class);

        AttributeMapping attributeMapping =
                new AttributeMapping("mapping")
                        /* Auto map ID and AGE between source and target */
                        .source(sourceConfig)
                        .mapTo(targetConfig)
                        /* Add the mapping for the FIRSTNAME to NAME between source and target */
                        .mapTo(sourceConfig.getColumn("FIRSTNAME").get(), targetConfig.getColumn("NAME").get());


        DataRecordSet sourcePersons = new StreamDataRecordProvider(sourceConfig).provide(ReaderSupplier.forResource("/" + source));
        DataRecordSet targetPersons = new StreamDataRecordProvider(targetConfig).provide(ReaderSupplier.forResource("/" + target));

        Comparison service = new Comparison();
        List<ResultRecord> results = service.compare(sourcePersons, targetPersons, attributeMapping, true);
        return service.toDifferenceMap(results);
    }

    public List<Map<String, Object>> readSource(final String source) throws Exception {

        return new StreamDataRecordProvider(dataRecordConfiguration(source).orElseThrow(() -> new Exception("No configuration for " + source)))
                        .provide(ReaderSupplier.forResource("/" + source))
                        .toDataRecordMap();
    }

    public List<Map<String, Object>> compoundCompare(final String source, final String target) throws Exception {

        DataRecordConfiguration config =
                new DataRecordConfiguration("invoice-config", DataRecordFileType.DELIMITED)
                    .withId("invoice_id", Long.class)
                    .withId("line_id", Long.class)
                    .withColumn("description", String.class)
                    .withColumn("amount", Double.class)
                    .withColumn("vat", Boolean.class)
                    ;

        AttributeMapping attributeMapping =
                new AttributeMapping("mapping").source(config).mapTo(config);

        DataRecordSet sourcePersons = new StreamDataRecordProvider(config).provide(ReaderSupplier.forResource("/" + source));
        DataRecordSet targetPersons = new StreamDataRecordProvider(config).provide(ReaderSupplier.forResource("/" + target));

        Comparison service = new Comparison();
        List<ResultRecord> results = service.compare(sourcePersons, targetPersons, attributeMapping, true);
        return service.toDifferenceMap(results);
    }


    private Optional<DataRecordConfiguration> dataRecordConfiguration(final String file) {

        switch(file) {
            case "source-invoice.csv":
                return Optional.of(new DataRecordConfiguration("invoice-config", DataRecordFileType.DELIMITED)
                                .withId("invoice_id", Long.class)
                                .withId("line_id", Long.class)
                                .withColumn("description", String.class)
                                .withColumn("amount", Double.class)
                                .withColumn("vat", Boolean.class));
            default: return Optional.empty();
        }
    }
}
