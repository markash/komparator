package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.Attribute;
import io.threesixty.kt.core.DataRecord;
import io.threesixty.kt.core.DataRecordColumn;
import io.threesixty.kt.core.DataRecordConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class JdbcDataRecordProvider implements DataRecordProvider<JdbcTemplate> {

    private final DataRecordConfiguration configuration;
    private final String sql;

    public JdbcDataRecordProvider(final DataRecordConfiguration configuration, final String sql) {
        this.configuration = configuration;
        this.sql = sql;
    }

    public Stream<DataRecord> fetch(final Supplier<JdbcTemplate> supplier) throws Exception {

        return supplier.get().query(sql, new RowMapper<DataRecord>() {
            @Override
            public DataRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

                DataRecord dataRecord = new DataRecord();
                for (DataRecordColumn column : configuration.getColumns()) {
                    if (column.isKey()) {
                        dataRecord.addKey(column.getName(), rs.getObject(column.getName(), column.getDataType()));
                    } else {
                        dataRecord.addAttribute(Attribute.create(column.getName(), rs.getObject(column.getName(), column.getDataType())));
                    }
                }
                return dataRecord;
            }
        }).stream();
    }
}
