package io.threesixty.kt.core.result;

import io.threesixty.kt.core.*;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class UnMatchRecord extends AbstractResultRecord {
    private DataRecord record;

    protected UnMatchRecord(DataRecord record, ResultType resultType) {
        super(resultType);
        this.record = record;
    }

    @Override
    public Key getKey() { return record.getKey(); }
    public DataRecord getRecord() { return record; }

    public List<Tuple2<Attribute, Attribute>> getDifferences() {
        return record.getAttributes()
                .map(attribute -> new Tuple2<Attribute, Attribute>(attribute, new Attribute(attribute.getName(), null)))
                .collect(Collectors.toList());
    }

    public static ResultRecord sourceUnmatched(DataRecord record) {
        return new UnMatchRecord(record, ResultType.SOURCE_UNMATCHED);
    }

    public static ResultRecord targetUnmatched(DataRecord record, final AttributeMapping attributeMapping) {
        /* Map the target attributes to the source using the attribute mapping
         * For attributes that only exist in the target but not the source, use the target attribute name */
        Stream<Attribute<?>> attributes = record
                .getAttributes()
                .filter(attribute -> !record.getKey().hasAttribute(attribute.getName()))
                .map(attribute -> {
                    String mappedName = attributeMapping.getMappingForTarget(attribute.getName());
                    mappedName = mappedName != null ? mappedName : attribute.getName();
                    return Attribute.create(mappedName, attribute.getValue());
                });

        return new UnMatchRecord(new DataRecord(record.getKey(), attributes), ResultType.TARGET_UNMATCHED);
    }


    @Override
    public String toString() {
        String recordType;

        switch (getResultType()) {
            case SOURCE_UNMATCHED: recordType = "sourceRecord"; break;
            case TARGET_UNMATCHED: recordType = "targetRecord"; break;
            default: recordType = "record";
        }

        return "UnMatchRecord{" +
                recordType + "=" + record +
                ", result=" + getResultType() +
                '}';
    }
}
