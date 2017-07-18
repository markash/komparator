package io.threesixty.kt.core.reader.sfm;

import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordConfiguration;
import io.threesixty.kt.core.reader.DataRecordProvider;
import org.simpleflatmapper.csv.CsvParser;
import org.springframework.core.convert.ConversionService;

import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class SfmCsvDataRecordProvider implements DataRecordProvider<File> {

    private DataRecord.DataRecordBuilder builder;
    private DataRecordConfiguration configuration;

    public SfmCsvDataRecordProvider(final DataRecordConfiguration configuration, final ConversionService conversionService) {
        this.builder = new DataRecord.DataRecordBuilder(conversionService);
        this.configuration = configuration;
    }

    @Override
    public Stream<DataRecord> fetch(final File file) throws Exception {
        Function<Stream<Map>, Stream<DataRecord>> convertToDataRecord = streamA -> streamA.map(values -> builder.from(configuration, values));

        return CsvParser
                .mapTo(Map.class)
                .stream(file, convertToDataRecord);
    }

}
