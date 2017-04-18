package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth
 */
@FunctionalInterface
public interface AttributeJoiner {
    boolean matches(final Attribute left, final Attribute right);
}
