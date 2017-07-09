package io.threesixty.kt.ui.service;

import io.threesixty.kt.core.DataRecordConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecordConfigurationRepository {
    private Map<String, DataRecordConfiguration> configurations;

    public DataRecordConfigurationRepository() {
        this.configurations = new HashMap<>();
    }

    public DataRecordConfigurationRepository(DataRecordConfiguration...configurations) {
        this();
        Arrays.stream(configurations).forEach(c -> this.configurations.put(c.getName(), c));
    }

    public Collection<DataRecordConfiguration> getConfigurations() {
        return this.configurations.values();
    }
}
