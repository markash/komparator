package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.*;
import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.ParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

            DataRecord dataRecord;
            List<DataRecord> results = new ArrayList<>();

            while (ds.next()) {
                if (!ds.isRecordID("header")) {


                    dataRecord = new DataRecord();
                    for (String column : ds.getColumns()) {
                        if (column.equals("ID")) {
                            dataRecord.addKey(column, ds.getString(column));
                        } else {
                            dataRecord.addAttribute(column, ds.getString(column));
                        }
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

    public DataRecordSet read(final List<Map<String, String>> records, final String idKey, final int skipHeaders) {
        /* Convert the list of map entries to a list of DataRecord values */
        List<DataRecord> results = new ArrayList<>();
        records.stream().skip(skipHeaders).map(record -> mapToDataRecord(record, idKey)).forEach(results::add);

        /* Convert the first record to a column definition */
        List<DataRecordColumn> columns = records.stream().limit(1).map(this::mapToDataRecordColumns).findFirst().orElse(new ArrayList<>());

        /* Convert the list of DataRecord and DataRecordColumns to a DataRecordSet */
        return new DataRecordSet(columns, results);
    }

    private DataRecord mapToDataRecord(final Map<String, String> record, final String idKey) {
        DataRecord dataRecord = new DataRecord();
        record.entrySet().forEach(a -> {
            if (a.getKey().equals(idKey)) {
                dataRecord.addKey(new Attribute<>(a.getKey(), a.getValue()));
            } else {
                dataRecord.addAttribute(new Attribute<>(a.getKey(), a.getValue()));
            }
        });
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
