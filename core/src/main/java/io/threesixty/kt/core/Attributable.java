package io.threesixty.kt.core;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface Attributable extends Serializable {
    /**
     * Add an attribute
     * @param name The name of the attribute
     * @param attribute The value of the attribute
     * @return The attributable
     */
    <T> Attributable addAttribute(final String name, final T attribute);

    /**
     * Add an attribute
     * @param attribute The attribute to add
     * @return The attributable
     */
    Attributable addAttribute(final Attribute<?> attribute);

    /**
     * Get the attribute
     * @param name The attribute name
     * @return The attribute
     */
    Attribute<?> getAttribute(final String name);

    /**
     * Has the attribute
     * @param name The attribute name
     * @return Whether attribute is contained
     */
    boolean hasAttribute(final String name);

    /**
     * Gets the attributes
     * @return The collection of attributes
     */
    Collection<Attribute<?>> getAttributes();
}
