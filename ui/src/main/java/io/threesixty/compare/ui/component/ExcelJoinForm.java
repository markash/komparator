package io.threesixty.compare.ui.component;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import io.threesixty.compare.Attribute;
import io.threesixty.compare.DataRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Excel Join Form allows for the user to choose the key from the source and target
 * data sets that will be used to join them.
 */
class ExcelJoinForm extends FormLayout {

    private final DataRecordProvider sourceDataProvider;
    private final DataRecordProvider targetDataProvider;
    private final List<String> sourceAttributesDataSource = new ArrayList<>();
    private final List<String> targetAttributesDataSource = new ArrayList<>();
    private final ListDataProvider<String> sourceProvider = new ListDataProvider<>(sourceAttributesDataSource);
    private final ListDataProvider<String> targetProvider = new ListDataProvider<>(targetAttributesDataSource);
    private final ComboBox<String> sourceAttributes;
    private final ComboBox<String> targetAttributes;

    ExcelJoinForm(
            final DataRecordProvider sourceDataProvider,
            final DataRecordProvider targetDataProvider,
            final HasValue.ValueChangeListener<String> sourceChangeListener,
            final HasValue.ValueChangeListener<String> targetChangeListener) {

        this.sourceDataProvider = sourceDataProvider;
        this.targetDataProvider = targetDataProvider;

        this.sourceAttributes = new ComboBox<>("Source Key");
        this.sourceAttributes.setDataProvider(this.sourceProvider);
        this.sourceAttributes.addFocusListener(this::onAttributesFocused);
        if (sourceChangeListener != null) {
            sourceAttributes.addValueChangeListener(sourceChangeListener);
        }

        this.targetAttributes = new ComboBox<>("Target Key");
        this.targetAttributes.setDataProvider(this.targetProvider);
        this.targetAttributes.addFocusListener(this::onAttributesFocused);
        if (targetChangeListener != null) {
            targetAttributes.addValueChangeListener(targetChangeListener);
        }

        this.addComponent(sourceAttributes);
        this.addComponent(targetAttributes);

    }

    private void onAttributesFocused(final FieldEvents.FocusEvent event) {

        DataRecordType dataRecordType = event.getComponent().equals(sourceAttributes) ? DataRecordType.SOURCE : DataRecordType.TARGET;
        DataRecordProvider dataRecordProvider = dataRecordType == DataRecordType.SOURCE ? this.sourceDataProvider : this.targetDataProvider;

        List<String> attributeNames = dataRecordProvider
                .getDataRecords()
                .stream().limit(1)
                .map(DataRecord::getAttributes)
                .flatMap(stream -> stream)
                .map(Attribute::getName)
                .collect(Collectors.toList());

        if (dataRecordType == DataRecordType.SOURCE) {
            setSourceAttributes(attributeNames);
        } else {
            setTargetAttributes(attributeNames);
        }
    }

    private void setSourceAttributes(final List<String> attributes) {
        this.sourceAttributesDataSource.clear();
        this.sourceAttributesDataSource.addAll(attributes);
        this.sourceProvider.refreshAll();
    }

    private void setTargetAttributes(final List<String> attributes) {
        this.targetAttributesDataSource.clear();
        this.targetAttributesDataSource.addAll(attributes);
        this.targetProvider.refreshAll();
    }
}
