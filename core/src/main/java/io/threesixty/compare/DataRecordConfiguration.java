package io.threesixty.compare;


import net.sf.flatpack.structure.ColumnMetaData;
import net.sf.flatpack.xml.MapParser;
import net.sf.flatpack.xml.MetaData;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mark P Ashworth
 */
public class DataRecordConfiguration {
    private static final String PREAMBLE = "<?xml version='1.0'?>\n" +
            "<!DOCTYPE PZMAP SYSTEM\n" +
            "\t\"flatpack.dtd\" >\n" +
            "<PZMAP>\n";
    private static final String POSTAMBLE = "</PZMAP>";

    private String name;
    private DataRecordFileType fileType;
    private char delimiter = ',';
    private char qualifier = '\"';
    private boolean ignoreFirstRecord = true;
    private List<DataRecordColumn> columns = new ArrayList<>();

    public static DataRecordConfiguration from(final File file) {

        try (FileReader reader = new FileReader(file)) {
            DataRecordConfiguration configuration = new DataRecordConfiguration();
            configuration.setName(file.getName().replace(".pzmap.xml", ""));
            MetaData metaData = MapParser.parseMap(reader, null);
            for(ColumnMetaData columnMetaData : metaData.getColumnsNames()) {
                configuration.addColumn(new DataRecordColumn(columnMetaData.getColName(), String.class));
            }
            return configuration;
        } catch (Exception e) {
            throw new ConfigurationException("Unable to parse configuration file", e);
        }
    }

    public DataRecordConfiguration() { }

    public DataRecordConfiguration(final String name, final DataRecordFileType fileType) {
        this.name = name;
        this.fileType = fileType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DataRecordFileType getFileType() { return fileType; }
    public void setFileType(DataRecordFileType fileType) { this.fileType = fileType; }

    public char getDelimiter() { return delimiter; }
    public void setDelimiter(char delimiter) { this.delimiter = delimiter; }

    public char getQualifier() { return qualifier; }
    public void setQualifier(char qualifier) { this.qualifier = qualifier; }

    public boolean isIgnoreFirstRecord() { return ignoreFirstRecord; }
    public void setIgnoreFirstRecord(boolean ignoreFirstRecord) { this.ignoreFirstRecord = ignoreFirstRecord; }

    public List<DataRecordColumn> getColumns() { return columns; }
    public void setColumns(List<DataRecordColumn> columns) { this.columns = columns; }

    public void addColumn(final DataRecordColumn column) { this.columns.add(column); }
    public Optional<DataRecordColumn> getColumn(final String name) { return this.columns.stream().filter(e -> e.getName().equals(name.trim())).findFirst(); }

    /**
     * Determines whether the given column name is one of the key columns
     * @param name The name of the column
     * @return Whether the column is a key column
     */
    public boolean isKeyColumn(final String name) {
        return getColumn(name).map(DataRecordColumn::isKey).orElse(false);
    }

    public Reader getParserConfiguration() {
        StringBuilder config = new StringBuilder(PREAMBLE);
        for (DataRecordColumn column : columns) {
            config.append(getColumnNode(column));
        }
        config.append(POSTAMBLE);
        return new StringReader(config.toString());
    }

    private String getColumnNode(final DataRecordColumn column) {
        return "<COLUMN name=\"" + column.getName() + "\" length=\"" + column.getLength() + "\" />\n";
    }

    public DataRecordConfiguration withDelimiter(final char delimiter) {
        this.setDelimiter(delimiter);
        return this;
    }

    public DataRecordConfiguration withId(final String name, final Class dataType) {
        this.columns.add(new DataRecordColumn(name, dataType, true));
        return this;
    }

    public DataRecordConfiguration withColumn(final String name, final Class dataType) {
        this.columns.add(new DataRecordColumn(name, dataType, false));
        return this;
    }

    public DataRecordConfiguration withColumn(final String name, final Class dataType, final int length) {
        this.columns.add(new DataRecordColumn(name, dataType, length, false));
        return this;
    }

    @Override
    public String toString() { return this.name; }
}
