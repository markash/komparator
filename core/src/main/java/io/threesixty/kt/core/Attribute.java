package io.threesixty.kt.core;

import org.jooq.lambda.tuple.Tuple2;
import org.springframework.core.convert.ConversionService;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

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

    public static class AttributeBuilder {

        private ConversionService conversionService;

        protected AttributeBuilder(final ConversionService conversionService) {
            this.conversionService = conversionService;
        }

        public <T> Tuple2<Optional<DataRecordColumn>, Attribute<T>> from(final DataRecordConfiguration configuration, Map.Entry<String, ?> entry) {
            /* Read the column configuration */
            final String columnName = entry.getKey().trim();
            Optional<DataRecordColumn> column = configuration.getColumn(columnName);
            if (column.isPresent()) {
                Class sourceType = entry.getValue().getClass();
                Class targetType = column.get().getDataType();

                if (conversionService.canConvert(sourceType, targetType)) {
                    return new Tuple2<>(Optional.of(column.get()), new Attribute(columnName, conversionService.convert(entry.getValue(), targetType)));
                }
            }
            return new Tuple2<>(Optional.empty(), new Attribute(columnName, entry.getValue()));
        }
    }
}