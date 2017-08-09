package io.threesixty.compare.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import io.threesixty.compare.DataRecord;
import io.threesixty.compare.DataRecordSet;
import io.threesixty.compare.excel.reader.ExcelDataRecordProvider;
import io.threesixty.compare.excel.reader.FilloExt;
import io.threesixty.ui.component.uploader.UploadReceiver;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.ui.MNotification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ExcelUploadForm extends FormLayout {

    private final MTextField query = new MTextField("SQL", "SELECT * FROM Sheet1").withFullWidth();
    private final List<String> sheetsDataSource = new ArrayList<>();
    private final ListDataProvider<String> sheetsProvider = new ListDataProvider<>(sheetsDataSource);
    private final ComboBox<String> sheets = new ComboBox<>("Sheets");
    private final MButton readButton = new MButton(VaadinIcons.ARROW_CIRCLE_RIGHT, "Read Excel", this::onReadData);
    private final Upload uploadButton = createDataUpload(new UploadReceiver(this::onReceiveData), "Upload Excel");
    private final DataRecordProvider dataProvider;
    private File file;

    ExcelUploadForm(final DataRecordProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.sheets.setDataProvider(sheetsProvider);
        this.sheets.addSelectionListener(this::onSheetSelected);
        toggleUploadLinkedComponents(file != null);

        this.addComponent(sheets);
        this.addComponent(query);
        this.addComponent(new MHorizontalLayout().add(uploadButton, readButton));
    }

    private void onSheetSelected(final SingleSelectionEvent<String> event) {
        this.query.setValue("SELECT * FROM " + event.getValue());
    }

    private void onReceiveData(final File file, final String mimeType, final long length) {
        this.file = file;
        toggleUploadLinkedComponents(file != null);

        try {
            refreshSheetNames(file);
        } catch (Exception e) {
            new MNotification("Unable to read Excel sheets", e.getMessage(), Notification.Type.ERROR_MESSAGE)
                    .withDelayMsec(60000)
                    .withPosition(Position.BOTTOM_CENTER)
                    .show(Page.getCurrent());
        }
    }

    private void onReadData(final Button.ClickEvent event) {
        try {
            if (StringUtils.isEmpty(this.query.getValue())) {
                throw new Exception("The query is required");
            }

            List<String> sheetNames = FilloExt.getSheetNames(this.file);
            sheetNames.forEach(System.out::println);

            List<DataRecord> results = new ExcelDataRecordProvider(this.query.getValue())
                    .fetch(FilloExt.getConnection(file))
                    .collect(Collectors.toList());

            this.dataProvider.setDataRecordSet(new DataRecordSet(results));

            new MNotification("Excel data", "Read " + results.size() + " rows.", Notification.Type.HUMANIZED_MESSAGE)
                    .withDelayMsec(60000)
                    .withPosition(Position.BOTTOM_CENTER)
                    .show(Page.getCurrent());
        } catch (Exception e) {
            new MNotification("Unable to read Excel data", e.getMessage(), Notification.Type.ERROR_MESSAGE)
                    .withDelayMsec(60000)
                    .withPosition(Position.BOTTOM_CENTER)
                    .show(Page.getCurrent());
        }
    }

    private void refreshSheetNames(final File file) throws Exception {
        this.sheetsDataSource.clear();
        this.sheetsDataSource.addAll(FilloExt.getSheetNames(file));
        this.sheetsProvider.refreshAll();
    }

    private void toggleUploadLinkedComponents(final boolean value) {
        this.sheets.setEnabled(value);
        this.query.setEnabled(value);
        this.readButton.setEnabled(value);
    }

    private static Upload createDataUpload(final UploadReceiver fileReceiver, final String buttonCaption) {
        Upload fileUpload = new Upload(null, fileReceiver);
        fileUpload.addSucceededListener(fileReceiver);
        fileUpload.setButtonCaption(buttonCaption);
        fileUpload.setEnabled(true);
        return fileUpload;
    }
}
