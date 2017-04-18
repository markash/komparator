package io.threesixty.kt.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import io.threesixty.kt.core.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.ui.component.uploader.UploadReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 */
public class DataDifferencePanel extends MPanel {

    private final List<DifferenceRecord> dataSource = new ArrayList<>();
    private final ListDataProvider<DifferenceRecord> dataProvider = new ListDataProvider<>(dataSource);
    private final Grid<DifferenceRecord> grid = new Grid<>(dataProvider);
    private final MButton compareButton = new MButton("Compare", this::onCompare);
    private final DataRecordProvider sourceDataProvider;
    private final DataRecordProvider targetDataProvider;

    public DataDifferencePanel(final String caption, final DataRecordProvider sourceDataProvider, final DataRecordProvider targetRecordProvider) {
        super(caption);
        this.sourceDataProvider = sourceDataProvider;
        this.targetDataProvider = targetRecordProvider;

        configureDataGrid();
        configureDummyColumns();

        MVerticalLayout buttonPanel = createButtonPanel(compareButton);

        MHorizontalLayout content =
                new MHorizontalLayout(
                        grid,
                        buttonPanel)
                .withMargin(false).withSpacing(false);

        content.setExpandRatio(grid, 10);
        content.setExpandRatio(buttonPanel, 2);

        this.setContent(content);
    }

    private void onCompare(final Button.ClickEvent event) {
        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.addMapping("ID", "ID");
        attributeMapping.addMapping("NAME", "FIRSTNAME");
        attributeMapping.addMapping("AGE", "AGE");

        ComparisonService service = new ComparisonService();
        setRecords(service.convert(service.compare(
                        sourceDataProvider.getDataRecords(),
                        targetDataProvider.getDataRecords(),
                        attributeMapping)));
    }

    private void configureDataGrid() {
        grid.setWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setHeightByRows(5);
    }

    private static Upload createDataUpload(final UploadReceiver fileReceiver, final String buttonCaption, final boolean enabled) {
        Upload fileUpload = new Upload(null, fileReceiver);
        fileUpload.addSucceededListener(fileReceiver);
        fileUpload.setButtonCaption(buttonCaption);
        fileUpload.setEnabled(enabled);

        return fileUpload;
    }

    private static MVerticalLayout createButtonPanel(final Component...components) {
        return new MVerticalLayout(components).withSpacing(true).withMargin(false);
    }

    private void configureDummyColumns() {
        List<DataRecordColumn> columns = new ArrayList<>();
        columns.add(new DataRecordColumn("ID", String.class));
        columns.add(new DataRecordColumn("NAME", String.class));
        columns.add(new DataRecordColumn("AGE", String.class));
        setColumns(columns);
    }

    /**
     * Setup the column of the grid
     * @param columns The columns of the data record set
     */
    private void setColumns(final List<DataRecordColumn> columns) {
        grid.removeAllColumns();
        Grid.Column<DifferenceRecord, ?> gridColumn;
        for (DataRecordColumn column : columns) {
            gridColumn = grid.addColumn(dataRecord -> dataRecord.get(column.getName()));
            gridColumn.setCaption(column.getName());
        }
    }

    /**
     * Setup the data provider of the grid
     * @param records The records of the record set
     */
    private void setRecords(List<DifferenceRecord> records) {
        dataSource.clear();
        dataSource.addAll(records);
        dataProvider.refreshAll();
    }
}
