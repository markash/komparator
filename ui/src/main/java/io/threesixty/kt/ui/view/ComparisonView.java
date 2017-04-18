package io.threesixty.kt.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import io.threesixty.kt.ui.Sections;
import io.threesixty.kt.ui.component.DataDifferencePanel;
import io.threesixty.kt.ui.component.DataRecordPanel;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.ui.view.AbstractDashboardView;

@SuppressWarnings("serial")
@SpringView(name = ComparisonView.VIEW_NAME)
@SideBarItem(sectionId = Sections.EXECUTION, caption = ComparisonView.VIEW_CAPTION, order = ComparisonView.VIEW_ORDER)
@VaadinFontIcon(VaadinIcons.COMPILE)
@ViewScope
public class ComparisonView extends AbstractDashboardView {
    static final String VIEW_NAME = "Comparison";
    static final String VIEW_CAPTION = "Comparison";
    static final int VIEW_ORDER = 2;

    private DataRecordPanel sourceRecordPanel = new DataRecordPanel("Source Data");
    private DataRecordPanel targetRecordPanel = new DataRecordPanel("Target Data");
    private DataDifferencePanel differencePanel = new DataDifferencePanel("Differences", sourceRecordPanel, targetRecordPanel);

    public ComparisonView() {
    	super(VIEW_CAPTION);
	}

	@Override
	protected Component buildContent() {
        return new MVerticalLayout(sourceRecordPanel, targetRecordPanel, differencePanel)
                        .withFullWidth().withSpacing(false).withMargin(false);
	}


	@Override
    public void enter(ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}