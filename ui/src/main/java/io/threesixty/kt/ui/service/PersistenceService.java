package io.threesixty.kt.ui.service;

import io.threesixty.kt.core.AttributeMapping;
import io.threesixty.kt.core.DataRecordConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PersistenceService {

    private DataRecordConfigurationRepository recordConfigurationRepository;
    private AttributeMappingRepository attributeMappingRepository;

    @Autowired
    public PersistenceService(
            final DataRecordConfigurationRepository recordConfigurationRepository,
            final AttributeMappingRepository attributeMappingRepository) {

        this.recordConfigurationRepository = recordConfigurationRepository;
        this.attributeMappingRepository = attributeMappingRepository;

//        DataRecordConfiguration configuration =
//                new DataRecordConfiguration("person", DataRecordFileType.DELIMITED)
//                .withId("ID", String.class)
//                .withColumn("NAME", String.class)
//                .withColumn("AGE", String.class);
//
//        this.configurations.put(configuration.getName(), configuration);
//
//        configuration =
//                new DataRecordConfiguration("target-persons", DataRecordFileType.DELIMITED)
//                        .withColumn("ID", String.class)
//                        .withColumn("AGE", String.class)
//                        .withColumn("FIRSTNAME", String.class)
//                        .withColumn("SURNAME", String.class);
//
//        this.configurations.put(configuration.getName(), configuration);
//
//        configuration =
//                new DataRecordConfiguration("AdventureWorks Person (Tab)", DataRecordFileType.DELIMITED)
//                        .withDelimiter('\t')
//                        .withColumn("ID", String.class)
//                        .withColumn("TYPE", String.class)
//                        .withColumn("NAME_STYLE", String.class)
//                        .withColumn("TITLE", String.class)
//                        .withColumn("FIRST_NAME", String.class)
//                        .withColumn("MIDDLE_NAME", String.class)
//                        .withColumn("LAST_NAME", String.class)
//                        .withColumn("SUFFIX", String.class)
//                        .withColumn("EMAIL_PROMOTION", String.class)
//                        .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
//                        .withColumn("DEMOGRAPHICS_INFO", String.class)
//                        .withColumn("ROWGUID_ID", String.class)
//                        .withColumn("MODIFIED_DATE", String.class)
//        ;
//
//        this.configurations.put(configuration.getName(), configuration);
//
//        configuration =
//                new DataRecordConfiguration("AdventureWorks Person (Semicolon)", DataRecordFileType.DELIMITED)
//                        .withDelimiter(';')
//                        .withColumn("ID", String.class)
//                        .withColumn("TYPE", String.class)
//                        .withColumn("NAME_STYLE", String.class)
//                        .withColumn("TITLE", String.class)
//                        .withColumn("FIRST_NAME", String.class)
//                        .withColumn("MIDDLE_NAME", String.class)
//                        .withColumn("LAST_NAME", String.class)
//                        .withColumn("SUFFIX", String.class)
//                        .withColumn("EMAIL_PROMOTION", String.class)
//                        .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
//                        .withColumn("DEMOGRAPHICS_INFO", String.class)
//                        .withColumn("ROWGUID_ID", String.class)
//                        .withColumn("MODIFIED_DATE", String.class)
//        ;
//
//        this.configurations.put(configuration.getName(), configuration);
//
//        AttributeMapping attributeMapping = new AttributeMapping();
//        attributeMapping.addMapping("ID", "ID");
//        attributeMapping.addMapping("NAME", "FIRSTNAME");
//        attributeMapping.addMapping("AGE", "AGE");
//
//        this.mappings.put("person to target-person", attributeMapping);
//
//
//        attributeMapping = new AttributeMapping("AdventureWorks Person Mapping")
//                .source(configuration)
//                .mapTo(configuration);
//
//        this.mappings.put(attributeMapping.getName(), attributeMapping);
    }

    public Collection<DataRecordConfiguration> retrieveDataRecordConfigurations() {
        return this.recordConfigurationRepository.getConfigurations();
    }

    public Collection<AttributeMapping> retrieveDataRecordAttributeMappings() {
        return this.attributeMappingRepository.getMappings();
    }
}
