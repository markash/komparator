package io.threesixty.kt.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import io.threesixty.kt.ui.Sections;
import io.threesixty.ui.view.AbstractDashboardView;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SuppressWarnings("serial")
@SpringView(name = HomeView.VIEW_NAME)
@SideBarItem(sectionId = Sections.DEFAULT, caption = HomeView.VIEW_CAPTION, order = HomeView.VIEW_ORDER)
@VaadinFontIcon(VaadinIcons.HOME)
@ViewScope
public class HomeView extends AbstractDashboardView {
    static final String VIEW_NAME = "";
    static final String VIEW_CAPTION = "Home";
    static final int VIEW_ORDER = 1;

    public HomeView() {
    	super(VIEW_CAPTION);
	}

	@Override
	protected Component buildContent() {
        return new Label("Home of the comparison");
	}


	@Override
    public void enter(ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}