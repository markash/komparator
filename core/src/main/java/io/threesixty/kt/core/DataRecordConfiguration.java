package io.threesixty.kt.core;


import net.sf.flatpack.structure.ColumnMetaData;
import net.sf.flatpack.xml.MapParser;
import net.sf.flatpack.xml.MetaData;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 */
public class DataRecordConfiguration {
    private static final String PREAMBLE = "<?xml version='1.0'?>\n" +
            "<!DOCTYPE PZMAP SYSTEM\n" +
            "\t\"flatpack.dtd\" >\n" +
            "<PZMAP>\n";
    private static final String POSTAMBLE = "</PZMAP>";

    private DataRecordFileType fileType;
    private char delimeter = ',';
    private char qualifier = '\"';
    private boolean ignoreFirstRecord = true;
    private List<DataRecordColumn> columns = new ArrayList<>();

    public static DataRecordConfiguration from(final File file) {

        try (FileReader reader = new FileReader(file)) {
            DataRecordConfiguration configuration = new DataRecordConfiguration();
            MetaData metaData = MapParser.parseMap(reader, null);
            for(ColumnMetaData columnMetaData : metaData.getColumnsNames()) {
                configuration.addColumn(new DataRecordColumn(columnMetaData.getColName(), String.class));
            }
            return configuration;
        } catch (Exception e) {
            throw new ConfigurationException("Unable to parse configuration file", e);
        }
    }

    public DataRecordFileType getFileType() { return fileType; }
    public void setFileType(DataRecordFileType fileType) { this.fileType = fileType; }

    public char getDelimeter() { return delimeter; }
    public void setDelimeter(char delimeter) { this.delimeter = delimeter; }

    public char getQualifier() { return qualifier; }
    public void setQualifier(char qualifier) { this.qualifier = qualifier; }

    public boolean isIgnoreFirstRecord() { return ignoreFirstRecord; }
    public void setIgnoreFirstRecord(boolean ignoreFirstRecord) { this.ignoreFirstRecord = ignoreFirstRecord; }

    public List<DataRecordColumn> getColumns() { return columns; }
    public void setColumns(List<DataRecordColumn> columns) { this.columns = columns; }

    public void addColumn(final DataRecordColumn column) {
        this.columns.add(column);
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
}
