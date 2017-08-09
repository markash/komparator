package io.threesixty.compare;

import io.threesixty.compare.converter.AttributeToStringConverter;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An attributable object that has a key and attributes
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecord extends AttributableObject {

    public DataRecord() {
        super();
    }

    public DataRecord(final DataRecord dataRecord) {
        this(dataRecord.getAttributes());
    }

    public DataRecord(final Stream<Attribute<?>> attributes) {
        super(attributes);
    }
    /**
     * Returns a map of the data record where the key is the attribute name and the value is the attribute value
     * @return A map of attribute name and values
     */
    public Map<String, Object> toMap() {
        return getAttributes().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }
    /**
     * Compares this DataRecord to another to determine the differences
     * @param other The other data record to compare to
     * @param join The joining strategy, i.e. by name or attribute mapping
     * @return A tuple of attributes that are different
     */
    public List<Tuple2<Attribute, Attribute>> difference(final DataRecord other, AttributeJoiner join) {
        return Seq.ofType(getAttributes(), Attribute.class)
                .innerJoin(Seq.ofType(other.getAttributes(), Attribute.class), join::matches)
                .filter(match -> !match.v1().getValue().equals(match.v2().getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Demote the attribute from key to regular attribute
     * @param attributeName The attribute name
     * @return If the attribute does not exist then false else
     *         If the attribute was a key and was demoted to an regular attribute
     */
    public boolean demoteFromKey(final String attributeName) {
        return setAttributeKeyValue(attributeName, false);
    }

    /**
     * Promote the attribute from an attribute to a key attribute
     * @param attributeName The attribute name
     * @return If the attribute does not exist then false else
     *         If the attribute was promoted to an key attribute
     */
    public boolean promoteToKey(final String attributeName) {
        return setAttributeKeyValue(attributeName, true);
    }

    private boolean setAttributeKeyValue(final String attributeName, final boolean key) {
        Optional<Attribute<?>> attribute = getAttribute(attributeName);
        if (attribute.isPresent()) {
            boolean result = !attribute.get().isKey() ^ key;
            addAttribute(Attribute.overrideKey(attribute.get(), key));
            return result;
        }
        return false;
    }

    public String getKeyAsString() {
        StringJoiner joiner = new StringJoiner("|");
        AttributeToStringConverter converter = new AttributeToStringConverter();
        getAttributes()
                .filter(Attribute::isKey)
                .map(converter::convert)
                .forEach(joiner::add);
        return joiner.toString();
    }

    public String getDataAsString() {
        StringJoiner joiner = new StringJoiner("|");
        AttributeToStringConverter converter = new AttributeToStringConverter(true);
        getAttributes()
                .filter(a -> !a.isKey())
                .map(converter::convert)
                .forEach(joiner::add);
        return joiner.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRecord that = (DataRecord) o;
        return getKeyAsString().equals(that.getKeyAsString());
    }

    @Override
    public int hashCode() {
        return getKeyAsString().hashCode();
    }


    @Override
    public String toString() { return "(" + getKeyAsString() + ") => "+ getDataAsString(); }

    public static class DataRecordBuilder {

        private Attribute.AttributeBuilder attributeBuilder;

        public DataRecordBuilder(final ConversionService conversionService) {
            this.attributeBuilder = new Attribute.AttributeBuilder(conversionService);
        }

        public DataRecord from(final DataRecordConfiguration configuration, final Map<String, ?> values) {
            DataRecord dataRecord = new DataRecord();
            values.entrySet().stream()
                    .map(a -> attributeBuilder.from(configuration, a))
                    .forEach(tuple -> {
                        /* If the data column is present and a key then attribute is a key */
                        //if (tuple.v1.isPresent() && tuple.v1.get().isKey()) {
                        //    dataRecord.addKey(tuple.v2);
                        //} else {
                            /* Else an attribute */
                            dataRecord.addAttribute(tuple.v2);
                        //}
                    });
            return dataRecord;
        }
    }
}
