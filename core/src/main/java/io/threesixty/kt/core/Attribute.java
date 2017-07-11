package io.threesixty.kt.core;

import java.io.Serializable;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class Attribute<T> implements Serializable {
    private String name;
    private T value;

    public Attribute(final String name, final T value) {
        this.name = name;
        this.value = value;
    }

    /**
     * The name of the attribute which is the v1 value of the Tuple
     * @return The name of the attribute
     */
    public String getName() { return this.name; }
    /**
     * The value of the attribute which is the v2 value of the Tuple
     * @return The value of the attribute
     */
    public T getValue() { return this.value; }
    /**
     * Create an attribute from the name and value parameters
     * @param name The name of the attribute
     * @param value The value of the attribute
     * @param <T> The type of the value
     * @return A new attribute
     */
    public static <T> Attribute<T> create(final String name, final T value) {
        return new Attribute<>(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute<?> attribute = (Attribute<?>) o;
        return name != null ? name.equals(attribute.name) : attribute.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString() { return "(" + name + ", " + value + ")"; }
}