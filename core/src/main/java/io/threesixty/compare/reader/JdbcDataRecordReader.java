package io.threesixty.compare.reader;

import io.threesixty.compare.DataRecordConfiguration;
import io.threesixty.compare.DataRecordSet;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface JdbcDataRecordReader {
    /**
     * Read the data record set from the JDBC data source for the given configuration.
     * @param configuration The data record configuration
     * @return The data record set
     */
    DataRecordSet read(final DataRecordConfiguration configuration, final String query);
}
