package io.threesixty.kt.core;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth
 */
public class DataRecord {
    Id2<Long> id;
    Map<String, Attribute<?>> attributes;

    public DataRecord() {
        this.attributes = new HashMap<>();
    }

    public DataRecord(final DataRecord dataRecord) {
        this(dataRecord.id, dataRecord.attributes.values());
    }

    public DataRecord(Id2<Long> id, Attribute<?>...attributes) {
        this(id, Arrays.asList(attributes));
    }

    public DataRecord(Id2<Long> id, Collection<Attribute<?>> attributes) {
        this.id = id;
        this.attributes = new HashMap<>();
        if (attributes != null) {
            for (Attribute<?> attribute : attributes) {
                this.attributes.put(attribute.getName(), attribute);
            }
        }
    }

    public DataRecord add(final Id2<Long> id) {
        return addKey(id);
    }

    public DataRecord add(final Attribute<?> attribute) {
        return addAttribute(attribute);
    }

    public DataRecord addKey(final Id2<Long> id) {
        this.id = id;
        return this;
    }

    public DataRecord addAttribute(final String name, final String attribute) {
        return addAttribute(new Attribute<>(name, attribute));
    }

    public DataRecord addAttribute(final Attribute<?> attribute) {
        this.attributes.put(attribute.getName(), attribute);
        return this;
    }

    public Object get(final String name) {
        if (this.id.getName().equals(name)) {
            return this.id.getValue();
        } else if (this.attributes.containsKey(name)) {
            return getAttribute(name).getValue();
        }
        return null;
    }

    public Attribute<?> getAttribute(final String name) {
        return this.attributes.get(name);
    }

    public List<Tuple2<Attribute, Attribute>> compareTo(DataRecord other, AttributeJoiner join) {

        return Seq.ofType(attributes.values().stream(), Attribute.class)
                .innerJoin(Seq.ofType(other.attributes.values().stream(), Attribute.class), join::matches)
                .filter(match -> !match.v1().getValue().equals(match.v2().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
