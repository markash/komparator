package io.threesixty.kt.core;

import java.util.Collection;
import java.util.StringJoiner;

public class Key extends AttributableObject implements Comparable<Key> {

    public Key() {
        super();
    }

    public Key(Collection<Attribute<?>> attributes) {
        super(attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final String thisKey = toString();
        final String otherKey = ((Key) o).toString();

        return thisKey.equals(otherKey);
    }

    @Override
    public int hashCode() {
        final String thisKey = toString();
        return thisKey != null ? thisKey.hashCode() : 0;
    }

    @Override
    public int compareTo(final Key o) {
        final String thisKey = toString();
        final String otherKey = o != null ? o.toString() : "";
        return thisKey.compareTo(otherKey);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("|");
        AttributeToStringConverter converter = new AttributeToStringConverter();
        getAttributes().stream().map(converter::convert).forEach(joiner::add);
        return joiner.toString();
    }
}