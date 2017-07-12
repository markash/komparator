package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.ConfigurationException;
import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordConfiguration;
import net.sf.flatpack.*;

import java.io.Reader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamDataRecordProvider implements DataRecordProvider<Reader> {

    private final DataRecordConfiguration configuration;

    public StreamDataRecordProvider(final DataRecordConfiguration configuration) {
        this.configuration = configuration;
    }

    public Stream<DataRecord> fetch(final Supplier<Reader> supplier) throws Exception {
        try (Reader reader = supplier.get()) {
            DataSet dataSet = resolverParser(configuration, reader).parse();
            dataSet.setPZConvertProps(getConverters());
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(
                            spliterator(dataSet),
                            Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE),
                    false);
        }
    }

    private Iterator<DataRecord> spliterator(DataSet dataSet) {

        return new Iterator<DataRecord>() {
            @Override
            public boolean hasNext() {
                return dataSet.next();
            }

            @Override
            public DataRecord next() {

                DataRecord dataRecord = new DataRecord();
                for (String column : dataSet.getColumns()) {
                    if (configuration.isKeyColumn(column)) {
                        dataRecord.addKey(column, getCellValue(column, configuration, dataSet).orElse(null));
                    } else {
                        dataRecord.addAttribute(column, getCellValue(column, configuration, dataSet).orElse(null));
                    }
                }
                return dataRecord;
            }

            private Optional<Object> getCellValue(final String columnName, final DataRecordConfiguration configuration, DataSet ds) {
                return configuration
                        .getColumn(columnName)
                        .map(dataRecordColumn -> ds.getObject(columnName, dataRecordColumn.getDataType()));
            }
        };
    }

    private Properties getConverters() {
        Properties properties = new Properties();
        properties.setProperty("java.math.BigDecimal", "net.sf.flatpack.converter.ConvertBigDecimal");
        properties.setProperty("java.lang.Double", "net.sf.flatpack.converter.ConvertDouble");
        properties.setProperty("java.lang.Integer", "net.sf.flatpack.converter.ConvertInteger");
        properties.setProperty("java.lang.Long", "io.threesixty.kt.core.util.ConvertLong");
        properties.setProperty("java.lang.String", "io.threesixty.kt.core.util.ConvertString");
        return properties;
    }

    private Parser resolverParser(final DataRecordConfiguration configuration, final Reader dataFileReader) {
        ParserFactory factory = DefaultParserFactory.getInstance();
        switch (configuration.getFileType()) {
            case DELIMITED:
                return factory.newDelimitedParser(
                        configuration.getParserConfiguration(),
                        dataFileReader,
                        configuration.getDelimiter(),
                        configuration.getQualifier(),
                        configuration.isIgnoreFirstRecord());
            case FIXED:
                return factory.newFixedLengthParser(configuration.getParserConfiguration(), dataFileReader);
            default:
                throw new ConfigurationException("Unsupported data record file type provided " + configuration.getFileType());
        }
    }
}
