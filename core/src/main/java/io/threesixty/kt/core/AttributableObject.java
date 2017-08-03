package io.threesixty.kt.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttributableObject implements Attributable {
    private Map<String, Attribute<?>> attributes;

    /**
     * Attributable object with no defined attributes
     */
    AttributableObject() {
        this.attributes = new HashMap<>();
    }

    /**
     * Attributable object with the defined attributes
     * @param attributes The attributes of the attributable object
     */
    AttributableObject(final Stream<Attribute<?>> attributes) {
        this.attributes = attributes.collect(Collectors.toMap(Attribute::getName, Function.identity()));
    }

    /**
     * Add an attribute
     * @param name      The name of the attribute
     * @param attribute The value of the attribute
     * @return The attributable
     */
    @Override
    public <T> Attributable addAttribute(final String name, final T attribute) {
        return addAttribute(new Attribute<>(name, attribute));
    }

    /**
     * Add an attribute
     * @param attribute The attribute to add
     * @return The attributable
     */
    @Override
    public Attributable addAttribute(Attribute<?> attribute) {
        this.attributes.put(attribute.getName(), attribute);
        return this;
    }

    /**
     * Get the attribute
     *
     * @param name The attribute name
     * @return The attribute
     */
    @Override
    public Optional<Attribute<?>> getAttribute(String name) {
        return Optional.ofNullable(this.attributes.get(name));
    }

    /**
     * Has the attribute
     * @param name The attribute name
     * @return Whether attribute is contained
     */
    @Override
    public boolean hasAttribute(final String name) {
        return this.attributes.containsKey(name);
    }

    /**
     * Gets the attributes
     * @return The collection of attributes
     */
    public Stream<Attribute<?>> getAttributes() {
        return this.attributes.values().stream();
    }
}
