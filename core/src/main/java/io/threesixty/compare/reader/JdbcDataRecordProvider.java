package io.threesixty.compare.reader;

import io.threesixty.compare.DataRecord;
import io.threesixty.compare.Attribute;
import io.threesixty.compare.DataRecordColumn;
import io.threesixty.compare.DataRecordConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class JdbcDataRecordProvider implements DataRecordProvider<JdbcTemplate> {

    private final DataRecordConfiguration configuration;
    private final String sql;

    public JdbcDataRecordProvider(final DataRecordConfiguration configuration, final String sql) {
        this.configuration = configuration;
        this.sql = sql;
    }

    public Stream<DataRecord> fetch(final JdbcTemplate jdbcTemplate) throws Exception {

        return jdbcTemplate.query(sql, new RowMapper<DataRecord>() {
            @Override
            public DataRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

                DataRecord dataRecord = new DataRecord();
                for (DataRecordColumn column : configuration.getColumns()) {
                    dataRecord.addAttribute(Attribute.create(column.getName(), rs.getObject(column.getName(), column.getDataType()), column.isKey()));
                }
                return dataRecord;
            }
        }).stream();
    }
}
