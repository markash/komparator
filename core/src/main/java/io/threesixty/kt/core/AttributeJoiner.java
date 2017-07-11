package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@FunctionalInterface
public interface AttributeJoiner {
    boolean matches(final Attribute left, final Attribute right);
}
