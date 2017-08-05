package io.threesixty.compare;

/**
 * @author Mark P Ashworth
 */
public class AttributeMappingJoiner extends AttributeNameJoiner {

    private final AttributeMapping mapping;

    public AttributeMappingJoiner(final AttributeMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public boolean matches(final Attribute left, final Attribute right) {
        return !isNull(left, right) && isNameEqual(left, right);
    }

    private boolean isNameEqual(Attribute left, Attribute right) {
        String name = mapping.getMappingForSource(left.getName());
        String other = mapping.getMappingForSource(right.getName());
        return (name != null && name.equalsIgnoreCase(right.getName())) || (other != null && other.equalsIgnoreCase(left.getName()));
    }
}
