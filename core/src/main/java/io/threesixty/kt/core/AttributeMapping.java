package io.threesixty.kt.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class AttributeMapping {
    private String name;
    private Map<DataRecordColumn, DataRecordColumn> attributeMap = new HashMap<>();

    public AttributeMapping() { }

    public AttributeMapping(final String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    /**
     * Add the source column definitions
     * @param configuration The column configuration
     * @return AttributeMapping
     */
    public AttributeMapping source(final DataRecordConfiguration configuration) {
        return source(configuration.getColumns());
    }

    /**
     * Add the source column definitions
     * @param columnDefinitions The column definitions
     * @return AttributeMapping
     */
    public AttributeMapping source(final List<DataRecordColumn> columnDefinitions) {
        attributeMap.clear();
        columnDefinitions.forEach(e -> attributeMap.put(e, null));
        return this;
    }

    public AttributeMapping mapTo(final DataRecordConfiguration configuration) {
        return mapTo(configuration.getColumns());
    }

    public AttributeMapping mapTo(final List<DataRecordColumn> columnDefinitions) {
        columnDefinitions.forEach(this::autoMap);
        return this;
    }

    private AttributeMapping mapTo(final DataRecordColumn source, final DataRecordColumn target) {
        if (this.attributeMap.containsKey(source)) {
            this.attributeMap.replace(source, target);
        } else {
            this.attributeMap.put(source, target);
        }
        return this;
    }

    /**
     * Map the source to the target data record definition
     * @param target The target column which is matched to the source by name
     */
    private void autoMap(final DataRecordColumn target) {
        this.attributeMap.keySet().stream()
                        .filter(source -> source.getName().equals(target.getName()))
                        .forEach(source -> mapTo(source, target));
    }

    /**
     * Adds a mapping between a data record attributes
     * @param sourceAttributeName The attribute name of the source data record
     * @param targetAttributeName The attribute name of the target data record
     */
    public void addMapping(final String sourceAttributeName, final String targetAttributeName) {
        this.attributeMap.put(new DataRecordColumn(sourceAttributeName, String.class), new DataRecordColumn(targetAttributeName, String.class));
    }

    /**
     * Returns the target data record attribute name for the source attribute name
     * @param sourceAttributeName The source attribute name
     * @return The attribute name
     */
    public String getMappingForSource(final String sourceAttributeName) {
        final DataRecordColumn key = new DataRecordColumn(sourceAttributeName, String.class);
        if (attributeMap.containsKey(key)) {
            return attributeMap.get(key).getName();
        }
        return null;
    }

    /**
     * Returns the source data record attribute name for the source attribute name
     * @param targetAttributeName The source attribute name
     * @return The attribute name
     */
    public String getMappingForTarget(final String targetAttributeName) {

        final DataRecordColumn targetKey = new DataRecordColumn(targetAttributeName, String.class);

        DataRecordColumn source = attributeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(targetKey))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return source != null ? source.getName() : null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
