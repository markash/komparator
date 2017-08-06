package io.threesixty.compare.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 * @author Mark P Ashworth
 */
@PrototypeScope
@SpringComponent
public class DashboardView extends HorizontalLayout {
    private final VerticalLayout viewContainer = new VerticalLayout();

    @Autowired
    public DashboardView(final ValoSideBar sideBar) {
        this.setSizeFull();

        viewContainer.setSizeFull();
        viewContainer.setSpacing(true);
        viewContainer.setMargin(false);

        sideBar.setLogo(getLogo());
        this.addComponent(sideBar);
        this.addComponent(viewContainer);
        this.setExpandRatio(viewContainer, 1.0f);
    }

    public VerticalLayout getViewContainer() {
        return this.viewContainer;
    }

    private Component getLogo() {
        Label logo = new Label(VaadinIcons.ROCKET.getHtml(), ContentMode.HTML);
        Label name = new Label("&nbsp;&nbsp;KT <strong>360</strong>", ContentMode.HTML);
        HorizontalLayout logoWrapper = new HorizontalLayout(logo, name);
        logoWrapper.setComponentAlignment(logo, Alignment.TOP_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }
}
