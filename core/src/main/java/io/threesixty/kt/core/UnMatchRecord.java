package io.threesixty.kt.core;

import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mark P Ashworth
 */
public class UnMatchRecord extends AbstractResultRecord {
    private DataRecord record;

    protected UnMatchRecord(DataRecord record, ResultType resultType) {
        super(resultType);
        this.record = record;
    }

    @Override
    public Id2<Long> getId() { return record.id; }
    public DataRecord getRecord() { return record; }

    public List<Tuple2<Attribute, Attribute>> getDifferences() {
        List<Tuple2<Attribute, Attribute>> results = new ArrayList<>();

        for(Attribute attribute : record.attributes.values()){
            results.add(new Tuple2<Attribute, Attribute>(attribute, new Attribute(attribute.getName(), null)));
        }
        return results;
    }

    public static UnMatchRecord sourceUnmatched(DataRecord record) {
        return new UnMatchRecord(record, ResultType.SOURCE_UNMATCHED);
    }

    public static UnMatchRecord targetUnmatched(DataRecord record, final AttributeMapping attributeMapping) {

        /* Map the target attributes to the source using the attribute mapping
         * For attributes that only exist in the target but not the source, use the target attribute name
         */
        String mappedName;
        List<Attribute<?>> attributes = new ArrayList<>();
        for(Map.Entry<String, Attribute<?>> entry : record.attributes.entrySet()) {
            mappedName = attributeMapping.getMappingForTarget(entry.getKey());
            mappedName = mappedName != null ? mappedName : entry.getKey();
            attributes.add(new Attribute<>(mappedName, entry.getValue().getValue()));
        }
        return new UnMatchRecord(new DataRecord(record.id, attributes), ResultType.TARGET_UNMATCHED);
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
