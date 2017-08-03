package io.threesixty.kt.core;

import org.concordion.api.ConcordionResources;
import org.concordion.api.FullOGNL;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.runner.RunWith;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author Mark P Ashworth
 */
@FullOGNL
@RunWith(ConcordionRunner.class)
@ConcordionResources( value = { "person-mapping.png", "invoice-mapping.png" } )
public class AttributeFixture {

    private DefaultConversionService conversionService = new DefaultConversionService();

    public AttributeFixture() {
        conversionService.addConverter(new LocalDateConverter("yyyy-MM-dd"));
        conversionService.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

    public Tuple2<String, Object> create(String value, String type) {

        try {
            Object v = new Attribute.AttributeBuilder(conversionService)
                    .from(config(type), entry("Attribute", value))
                    .v2.getValue();

            return new Tuple2<>(v.getClass().getName(), v);

        } catch (ConversionException e) {
            return new Tuple2<>(e.getMessage(), null);
        }
    }

    public Map.Entry<String, String> entry(String key, String value) {
        return new AbstractMap.SimpleEntry(key, value.trim());
    }

    public DataRecordConfiguration config(String type) {
        Class clazz = String.class;

        switch (type.trim()) {
            case "Long":
                clazz = Long.class;
                break;
            case "Int":
            case "Integer":
                clazz = Integer.class;
                break;
            case "Dbl":
            case "Double":
                clazz = Double.class;
                break;
            case "Date":
                clazz = LocalDate.class;
                break;
            case "DateTime":
                clazz = LocalDateTime.class;
                break;
            default:
                clazz = String.class;
                break;
        }

        return new DataRecordConfiguration().withColumn("Attribute", clazz);
    }


    public final class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

        private final DateTimeFormatter formatter;

        public LocalDateTimeConverter(String dateFormat) {
            this.formatter = DateTimeFormatter.ofPattern(dateFormat);
        }

        @Override
        public LocalDateTime convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }

            return LocalDateTime.parse(source, formatter);
        }
    }

    public final class LocalDateConverter implements Converter<String, LocalDate> {

        private final DateTimeFormatter formatter;

        public LocalDateConverter(String dateFormat) {
            this.formatter = DateTimeFormatter.ofPattern(dateFormat);
        }

        @Override
        public LocalDate convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }

            return LocalDate.parse(source, formatter);
        }
    }
}
