package io.threesixty.compare.excel.reader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Recordset;
import io.threesixty.compare.Attribute;
import io.threesixty.compare.DataRecord;
import io.threesixty.compare.DataRecordConfiguration;
import io.threesixty.compare.reader.DataRecordProvider;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class ExcelDataRecordProvider implements DataRecordProvider<Connection> {

    private final String sql;

    public ExcelDataRecordProvider(final String sql) {
        this.sql = sql;
    }

    @Override
    public Stream<DataRecord> fetch(final Connection connection) throws Exception {
        Objects.requireNonNull(connection, "The connection is required");

        Recordset recordset = null;
        try {
            recordset = connection.executeQuery(sql);
            List<DataRecord> results = new ArrayList<>();
            while (recordset.next()) {
                results.add(new DataRecord(fetchRow(recordset)));
            }
            return results.stream();
        } finally {
            try { if (recordset != null) recordset.close();  } catch (Exception e) { /* IGNORE */ }
            try { connection.close(); } catch (Exception e) { /* IGNORE */ }
        }
    }

    private Stream<Attribute<?>> fetchRow(final Recordset recordset) throws FilloException {

        List<Attribute<?>> results = new ArrayList<>();
        for(String name : recordset.getFieldNames()) {
            results.add(Attribute.create(name, recordset.getField(name), false));
        }
        return results.stream();
    }
}
