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
public interface JdbcDataRecordReader {
    /**
     * Read the data record set from the JDBC data source for the given configuration.
     * @param configuration
     * @return
     */
    DataRecordSet read(final DataRecordConfiguration configuration);
}
