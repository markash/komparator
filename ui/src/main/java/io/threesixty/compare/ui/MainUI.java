package io.threesixty.compare.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Component;
import io.threesixty.compare.ui.view.DashboardView;
import io.threesixty.ui.ApplicationUI;
import io.threesixty.ui.component.logo.Logo;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.sidebar.components.ValoSideBar;

@Theme("kt")
@SpringUI
public class MainUI extends ApplicationUI {
	private static final long serialVersionUID = 1L;

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private EventBus.SessionEventBus eventBus;
    @Autowired
    private DashboardView dashboardView;
    @Autowired
    private ValoSideBar sideBar;
    @Autowired
    private Logo logo;

    @Override
    protected Component getSideBar() {
        sideBar.setLogo(logo);
        return sideBar;
    }

    @Override
    protected ViewProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        super.detach();
    }
}