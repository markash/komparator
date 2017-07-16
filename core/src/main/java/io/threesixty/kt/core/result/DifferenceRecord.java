package io.threesixty.kt.core.result;

import io.threesixty.kt.core.*;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DifferenceRecord extends AbstractResultRecord {
    private Key key;
    private List<String> order;
    private Map<String, Difference> record;

    public DifferenceRecord(final ResultRecord record) throws RuntimeException {
        super(record.getResultType());
        if (record instanceof MatchRecord) {
            MatchRecord r = (MatchRecord) record;
            this.record = mapDifferencesToSourceRecord(r.getSourceRecord(), r.getTargetRecord(), r.getDifferences());
        } else if (record instanceof UnMatchRecord) {
            UnMatchRecord r = (UnMatchRecord) record;
            this.record = mapDifferencesToSourceRecord(r.getRecord(), null, r.getDifferences());
        } else if (record instanceof DifferenceRecord) {
            this.record = ((DifferenceRecord) record).getRecord();
        } else {
            throw new RuntimeException("Unknown result record type");
        }
    }

    @Override
    public Key getKey() { return this.key; }
    public Map<String, Difference> getRecord() { return record; }
    public Difference get(final String name) { return record.get(name); }

    /**
     * Maps the source data record (i.e. left hand side) to the differences recorded
     * @param sourceRecord The source data record
     * @param differences The differences between the source and target data record
     * @return Map of differences key'ed by the source attribute name
     */
    private Map<String, Difference> mapDifferencesToSourceRecord(
            final DataRecord sourceRecord,
            final DataRecord targetRecord,
            final List<Tuple2<Attribute, Attribute>> differences) {

        this.order = new ArrayList<>();
        Map<String, Difference> results = new HashMap<>();

        /* Pair up the key values */
        this.key = sourceRecord.getKey();
        //this.order.add(sourceRecord.id.getName());
        //results.put(sourceRecord.id.getName(), new Difference(sourceRecord.id, (targetRecord != null ? targetRecord.id : null)));


        /* Convert the list of differences to a map by source attribute name */
        Map<String, Tuple2<Attribute, Attribute>> differencesMap = new HashMap<>();
//        for(Tuple2<Attribute, Attribute> tuple : differences) {
//            differencesMap.put(tuple.v1.getName(), tuple);
//        }
        differences.forEach(difference -> differencesMap.put(difference.v1.getName(), difference));


        /* Pair up the source record attributes with the differences */
//        DataRecord record = new DataRecord();
//        record.addKey(sourceRecord.id);
        for(Attribute<?> attribute : sourceRecord.getCompleteAttributeList()) {
            if (differencesMap.containsKey(attribute.getName())) {
                results.put(attribute.getName(), new Difference(differencesMap.get(attribute.getName())));
            } else {
                results.put(attribute.getName(), new Difference(attribute, attribute));
            }
            this.order.add(attribute.getName());
        }
        return results;
    }

    public Object[] toArray() {

        Object[] results = new Object[record.values().size() + 1];
        results[0] = getResultType();

        int i = 1;
        Difference difference;
        for(String name : order) {
            difference = get(name);
            results[i++] = difference.getLeftValue() + (getResultType() == ResultType.MISMATCH && difference.isDifferent() ? " (" + difference.getRightValue() + ") " : "");
        }
        return results;
    }

    public Map<String, Object> toMap() {

        Map<String, Object> results = new HashMap<>();
        results.put("resultType", getResultType());

        Difference difference;
        for(String name : order) {
            difference = get(name);
            results.put(name, difference.getLeftValue() + (getResultType() == ResultType.MISMATCH && difference.isDifferent() ? " (" + difference.getRightValue() + ") " : ""));
        }
        return results;
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        for(Map.Entry<String, Difference> entry : record.entrySet()) {
            if (str.length() > 0) str.append(", ");
            str.append(entry.getValue());
        }

        return "DifferenceRecord{" +
                ", result=" + getResultType() +
                ", " + str.toString() +
                '}';
    }
}
