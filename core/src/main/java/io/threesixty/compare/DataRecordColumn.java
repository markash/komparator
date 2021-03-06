package io.threesixty.compare;

import org.springframework.util.StringUtils;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataRecordColumn implements Comparable<DataRecordColumn> {
    private String name;
    private Class dataType;
    private int length;
    private boolean key;

    public DataRecordColumn(final String name, final Class dataType) {
        this(name, dataType, 0, false);
    }

    public DataRecordColumn(final String name, final Class dataType, final boolean key) {
        this(name, dataType, 0, key);
    }

    public DataRecordColumn(final String name, final Class dataType, final int length, final boolean key) {
        this.name = name;
        this.dataType = dataType;
        this.length = length;
        this.key = key;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Class getDataType() { return dataType; }
    public void setDataType(Class dataType) { this.dataType = dataType; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public boolean isKey() { return key; }
    public void setKey(boolean key) { this.key = key; }

    @Override
    public int compareTo(final DataRecordColumn o) {
        final String value = !StringUtils.isEmpty(this.name) ? this.name : "";
        final String other = o != null && !StringUtils.isEmpty(o.getName()) ? o.getName() : "";
        return value.compareTo(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRecordColumn that = (DataRecordColumn) o;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
