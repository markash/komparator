package io.threesixty.compare.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import io.threesixty.compare.Attribute;
import io.threesixty.compare.DataRecord;
import io.threesixty.compare.DataRecordColumn;
import io.threesixty.compare.DataRecordSet;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 */
public class DataRecordPanel extends MPanel implements DataRecordProvider {

    private List<DataRecord> dataSource = new ArrayList<>();
    private ListDataProvider<DataRecord> dataProvider = new ListDataProvider<>(dataSource);
    private Grid<DataRecord> grid = new Grid<>(dataProvider);

    public DataRecordPanel(final String caption) {
        super(caption);

        configureDataGrid();
        configureDummyColumns();

        MHorizontalLayout content =
                new MHorizontalLayout(
                        grid)
                .withMargin(false)
                .withSpacing(false)
                .withFullWidth()
                .withFullHeight();

        this.setContent(content);
    }

    public List<DataRecord> getDataRecords() {
        return dataSource;
    }

    public void setDataRecordSet(final DataRecordSet recordSet) {
        setColumns(recordSet.getColumns());
        setRecords(recordSet.getRecords());
    }

    private void configureDataGrid() {
        grid.setWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setHeightByRows(5);
    }

    private void configureDummyColumns() {
        List<DataRecordColumn> columns = new ArrayList<>();
        columns.add(new DataRecordColumn("Column 1", String.class));
        columns.add(new DataRecordColumn("Column 2", String.class));
        columns.add(new DataRecordColumn("Column 3", String.class));
        setColumns(columns);
    }

    /**
     * Setup the column of the grid
     * @param columns The columns of the data record set
     */
    private void setColumns(final List<DataRecordColumn> columns) {
        grid.removeAllColumns();
        Grid.Column<DataRecord, ?> gridColumn;
        for (DataRecordColumn column : columns) {
            gridColumn = grid.addColumn(dataRecord -> provideValueForAttribute(dataRecord, column));
            gridColumn.setCaption(column.getName());
        }
    }

    /**
     * Provides the value for the given column of the data record
     * @param dataRecord The data record
     * @param column The column to provide the value for
     * @return The value of the attribute
     */
    private Object provideValueForAttribute(DataRecord dataRecord, DataRecordColumn column) {
        return dataRecord
                .getAttribute(column.getName())
                .orElse(Attribute.create(column.getName(), "", column.isKey()))
                .getValue();
    }

    /**
     * Setup the data provider of the grid
     * @param records The records of the record set
     */
    private void setRecords(List<DataRecord> records) {
        /*  */
        dataSource.addAll(records);
        dataProvider.refreshAll();
    }
}
