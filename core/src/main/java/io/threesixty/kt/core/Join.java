package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth
 */
@FunctionalInterface
public interface Join<L, R> {
    boolean matches(final L left, final R right);
}
