package io.threesixty.kt.ui.config;

import io.threesixty.kt.core.AttributeMapping;
import io.threesixty.kt.core.DataRecordConfiguration;
import io.threesixty.kt.core.DataRecordFileType;
import io.threesixty.kt.ui.service.AttributeMappingRepository;
import io.threesixty.kt.ui.service.DataRecordConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComparatorConfig {

    @Bean
    public AttributeMappingRepository attributeMappingRepository() {
        return new AttributeMappingRepository(
                personAttributeMapping(),
                adventureWorksPersonMapping());
    }

    @Bean
    public DataRecordConfigurationRepository recordConfigurationRepository() {
        return new DataRecordConfigurationRepository(
                sourcePersonRecordConfig(),
                targetPersonRecordConfig(),
                adventureWorksPersonSemiColonDelimited(),
                adventureWorksPersonTabDelimited());
    }

    @Bean
    public DataRecordConfiguration sourcePersonRecordConfig() {
        return new DataRecordConfiguration("source-person", DataRecordFileType.DELIMITED)
                        .withId("ID", String.class)
                        .withColumn("NAME", String.class)
                        .withColumn("AGE", String.class);
    }

    @Bean
    public DataRecordConfiguration targetPersonRecordConfig() {
        return new DataRecordConfiguration("target-persons", DataRecordFileType.DELIMITED)
                        .withId("ID", String.class)
                        .withColumn("AGE", String.class)
                        .withColumn("FIRSTNAME", String.class)
                        .withColumn("SURNAME", String.class);
    }

    @Bean
    public DataRecordConfiguration adventureWorksPersonTabDelimited() {
        return new DataRecordConfiguration("AdventureWorks Person (Tab)", DataRecordFileType.DELIMITED)
                        .withDelimiter('\t')
                        .withId("ID", String.class)
                        .withColumn("TYPE", String.class)
                        .withColumn("NAME_STYLE", String.class)
                        .withColumn("TITLE", String.class)
                        .withColumn("FIRST_NAME", String.class)
                        .withColumn("MIDDLE_NAME", String.class)
                        .withColumn("LAST_NAME", String.class)
                        .withColumn("SUFFIX", String.class)
                        .withColumn("EMAIL_PROMOTION", String.class)
                        .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
                        .withColumn("DEMOGRAPHICS_INFO", String.class)
                        .withColumn("ROWGUID_ID", String.class)
                        .withColumn("MODIFIED_DATE", String.class)
        ;
    }

    @Bean
    public DataRecordConfiguration adventureWorksPersonSemiColonDelimited() {
        return new DataRecordConfiguration("AdventureWorks Person (Semicolon)", DataRecordFileType.DELIMITED)
                        .withDelimiter(';')
                        .withId("ID", String.class)
                        .withColumn("TYPE", String.class)
                        .withColumn("NAME_STYLE", String.class)
                        .withColumn("TITLE", String.class)
                        .withColumn("FIRST_NAME", String.class)
                        .withColumn("MIDDLE_NAME", String.class)
                        .withColumn("LAST_NAME", String.class)
                        .withColumn("SUFFIX", String.class)
                        .withColumn("EMAIL_PROMOTION", String.class)
                        .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
                        .withColumn("DEMOGRAPHICS_INFO", String.class)
                        .withColumn("ROWGUID_ID", String.class)
                        .withColumn("MODIFIED_DATE", String.class)
        ;
    }

    @Bean
    public AttributeMapping personAttributeMapping() {
        AttributeMapping attributeMapping = new AttributeMapping("Person Mapping");
        attributeMapping.addMapping("ID", "ID");
        attributeMapping.addMapping("NAME", "FIRSTNAME");
        attributeMapping.addMapping("AGE", "AGE");
        return attributeMapping;
    }

    @Bean
    public AttributeMapping adventureWorksPersonMapping() {
        return new AttributeMapping("AdventureWorks Person Mapping")
                .source(adventureWorksPersonSemiColonDelimited())
                .mapTo(adventureWorksPersonSemiColonDelimited());
    }
}
