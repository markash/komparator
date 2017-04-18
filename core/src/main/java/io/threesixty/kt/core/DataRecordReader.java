package io.threesixty.kt.core;

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

/**
 * @author Mark P Ashworth
 */
public class DataRecordReader {

    public DataRecordSet read(final DataRecordConfiguration configuration, final Reader dataFileReader) {
        return read(configuration, configuration.getParserConfiguration(), dataFileReader);
    }

    public DataRecordSet read(final DataRecordConfiguration configuration, final InputStream dataInputStream) {
        return read(configuration, configuration.getParserConfiguration(), new InputStreamReader(dataInputStream));
    }

    public DataRecordSet read(final DataRecordConfiguration configuration, final Reader mapFileReader, final Reader dataFileReader) {

        try {
            final Parser parser = resolverParser(configuration, mapFileReader, dataFileReader);
            final DataSet ds = parser.parse();

            DataRecord dataRecord;
            List<DataRecord> results = new ArrayList<>();

            while (ds.next()) {
                if (!ds.isRecordID("header")) {


                    dataRecord = new DataRecord();
                    for (String column : ds.getColumns()) {
                        if (column.equals("ID")) {
                            dataRecord.addKey(new Id2<>("ID", ds.getLong("ID")));
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

    private Parser resolverParser(final DataRecordConfiguration configuration, final Reader mapFileReader, final Reader dataFileReader) {
        ParserFactory factory = DefaultParserFactory.getInstance();
        switch (configuration.getFileType()) {
            case DELIMITED:
                return factory.newDelimitedParser(
                        mapFileReader,
                        dataFileReader,
                        configuration.getDelimeter(),
                        configuration.getQualifier(),
                        configuration.isIgnoreFirstRecord());
            case FIXED:
                return factory.newFixedLengthParser(mapFileReader, dataFileReader);
            default:
                throw new ConfigurationException("Unsupported data record file type provided " + configuration.getFileType());
        }
    }
}
