package io.threesixty.kt.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import io.threesixty.kt.ui.view.DashboardView;
import io.threesixty.kt.ui.view.ErrorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

@Theme("sidebar")
@SpringUI
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private EventBus.SessionEventBus eventBus;
    @Autowired
    private DashboardView dashboardView;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("Katie");
		configureNavigator();
        showMainScreen();
    }

    private void configureNavigator() {
        final Navigator navigator = new Navigator(this, this.dashboardView.getViewContainer());
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);
    }

    private void showMainScreen() {
        setContent(dashboardView);
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