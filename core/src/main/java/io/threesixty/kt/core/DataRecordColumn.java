package io.threesixty.kt.core;

/**
 * @author Mark P Ashworth
 */
public class DataRecordColumn {
    private String name;
    private Class dataType;
    private int length;

    public DataRecordColumn(final String name, final Class dataType) {
        this(name, dataType, 0);
    }

    public DataRecordColumn(final String name, final Class dataType, final int length) {
        this.name = name;
        this.dataType = dataType;
        this.length = length;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Class getDataType() { return dataType; }
    public void setDataType(Class dataType) { this.dataType = dataType; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
}
