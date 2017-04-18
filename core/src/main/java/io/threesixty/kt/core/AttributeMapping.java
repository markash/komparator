package io.threesixty.kt.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark P Ashworth
 */
public class AttributeMapping {
    private Map<String, String> attributeMap = new HashMap<>();

    /**
     * Adds a mapping between a data record attributes
     * @param sourceAttributeName The attribute name of the source data record
     * @param targetAttributeName The attribute name of the target data record
     */
    public void addMapping(final String sourceAttributeName, final String targetAttributeName) {
        this.attributeMap.put(sourceAttributeName, targetAttributeName);
    }

    /**
     * Returns the target data record attribute name for the source attribute name
     * @param sourceAttributeName The source attribute name
     * @return The attribute name
     */
    public String getMappingForSource(final String sourceAttributeName) {
        return attributeMap.get(sourceAttributeName);
    }

    /**
     * Returns the source data record attribute name for the source attribute name
     * @param targetAttributeName The source attribute name
     * @return The attribute name
     */
    public String getMappingForTarget(final String targetAttributeName) {
        return attributeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(targetAttributeName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
