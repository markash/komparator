package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcDataRecordReaderImpl implements JdbcDataRecordReader {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public DataRecordSet read(final DataRecordConfiguration configuration, final String sql) {

        List<DataRecord> records = jdbcTemplate.query(sql, new RowMapper<DataRecord>() {
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
        });
        return new DataRecordSet(configuration.getColumns(), records);
    }
}
