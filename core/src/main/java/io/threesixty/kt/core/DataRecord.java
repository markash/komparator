package io.threesixty.kt.core;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An attributable object that has a key and attributes
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

    public DataRecord(final Key key, final Stream<Attribute<?>> attributes) {
        this(key.getAttributes(), attributes);
    }

    private DataRecord(final Stream<Attribute<?>> keys, final Stream<Attribute<?>> attributes) {
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

    /**
     * Gets the attribute value with the given name. The attribute could be part of the key.
     * @param name The name of the attribute
     * @return The
     */
    public Optional<Object> getAttributeValue(final String name) {
        return getAttribute(name).map(Attribute::getValue);
    }

    /**
     * Gets the attribute value with the given name. The attribute could be part of the key.
     * @param name The name of the attribute
     * @return The
     */
    public Optional<Attribute<?>> getAttribute(final String name) {
        if (hasKey(name)) {
            return getKey(name);
        } else if (hasAttribute(name)) {
            return super.getAttribute(name);
        }
        return Optional.empty();
    }

    public Key getKey() {
        return this.key;
    }

    public Optional<Attribute<?>> getKey(final String name) {
        return this.key.getAttribute(name);
    }

    public Stream<Attribute<?>> getCompleteAttributeList() {
        return Stream.of(this.key.getAttributes(), getAttributes()).flatMap(a -> a);
    }

    /**
     * Determine if the attribute name is a key
     * @param name The name of the attribute
     * @return Whether the name is an attribute of the key
     */
    public boolean hasKey(final String name) {
        return this.key.hasAttribute(name);
    }

    /**
     * Gets the attributes
     * @return The collection of attributes
     */
    public Stream<Attribute<?>> getAttributes() {
        return Stream
                .of(getKey().getAttributes(), super.getAttributes())
                .flatMap(s -> s);
    }

    /**
     * Returns a map of the data record where the key is the attribute name and the value is the attribute value
     * @return A map of attribute name and values
     */
    public Map<String, Object> toMap() {
        return getAttributes().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }

    /**
     * Compares this DataRecord to another to determine the differences
     * @param other The other data record to compare to
     * @param join The joining strategy, i.e. by name or attribute mapping
     * @return A tuple of attributes that are different
     */
    public List<Tuple2<Attribute, Attribute>> difference(final DataRecord other, AttributeJoiner join) {
        return Seq.ofType(getAttributes(), Attribute.class)
                .innerJoin(Seq.ofType(other.getAttributes(), Attribute.class), join::matches)
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
