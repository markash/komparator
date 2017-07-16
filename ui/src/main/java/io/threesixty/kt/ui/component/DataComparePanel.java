package io.threesixty.kt.ui.component;

import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import io.threesixty.kt.core.*;
import io.threesixty.kt.core.reader.FileDataRecordReader;
import io.threesixty.kt.ui.service.PersistenceService;
import io.threesixty.ui.component.uploader.UploadReceiver;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;

public class DataComparePanel extends MPanel {

    private final ComboBox<DataRecordConfiguration> sourceRecordConfigurations;
    private final ComboBox<DataRecordConfiguration> targetRecordConfigurations;
    private final ComboBox<AttributeMapping> mappingConfigurations;

    private final Upload sourceDataUpload;
    private final Upload targetDataUpload;
    private final MButton compareButton;
    private DataRecordConfiguration sourceRecordConfiguration;
    private DataRecordConfiguration targetRecordConfiguration;
    private AttributeMapping attributeMapping;

    private final DataRecordProvider sourceDataProvider;
    private final DataRecordProvider targetDataProvider;
    private final DataDifferenceProvider dataDifferenceProvider;

    public DataComparePanel(
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
        this.sourceRecordConfigurations = new ComboBox<>("Format", configurations);
        this.sourceRecordConfigurations.addSelectionListener(this::onSourceSelection);
        this.targetRecordConfigurations = new ComboBox<>("Format", configurations);
        this.targetRecordConfigurations.addSelectionListener(this::onTargetSelection);
        this.sourceDataUpload = createDataUpload(new UploadReceiver(this::onReceiveSourceData), "Upload data", false);
        this.targetDataUpload = createDataUpload(new UploadReceiver(this::onReceiveTargetData), "Upload data", false);
        this.compareButton = new MButton("Compare", this::onCompare);
        this.compareButton.setEnabled(false);

        MVerticalLayout content =
                new MVerticalLayout(
                        new MLabel()
                            .withContent("<h3><span class=\"step\">1</span>Define the master data, i.e. source data</h3>")
                            .withContentMode(ContentMode.HTML),
                        createDataPanel(this.sourceRecordConfigurations, this.sourceDataUpload),
                        new MLabel()
                                .withContent("<h3><span class=\"step\">2</span>Define the data to compare to the master, i.e. target data</h3>")
                                .withContentMode(ContentMode.HTML),
                        createDataPanel(this.targetRecordConfigurations, this.targetDataUpload),
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

        this.setContent(content);
    }

    private static FormLayout createDataPanel(ComboBox<DataRecordConfiguration> configurations, final Upload dataUpload) {
        FormLayout layout = new FormLayout();
        layout.addComponent(configurations);
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
            FileDataRecordReader reader = new FileDataRecordReader();
            DataRecordSet recordSet = reader.read(sourceRecordConfiguration, new FileReader(file));
            this.sourceDataProvider.setDataRecordSet(recordSet);
        } catch (FileNotFoundException e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }

    private void onReceiveTargetData(final File file, final String mimeType, final long length) {
        try {
            FileDataRecordReader reader = new FileDataRecordReader();
            DataRecordSet recordSet = reader.read(targetRecordConfiguration, new FileReader(file));
            this.targetDataProvider.setDataRecordSet(recordSet);
        } catch (FileNotFoundException e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }

    private void onCompare(final Button.ClickEvent event) {

        try {
            Comparison service = new Comparison();
            dataDifferenceProvider.setDataDifferences(service.convert(service.compare(
                    sourceDataProvider.getDataRecords(),
                    targetDataProvider.getDataRecords(),
                    attributeMapping)));
        } catch (Exception e) {
            new Notification(e.getMessage()).show(Page.getCurrent());
        }
    }
}
