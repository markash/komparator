package io.threesixty.compare.ui.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import io.threesixty.compare.*;
import io.threesixty.compare.excel.reader.ExcelDataRecordProvider;
import io.threesixty.compare.excel.reader.FilloExt;
import io.threesixty.compare.result.DifferenceRecord;
import io.threesixty.compare.result.ResultRecordToDifferenceRecord;
import io.threesixty.compare.ui.service.PersistenceService;
import io.threesixty.ui.component.uploader.UploadReceiver;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelDataComparePanel extends MPanel {

    //private final ComboBox<DataRecordConfiguration> sourceRecordConfigurations;
    //private final ComboBox<DataRecordConfiguration> targetRecordConfigurations;
    private final ComboBox<AttributeMapping> mappingConfigurations;

    private final MTextField sourceDataQuery;
    private final MTextField targetDataQuery;
    private final Upload sourceDataUpload;
    private final Upload targetDataUpload;
    private final MButton compareButton;

    private final List<String> sheetsDataSource = new ArrayList<>();
    private final ListDataProvider<String> sheetsProvider = new ListDataProvider<>(sheetsDataSource);
    private final PopupView sheetSelection;

    private DataRecordConfiguration sourceRecordConfiguration;
    private DataRecordConfiguration targetRecordConfiguration;
    private AttributeMapping attributeMapping;

    private final DataRecordProvider sourceDataProvider;
    private final DataRecordProvider targetDataProvider;
    private final DataDifferenceProvider dataDifferenceProvider;

    public ExcelDataComparePanel(
            final String caption,
            final DataRecordProvider sourceDataProvider,
            final DataRecordProvider targetDataProvider,
            final DataDifferenceProvider dataDifferenceProvider,
            final PersistenceService persistenceService) {
        super(caption);

        this.sourceDataProvider = sourceDataProvider;
        this.targetDataProvider = targetDataProvider;
        this.dataDifferenceProvider = dataDifferenceProvider;

        Collection<DataRecordConfiguration> configurations = persistenceService.retrieveDataRecordConfigurations();
        Collection<AttributeMapping> mappings = persistenceService.retrieveDataRecordAttributeMappings();

        this.mappingConfigurations = new ComboBox<>("Mapping", mappings);
        this.mappingConfigurations.addSelectionListener(this::onMappingSelection);
        //this.sourceRecordConfigurations = new ComboBox<>("Format", configurations);
        //this.sourceRecordConfigurations.addSelectionListener(this::onSourceSelection);
        this.sourceDataQuery = new MTextField("SQL", "SELECT * FROM Sheet1");

        //this.targetRecordConfigurations = new ComboBox<>("Format", configurations);
        //this.targetRecordConfigurations.addSelectionListener(this::onTargetSelection);
        this.targetDataQuery = new MTextField("SQL", "SELECT * FROM Sheet1");

        this.sourceDataUpload = createDataUpload(new UploadReceiver(this::onReceiveSourceData), "Upload data", true);
        this.targetDataUpload = createDataUpload(new UploadReceiver(this::onReceiveTargetData), "Upload data", true);

        this.compareButton = new MButton("Compare", this::onCompare);
        this.compareButton.setEnabled(false);

        MVerticalLayout content =
                new MVerticalLayout(
                        new MLabel()
                            .withContent("<h3><span class=\"step\">1</span>Define the master data, i.e. source data</h3>")
                            .withContentMode(ContentMode.HTML),
                        createDataPanel(this.sourceDataQuery, this.sourceDataUpload),
                        new MLabel()
                                .withContent("<h3><span class=\"step\">2</span>Define the data to compare to the master, i.e. target data</h3>")
                                .withContentMode(ContentMode.HTML),
                        createDataPanel(this.targetDataQuery, this.targetDataUpload),
                        new MLabel()
                                .withContent("<h3><span class=\"step\">3</span>Define the mapping between the source and target data</h3>")
                                .withContentMode(ContentMode.HTML),
                        this.mappingConfigurations,
                        new MLabel()
                                .withContent("<h3><span class=\"step\">4</span>Perform the comparison</h3>")
                                .withContentMode(ContentMode.HTML),
                        this.compareButton
                        ).withMargin(true)
                        .withSpacing(false)
                        .withFullWidth();

        ComboBox<String> sheetsComponent = new ComboBox<>("Sheets");
        sheetsComponent.setDataProvider(sheetsProvider);

        VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(sheetsComponent);
        popupContent.addComponent(new Button("Select"));

        this.sheetSelection = new PopupView("Sheet Selection", popupContent);
        content.addComponent(sheetSelection);

        this.setContent(content);

    }

    private static FormLayout createDataPanel(MTextField sqlField, final Upload dataUpload) {
        FormLayout layout = new FormLayout();
        layout.addComponent(sqlField);
        layout.addComponent(dataUpload);
        return layout;
    }

    private static Upload createDataUpload(final UploadReceiver fileReceiver, final String buttonCaption, final boolean enabled) {
        Upload fileUpload = new Upload(null, fileReceiver);
        fileUpload.addSucceededListener(fileReceiver);
        fileUpload.setButtonCaption(buttonCaption);
        fileUpload.setEnabled(enabled);

        return fileUpload;
    }

    private void onSourceSelection(final SingleSelectionEvent<DataRecordConfiguration> event) {
        this.sourceRecordConfiguration = event.getValue();
        this.sourceDataUpload.setEnabled(true);
    }

    private void onTargetSelection(final SingleSelectionEvent<DataRecordConfiguration> event) {
        this.targetRecordConfiguration = event.getValue();
        this.targetDataUpload.setEnabled(true);
    }

    private void onMappingSelection(final SingleSelectionEvent<AttributeMapping> event) {
        this.attributeMapping = event.getValue();
        this.compareButton.setEnabled(true);
    }

    private void onReceiveSourceData(final File file, final String mimeType, final long length) {
        try {
            Registration registration = this.sheetSelection.addPopupVisibilityListener(event -> {
               if (!event.isPopupVisible()) {
                   System.out.println("Hiding");
               }
            });

            this.sheetsDataSource.clear();
            this.sheetsDataSource.addAll(FilloExt.getSheetNames(file));
            this.sheetsProvider.refreshAll();

            this.sheetSelection.setPopupVisible(true);

            List<DataRecord> results = new ExcelDataRecordProvider(this.sourceDataQuery.getValue())
                    .fetch(FilloExt.getConnection(file))
                    .collect(Collectors.toList());

            this.sourceDataProvider.setDataRecordSet(new DataRecordSet(results));
        } catch (Exception e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }

    private void onReceiveTargetData(final File file, final String mimeType, final long length) {
        try {
            List<String> sheetNames = FilloExt.getSheetNames(file);
            sheetNames.forEach(System.out::println);

            List<DataRecord> results = new ExcelDataRecordProvider(this.targetDataQuery.getValue())
                    .fetch(FilloExt.getConnection(file))
                    .collect(Collectors.toList());

            this.targetDataProvider.setDataRecordSet(new DataRecordSet(results));
        } catch (Exception e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }

    private void onCompare(final Button.ClickEvent event) {

        try {
            ResultRecordToDifferenceRecord toDifferenceRecord = new ResultRecordToDifferenceRecord();

            List<DifferenceRecord> differences =
                    new Comparison().compare(
                            sourceDataProvider.getDataRecords(),
                            targetDataProvider.getDataRecords(),
                            attributeMapping)
                            .map(toDifferenceRecord)
                            .collect(Collectors.toList());

            dataDifferenceProvider.setDataDifferences(differences);
        } catch (Exception e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }
}
