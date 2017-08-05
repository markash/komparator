package io.threesixty.compare.reader;

import io.threesixty.compare.*;
import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.ParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class FileDataRecordReader {

    public DataRecordSet read(final DataRecordConfiguration configuration, final Reader dataFileReader) {
        return read(configuration, configuration.getParserConfiguration(), dataFileReader);
    }

    public DataRecordSet read(final DataRecordConfiguration configuration, final InputStream dataInputStream) {
        return read(configuration, configuration.getParserConfiguration(), new InputStreamReader(dataInputStream));
    }

    private DataRecordSet read(final DataRecordConfiguration configuration, final Reader mapFileReader, final Reader dataFileReader) {

        try {
            final Parser parser = resolverParser(configuration, dataFileReader);
            final DataSet ds = parser.parse();
            ds.setPZConvertProps(getConverters());

            DataRecord dataRecord;
            List<DataRecord> results = new ArrayList<>();

            while (ds.next()) {
                if (!ds.isRecordID("header")) {

                    dataRecord = new DataRecord();
                    for (String column : ds.getColumns()) {
                        dataRecord.addAttribute(
                                Attribute.create(
                                        column,
                                        getCellValue(column, configuration, ds).orElse(null),
                                        configuration.isKeyColumn(column)));
                    }
                    results.add(dataRecord);
                }
            }
            return new DataRecordSet(configuration.getColumns(), results);
        } finally {
            try { mapFileReader.close(); } catch (IOException e) { /*IGNORE*/}
            try { dataFileReader.close(); } catch (IOException e) { /*IGNORE*/}
        }
    }

    private Properties getConverters() {
        Properties properties = new Properties();
        properties.setProperty("java.math.BigDecimal", "net.sf.flatpack.converter.ConvertBigDecimal");
        properties.setProperty("java.lang.Double", "net.sf.flatpack.converter.ConvertDouble");
        properties.setProperty("java.lang.Integer", "net.sf.flatpack.converter.ConvertInteger");
        properties.setProperty("java.lang.Long", "io.threesixty.compare.converter.ConvertLong");
        properties.setProperty("java.lang.String", "io.threesixty.compare.converter.ConvertString");
        return properties;
    }

    public DataRecordSet read(final List<Map<String, String>> records, final String idKey, final int skipHeaders) {
        /* Convert the list of map entries to a list of DataRecord values */
        List<DataRecord> results = new ArrayList<>();
        records.stream().skip(skipHeaders).map(record -> mapToDataRecord(record, idKey)).forEach(results::add);

        /* Convert the first record to a column definition */
        List<DataRecordColumn> columns = records.stream().limit(1).map(this::mapToDataRecordColumns).findFirst().orElse(new ArrayList<>());

        /* Convert the list of DataRecord and DataRecordColumns to a DataRecordSet */
        return new DataRecordSet(columns, results);
    }

    private Optional<Object> getCellValue(final String columnName, final DataRecordConfiguration configuration, DataSet ds) {
        return configuration
                .getColumn(columnName)
                .map(dataRecordColumn -> ds.getObject(columnName, dataRecordColumn.getDataType()));
    }

    private DataRecord mapToDataRecord(final Map<String, String> record, final String idKey) {
        DataRecord dataRecord = new DataRecord();
        record.forEach((key, value) -> dataRecord.addAttribute(Attribute.create(key, value, key.equals(idKey))));
        return dataRecord;
    }

    private List<DataRecordColumn> mapToDataRecordColumns(final Map<String, String> record) {
        return record.entrySet()
                .stream()
                .map(entry -> new DataRecordColumn(entry.getKey(), String.class))
                .collect(Collectors.toList());
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
