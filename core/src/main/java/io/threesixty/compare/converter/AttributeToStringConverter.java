package io.threesixty.compare.converter;

import io.threesixty.compare.Attribute;
import org.springframework.core.convert.converter.Converter;

public class AttributeToStringConverter implements Converter<Attribute<?>, String> {
    private boolean includeName;

    public AttributeToStringConverter() {
        this(false);
    }

    public AttributeToStringConverter(final boolean includeName) {
        this.includeName = includeName;
    }

    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public String convert(Attribute<?> source) {
        if (source == null || source.getValue() == null) return null;

        final Object value = source.getValue();

        if (value instanceof String) {
            return format(source.getName(), (String) value);
        }

        return format(source.getName(), value.toString());
    }

    private String format(final String name, final String value) {
        return ((includeName) ? name + "=" : "") + value;
    }
}
