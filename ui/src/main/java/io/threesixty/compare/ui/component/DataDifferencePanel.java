package io.threesixty.compare.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import io.threesixty.compare.DataRecordColumn;
import io.threesixty.compare.result.DifferenceRecord;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class DataDifferencePanel extends MPanel implements DataDifferenceProvider {

    private final List<DifferenceRecord> dataSource = new ArrayList<>();
    private final ListDataProvider<DifferenceRecord> dataProvider = new ListDataProvider<>(dataSource);
    private final Grid<DifferenceRecord> grid = new Grid<>(dataProvider);

    public DataDifferencePanel(final String caption) {
        super(caption);

        configureDataGrid();
        configureDummyColumns();

        MHorizontalLayout content =
                new MHorizontalLayout(grid)
                .withMargin(false)
                .withSpacing(false)
                .withFullWidth()
                .withFullHeight();

        this.setContent(content);
    }

    @Override
    public void setAttributeMapping() {

    }

    @Override
    public void setDataDifferences(List<DifferenceRecord> dataDifferences) {
        setRecords(dataDifferences);
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
        Grid.Column<DifferenceRecord, String> gridColumn;
        for (DataRecordColumn column : columns) {
            gridColumn = grid.addColumn(dataRecord -> dataRecord.get(column.getName()).toHtml());
            gridColumn.setCaption(column.getName());
            gridColumn.setRenderer(new HtmlRenderer());
        }

        grid.addColumn(DifferenceRecord::getResultType).setCaption("Difference");
    }

    /**
     * Setup the data provider of the grid
     * @param records The records of the record set
     */
    private void setRecords(final List<DifferenceRecord> records) {

        List<DataRecordColumn> columns =
                records.stream()
                .limit(1)
                .map(record -> record.getAttributeNames().stream())
                .flatMap(stream -> stream)
                .map(attributeName -> new DataRecordColumn(attributeName, String.class))
                .collect(Collectors.toList());

        setColumns(columns);
        dataSource.clear();
        dataSource.addAll(records);
        dataProvider.refreshAll();
    }
}
