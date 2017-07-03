package io.threesixty.kt.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import io.threesixty.kt.core.*;
import io.threesixty.ui.component.uploader.UploadReceiver;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 */
public class DataRecordPanel extends MPanel implements DataRecordProvider {

    private DataRecordReader reader = new DataRecordReader();
    private List<DataRecord> dataSource = new ArrayList<>();
    private ListDataProvider<DataRecord> dataProvider = new ListDataProvider<>(dataSource);
    private Grid<DataRecord> grid = new Grid<>(dataProvider);
    private DataRecordConfiguration configuration = new DataRecordConfiguration();
    private Upload dataUpload;
    private Upload configUpload;

    public DataRecordPanel(final String caption) {
        super(caption);

        configureDataGrid();
        configureDummyColumns();
//        configUpload = createDataUpload(new UploadReceiver(this::onReceiveConfig), "Upload config", true);
//        dataUpload = createDataUpload(new UploadReceiver(this::onReceiveData), "Upload data", false);

//        MVerticalLayout buttonPanel = createButtonPanel(configUpload, dataUpload);

        MHorizontalLayout content =
                new MHorizontalLayout(
                        grid/*,
                        buttonPanel*/)
                .withMargin(false)
                .withSpacing(false)
                .withFullWidth();

        //content.setExpandRatio(grid, 10);
        //content.setExpandRatio(buttonPanel, 2);

        this.setContent(content);
    }

    public List<DataRecord> getDataRecords() {
        return dataSource;
    }

    public void setDataRecordSet(final DataRecordSet recordSet) {
        setColumns(recordSet.getColumns());
        setRecords(recordSet.getRecords());
    }

//    private void onReceiveConfig(final File file, final String mimeType, final long length) {
//        configuration = DataRecordConfiguration.from(file);
//        configuration.setFileType(DataRecordFileType.DELIMITED);
//        dataUpload.setEnabled(true);
//    }

//    private void onReceiveData(final File file, final String mimeType, final long length) {
//        try {
//            DataRecordSet recordSet = reader.read(configuration, new FileReader(file));
//            setColumns(recordSet.getColumns());
//            setRecords(recordSet.getRecords());
//        } catch (FileNotFoundException e) {
//            new Notification(e.getMessage()).show(Page.getCurrent());
//        }
//    }

    private void configureDataGrid() {
        grid.setWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setHeightByRows(5);
    }

//    private static Upload createDataUpload(final UploadReceiver fileReceiver, final String buttonCaption, final boolean enabled) {
//        Upload fileUpload = new Upload(null, fileReceiver);
//        fileUpload.addSucceededListener(fileReceiver);
//        fileUpload.setButtonCaption(buttonCaption);
//        fileUpload.setEnabled(enabled);
//
//        return fileUpload;
//    }

    private static MVerticalLayout createButtonPanel(final Component...components) {
        return new MVerticalLayout(components).withSpacing(true).withMargin(false);
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
            gridColumn = grid.addColumn(dataRecord -> dataRecord.get(column.getName()));
            gridColumn.setCaption(column.getName());
        }
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
