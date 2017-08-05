package io.threesixty.compare.reader;

import io.threesixty.compare.DataRecord;
import io.threesixty.compare.Attribute;
import io.threesixty.compare.ConfigurationException;
import io.threesixty.compare.DataRecordConfiguration;
import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.ParserFactory;

import java.io.Reader;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamDataRecordProvider implements DataRecordProvider<Reader> {

    private final DataRecordConfiguration configuration;

    public StreamDataRecordProvider(final DataRecordConfiguration configuration) {
        this.configuration = configuration;
    }

    public Stream<DataRecord> fetch(final Reader reader) throws Exception {
        DataSet dataSet = resolverParser(configuration, reader).parse();
        dataSet.setPZConvertProps(getConverters());
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        spliterator(dataSet),
                        Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE),
                false);
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
                    dataRecord.addAttribute(
                                Attribute.create(
                                        column,
                                        getCellValue(column, configuration, dataSet).orElse(null),
                                        configuration.isKeyColumn(column)));
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
        properties.setProperty("java.lang.Long", "io.threesixty.compare.util.ConvertLong");
        properties.setProperty("java.lang.String", "io.threesixty.compare.util.ConvertString");
        properties.setProperty(Boolean.class.getName(), "io.threesixty.compare.util.ConvertBoolean");
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
