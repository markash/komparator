package io.threesixty.kt.core.reader;

import io.threesixty.kt.core.AttributeMapping;
import io.threesixty.kt.core.DataRecordConfiguration;
import io.threesixty.kt.core.DataRecordFileType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.function.Supplier;

@Configuration
@ComponentScan(basePackages = {"io.threesixty.kt.core.reader"})
public class ReaderConfiguration {

    @Bean
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("test-data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public DataRecordConfiguration sourceInvoiceConfiguration() {
        return new DataRecordConfiguration("target-invoice", DataRecordFileType.DELIMITED)
                        .withId("invoice_id", Long.class)
                        .withId("line_id", Long.class)
                        .withColumn("description", String.class)
                        .withColumn("amount", Double.class)
                        .withColumn("vat", Boolean.class)
                ;

    }

    @Bean
    public DataRecordConfiguration targetInvoiceConfiguration() {
        return new DataRecordConfiguration("target-invoice", DataRecordFileType.DELIMITED)
                        .withId("invoice_id", Long.class)
                        .withId("line_id", Long.class)
                        .withColumn("description", String.class)
                        .withColumn("amount", Double.class)
                        .withColumn("vat", Boolean.class)
                ;

    }

    @Bean
    public AttributeMapping invoiceMapping() {
        return new AttributeMapping("invoice-mapping")
                .source(sourceInvoiceConfiguration())
                .mapTo(targetInvoiceConfiguration());
    }
}
