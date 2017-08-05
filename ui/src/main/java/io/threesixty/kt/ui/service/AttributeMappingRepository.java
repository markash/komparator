package io.threesixty.kt.ui.service;

import io.threesixty.compare.AttributeMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class AttributeMappingRepository {
    private Map<String, AttributeMapping> mappings;

    public AttributeMappingRepository() {
        this.mappings = new HashMap<>();
    }

    public AttributeMappingRepository(final AttributeMapping...mappings) {
        this();
        Arrays.stream(mappings).forEach(m -> this.mappings.put(m.getName(), m));
    }

    public Collection<AttributeMapping> getMappings() {
        return this.mappings.values();
    }
}
