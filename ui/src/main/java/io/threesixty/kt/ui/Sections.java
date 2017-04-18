package io.threesixty.kt.ui;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@SideBarSections({
    @SideBarSection(id = Sections.DEFAULT, caption = ""),
	@SideBarSection(id = Sections.EXECUTION, caption = "Execution"),
	@SideBarSection(id = Sections.CONFIGURATION, caption = "Configuration"),
    @SideBarSection(id = Sections.REPORTING, caption = "Reporting"),
    @SideBarSection(id = Sections.VAADIN_FONT_ICONS, caption = "Vaadin Font Icons", ui = MainUI.class)
})
@Component
public class Sections {
	public static final String DEFAULT = "default";
	public static final String EXECUTION = "execution";
	public static final String CONFIGURATION = "configuration";
	public static final String REPORTING = "reporting";
	public static final String VAADIN_FONT_ICONS = "vaadinFontIcons";
}
