package io.threesixty.kt.ui.service;

import io.threesixty.kt.core.AttributeMapping;
import io.threesixty.kt.core.DataRecordConfiguration;
import io.threesixty.kt.core.DataRecordFileType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class PersistenceService {

    Map<String, DataRecordConfiguration> configurations = new HashMap<>();
    Map<String, AttributeMapping> mappings = new HashMap<>();

    public PersistenceService() {
        DataRecordConfiguration configuration =
                new DataRecordConfiguration("person", DataRecordFileType.DELIMITED)
                .withColumn("ID", String.class)
                .withColumn("NAME", String.class)
                .withColumn("AGE", String.class);

        this.configurations.put(configuration.getName(), configuration);

        configuration =
                new DataRecordConfiguration("target-persons", DataRecordFileType.DELIMITED)
                        .withColumn("ID", String.class)
                        .withColumn("AGE", String.class)
                        .withColumn("FIRSTNAME", String.class)
                        .withColumn("SURNAME", String.class);

        this.configurations.put(configuration.getName(), configuration);


        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.addMapping("ID", "ID");
        attributeMapping.addMapping("NAME", "FIRSTNAME");
        attributeMapping.addMapping("AGE", "AGE");

        this.mappings.put("person to target-person", attributeMapping);
    }

    public Collection<DataRecordConfiguration> retrieveDataRecordConfigurations() {
        return this.configurations.values();
    }

    public Collection<AttributeMapping> retrieveDataRecordAttributeMappings() {
        return this.mappings.values();
    }
}
