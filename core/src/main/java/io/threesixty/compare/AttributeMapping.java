package io.threesixty.compare;

import org.jooq.lambda.Seq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class AttributeMapping {
    private String name;
    private Map<String, String> attributeMap = new HashMap<>();

    public AttributeMapping() { }

    public AttributeMapping(final String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public AttributeMapping mapBetween(final Stream<String> source, final Stream<String> target) {
        this.attributeMap = Seq.seq(source).innerJoin(Seq.seq(target), String::equalsIgnoreCase)
                .collect(Collectors.toMap(tuple -> tuple.v1, tuple -> tuple.v2));
        return this;
    }

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
        columnDefinitions.forEach(e -> attributeMap.put(e.getName(), null));
        return this;
    }

    public AttributeMapping mapTo(final DataRecordConfiguration configuration) {
        return mapTo(configuration.getColumns());
    }

    public AttributeMapping mapTo(final List<DataRecordColumn> columnDefinitions) {
        columnDefinitions.forEach(this::autoMap);
        return this;
    }

    //Change to mapBetween
    public AttributeMapping mapTo(final String source, final String target) {
        if (this.attributeMap.containsKey(source)) {
            this.attributeMap.replace(source, target);
        } else {
            this.attributeMap.put(source, target);
        }
        return this;
    }

    public AttributeMapping mapTo(final DataRecordConfiguration configuration, final String source, final String target) {
        final Optional<String> sourceKey = getSource(source);
        final Optional<DataRecordColumn> targetKey = configuration.getColumn(target);

        if (sourceKey.isPresent() && targetKey.isPresent()) {
            return mapTo(sourceKey.get(), targetKey.get().getName());
        }
        return this;
    }

    public AttributeMapping mapTo(final List<DataRecordColumn> columnDefinitions, final String source, final String target) {

        final Optional<String> sourceKey = getSource(source);
        final Optional<DataRecordColumn> targetKey = columnDefinitions.stream().filter(e -> e.getName().equals(target)).findFirst();

        if (sourceKey.isPresent() && targetKey.isPresent()) {
            return mapTo(sourceKey.get(), targetKey.get().getName());
        }
        return this;
    }

    /**
     * Map the source to the target data record definition
     * @param target The target column which is matched to the source by name
     */
    private void autoMap(final String target) {
        this.attributeMap.keySet().stream()
                        .filter(source -> source.equals(target))
                        .forEach(source -> mapTo(source, target));
    }

    /**
     * Map the source to the target data record definition
     * @param target The target column which is matched to the source by name
     */
    private void autoMap(final DataRecordColumn target) {
        this.attributeMap.keySet().stream()
                .filter(source -> source.equals(target.getName()))
                .forEach(source -> mapTo(source, target.getName()));
    }

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
        if (attributeMap.containsKey(sourceAttributeName)) {
            return attributeMap.get(sourceAttributeName);
        }
        return null;
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

    private Optional<String> getSource(final String name) {
        return this.attributeMap.keySet().stream().filter(e -> e.equals(name)).findFirst();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
