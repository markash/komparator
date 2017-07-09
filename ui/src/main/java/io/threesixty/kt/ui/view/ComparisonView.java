package io.threesixty.kt.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import io.threesixty.kt.ui.Sections;
import io.threesixty.kt.ui.component.DataComparePanel;
import io.threesixty.kt.ui.component.DataDifferencePanel;
import io.threesixty.kt.ui.component.DataRecordPanel;
import io.threesixty.kt.ui.service.PersistenceService;
import io.threesixty.ui.view.AbstractDashboardView;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SuppressWarnings("serial")
@SpringView(name = ComparisonView.VIEW_NAME)
@SideBarItem(sectionId = Sections.EXECUTION, caption = ComparisonView.VIEW_CAPTION, order = ComparisonView.VIEW_ORDER)
@VaadinFontIcon(VaadinIcons.COMPILE)
@ViewScope
public class ComparisonView extends AbstractDashboardView {
    static final String VIEW_NAME = "Comparison";
    static final String VIEW_CAPTION = "Comparison";
    static final int VIEW_ORDER = 2;

    private DataComparePanel compareRecordPanel;
    private DataRecordPanel sourceRecordPanel;
    private DataRecordPanel targetRecordPanel;
    private DataDifferencePanel differencePanel;

    private PersistenceService persistenceService;

    public ComparisonView(final PersistenceService persistenceService) {
    	super(VIEW_CAPTION);

        this.sourceRecordPanel = new DataRecordPanel("Source Data");
        this.targetRecordPanel = new DataRecordPanel("Target Data");
        this.differencePanel = new DataDifferencePanel("Differences");
        this.compareRecordPanel = new DataComparePanel("Configure and execute a comparison", sourceRecordPanel, targetRecordPanel, differencePanel, persistenceService);

	}

	@Override
	protected Component buildContent() {

        TabSheet tabsheet = new TabSheet();
        tabsheet.addTab(compareRecordPanel, "Setup", VaadinIcons.CODE);
        tabsheet.addTab(sourceRecordPanel, "Source", VaadinIcons.CLOUD_DOWNLOAD_O);
        tabsheet.addTab(targetRecordPanel, "Target", VaadinIcons.CLOUD_UPLOAD_O);
        tabsheet.addTab(differencePanel, "Differences", VaadinIcons.COMPILE);
        tabsheet.setWidth("100%");
        tabsheet.setHeight("100%");
        tabsheet.setStyleName("framed");
        return tabsheet;
	}


	@Override
    public void enter(ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}