package io.threesixty.kt.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AttributableObject implements Attributable {
    private Map<String, Attribute<?>> attributes;

    public AttributableObject() {
        this.attributes = new HashMap<>();
    }

    public AttributableObject(final Collection<Attribute<?>> attributes) {
        this.attributes = new HashMap<>();
        if (attributes != null) {
            for (Attribute<?> attribute : attributes) {
                this.attributes.put(attribute.getName(), attribute);
            }
        }
    }

    /**
     * Add an attribute
     *
     * @param name      The name of the attribute
     * @param attribute The value of the attribute
     * @return The attributable
     */
    @Override
    public <T> Attributable addAttribute(String name, T attribute) {
        return addAttribute(new Attribute<>(name, attribute));
    }

    /**
     * Add an attribute
     *
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
    public Attribute<?> getAttribute(String name) {
        return this.attributes.get(name);
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
    public Collection<Attribute<?>> getAttributes() {
        return this.attributes.values();
    }
}
