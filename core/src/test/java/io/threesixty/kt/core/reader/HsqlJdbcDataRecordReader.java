package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HsqlJdbcDataRecordReader implements JdbcDataRecordReader {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public DataRecordSet read(final DataRecordConfiguration configuration) {

        List<DataRecord> records = jdbcTemplate.query("SELECT * FROM TARGET_PERSON", new RowMapper<DataRecord>() {
            @Override
            public DataRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

                DataRecord dataRecord = new DataRecord();
                for (DataRecordColumn column : configuration.getColumns()) {
                    if (column.getDataType().equals(String.class)) {
                        dataRecord.addAttribute(Attribute.create(column.getName(), rs.getString(column.getName())));
                    }
                }
                return dataRecord;
            }
        });
        return new DataRecordSet(configuration.getColumns(), records);
    }
}
