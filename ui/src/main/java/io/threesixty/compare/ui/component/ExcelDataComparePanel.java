package io.threesixty.compare.ui.component;

import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import io.threesixty.compare.AttributeMapping;
import io.threesixty.compare.Comparison;
import io.threesixty.compare.result.DifferenceRecord;
import io.threesixty.compare.result.ResultRecordToDifferenceRecord;
import io.threesixty.compare.ui.service.PersistenceService;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.ui.MNotification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelDataComparePanel extends MPanel {

    private final ComboBox<AttributeMapping> mappingConfigurations;
    private final MButton compareButton;
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

        Collection<AttributeMapping> mappings = persistenceService.retrieveDataRecordAttributeMappings();

        this.mappingConfigurations = new ComboBox<>("Mapping", mappings);
        this.mappingConfigurations.addSelectionListener(this::onMappingSelection);
        this.compareButton = new MButton("Compare", this::onCompare);
        this.compareButton.setEnabled(false);

        ExcelUploadForm sourceUploadForm = new ExcelUploadForm(sourceDataProvider);
        ExcelUploadForm targetUploadForm = new ExcelUploadForm(targetDataProvider);
        ExcelJoinForm dataJoinForm =
                new ExcelJoinForm(
                        sourceDataProvider,
                        targetDataProvider,
                        this::onSourceJoinSelection,
                        this::onTargetJoinSelection);

        MVerticalLayout content =
                new MVerticalLayout(
                        new MLabel()
                                .withContent("<h3><span class=\"step\">1</span>Define the master data, i.e. source data</h3>")
                                .withContentMode(ContentMode.HTML),
                        sourceUploadForm,

                        new MLabel()
                                .withContent("<h3><span class=\"step\">2</span>Define the data to compare to the master, i.e. target data</h3>")
                                .withContentMode(ContentMode.HTML),
                        targetUploadForm,

                        new MLabel()
                                .withContent("<h3><span class=\"step\">3</span>Define the join column between the source and target</h3>")
                                .withContentMode(ContentMode.HTML),
                        dataJoinForm,

                        new MLabel()
                                .withContent("<h3><span class=\"step\">4</span>Define the mapping between the source and target data</h3>")
                                .withContentMode(ContentMode.HTML),
                        this.mappingConfigurations,

                        new MLabel()
                                .withContent("<h3><span class=\"step\">5</span>Perform the comparison</h3>")
                                .withContentMode(ContentMode.HTML),
                        this.compareButton
                        ).withMargin(true)
                        .withSpacing(false)
                        .withFullWidth();

        this.setContent(content);

    }

    private void onSourceJoinSelection(final HasValue.ValueChangeEvent<String> event) {
        this.sourceDataProvider.getDataRecords().forEach(dataRecord -> {
            dataRecord.demoteFromKey(event.getOldValue());
            dataRecord.promoteToKey(event.getValue());
        });
    }

    private void onTargetJoinSelection(final HasValue.ValueChangeEvent<String> event) {
        this.targetDataProvider.getDataRecords().forEach(dataRecord -> {
            dataRecord.demoteFromKey(event.getOldValue());
            dataRecord.promoteToKey(event.getValue());
        });
    }

    private void onMappingSelection(final SingleSelectionEvent<AttributeMapping> event) {
        this.attributeMapping = event.getValue();
        this.compareButton.setEnabled(true);
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

            new MNotification("Comparison of data", "Comparison complete with " + differences.size() + " results.", Notification.Type.HUMANIZED_MESSAGE)
                    .withDelayMsec(60000)
                    .withPosition(Position.BOTTOM_CENTER)
                    .show(Page.getCurrent());
        } catch (Exception e) {

            new MNotification("Comparison of data", e.getMessage(), Notification.Type.ERROR_MESSAGE)
                    .withDelayMsec(60000)
                    .withPosition(Position.BOTTOM_CENTER)
                    .show(Page.getCurrent());
        }
    }
}
