package io.threesixty.compare;

import io.threesixty.compare.reader.StreamDataRecordProvider;
import io.threesixty.compare.reader.sfm.SfmCsvDataRecordProvider;
import io.threesixty.compare.result.ResultRecordToMap;
import io.threesixty.compare.util.FileSupplier;
import io.threesixty.compare.util.ReaderSupplier;
import org.concordion.api.ConcordionResources;
import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth
 */
@FullOGNL
@RunWith(ConcordionRunner.class)
@ConcordionResources( value = { "person-mapping.png", "invoice-mapping.png" } )
public class DataRecordComparisonFixture {

    private ConversionService conversionService = new DefaultConversionService();

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
                        .mapTo("FIRSTNAME", "NAME");


        DataRecordSet sourcePersons = new StreamDataRecordProvider(sourceConfig).provide(ReaderSupplier.forResource("/" + source));
        DataRecordSet targetPersons = new StreamDataRecordProvider(targetConfig).provide(ReaderSupplier.forResource("/" + target));

        return new Comparison()
                .compare(sourcePersons, targetPersons, attributeMapping, true)
                .map(new ResultRecordToMap())
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> readSource(final String source) throws Exception {

        List<Map<String, Object>> results = new StreamDataRecordProvider(dataRecordConfiguration(source).orElseThrow(() -> new Exception("No configuration for " + source)))
                        .provide(ReaderSupplier.forResource("/" + source))
                        .toDataRecordMap();

        System.out.println(results);

        return results;
    }

    /**
     * Read the data records from a CSV file using SimpleFlatMapper
     * @return A list (i.e. row) of maps with each map containing a name - value pair (i.e. column) of that row
     */
    public List<Map<String, Object>> readSfm(final String source) throws Exception {
        List<Map<String, Object>> results =
                new SfmCsvDataRecordProvider(dataRecordConfiguration(source).orElseThrow(() -> new Exception("No configuration for " + source)), conversionService)
                .provide(FileSupplier.forResource("/" + source)).toDataRecordMap();

        System.out.println(results);

        return results;
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

        return new Comparison()
                .compare(sourcePersons, targetPersons, attributeMapping, true)
                .map(new ResultRecordToMap())
                .collect(Collectors.toList());
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
