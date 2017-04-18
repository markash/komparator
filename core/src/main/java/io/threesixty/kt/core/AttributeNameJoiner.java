package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth
 */
public class AttributeNameJoiner implements AttributeJoiner {
    @Override
    public boolean matches(Attribute left, Attribute right) {
        return !isNull(left, right) && isNameEqual(left, right);
    }

    boolean isNull(Attribute left, Attribute right) {
        return (left == null || right == null || left.getName() == null || right.getName() == null);
    }

    private boolean isNameEqual(Attribute left, Attribute right) {
        return left.getName().equalsIgnoreCase(right.getName());
    }
}
