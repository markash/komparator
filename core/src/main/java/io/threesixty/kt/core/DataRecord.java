package io.threesixty.kt.core;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecord extends AttributableObject {
    private Key key;

    public DataRecord() {
        super();
        this.key = new Key();
    }

    public DataRecord(final DataRecord dataRecord) {
        this(dataRecord.getAttributes(), dataRecord.getAttributes());
    }

    public DataRecord(final Key key, final Collection<Attribute<?>> attributes) {
        this(key.getAttributes(), attributes);
    }

    public DataRecord(final Collection<Attribute<?>> keys, final Collection<Attribute<?>> attributes) {
        super(attributes);
        this.key = new Key(keys);
    }

    public <T> DataRecord addKey(final String name, final T attribute) {
        return addKey(new Attribute<>(name, attribute));
    }

    public DataRecord addKey(final Attribute<?> attribute) {
        this.key.addAttribute(attribute);
        return this;
    }

    public Object get(final String name) {
        if (hasKey(name)) {
            return getKey(name).getValue();
        } else if (hasAttribute(name)) {
            return getAttribute(name).getValue();
        }
        return null;
    }

    public Key getKey() {
        return this.key;
    }

    public Attribute<?> getKey(final String name) {
        return this.key.getAttribute(name);
    }

    public Collection<Attribute<?>> getCompleteAttributeList() {
        List<Attribute<?>> results = new ArrayList<>(this.key.getAttributes());
        results.addAll(getAttributes());
        return results;
    }

    public boolean hasKey(final String name) {
        return this.key.hasAttribute(name);
    }

    /**
     * Gets the attributes
     * @return The collection of attributes
     */
    public Collection<Attribute<?>> getAttributes() {
        List<Attribute<?>> attributes = new ArrayList<>(getKey().getAttributes());
        attributes.addAll(super.getAttributes());
        return attributes;
    }

    /**
     * Returns a map of the data record where the key is the attribute name and the value is the attribute value
     * @return A map of attribute name and values
     */
    public Map<String, Object> toMap() {
        return getAttributes().stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }

    /**
     * Compares this DataRecord to another to determine the differences
     * @param other The other data record to compare to
     * @param join The joining strategy, i.e. by name or attribute mapping
     * @return A tuple of attributes that are different
     */
    public List<Tuple2<Attribute, Attribute>> difference(final DataRecord other, AttributeJoiner join) {
        return Seq.ofType(getAttributes().stream(), Attribute.class)
                .innerJoin(Seq.ofType(other.getAttributes().stream(), Attribute.class), join::matches)
                .filter(match -> !match.v1().getValue().equals(match.v2().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRecord that = (DataRecord) o;
        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() { return this.key.toString(); }

    public static class DataRecordBuilder {

        private Attribute.AttributeBuilder attributeBuilder;

        public DataRecordBuilder(final ConversionService conversionService) {
            this.attributeBuilder = new Attribute.AttributeBuilder(conversionService);
        }

        public DataRecord from(final DataRecordConfiguration configuration, final Map<String, ?> values) {
            DataRecord dataRecord = new DataRecord();
            values.entrySet().stream()
                    .map(a -> attributeBuilder.from(configuration, a))
                    .forEach(tuple -> {
                        /* If the data column is present and a key then attribute is a key */
                        if (tuple.v1.isPresent() && tuple.v1.get().isKey()) {
                            dataRecord.addKey(tuple.v2);
                        } else {
                            /* Else an attribute */
                            dataRecord.addAttribute(tuple.v2);
                        }
                    });
            return dataRecord;
        }
    }
}
