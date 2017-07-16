package io.threesixty.kt.core;

import io.threesixty.kt.core.result.MatchRecord;
import io.threesixty.kt.core.result.ResultRecord;
import io.threesixty.kt.core.result.ResultType;
import io.threesixty.kt.core.result.UnMatchRecord;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;
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
}
