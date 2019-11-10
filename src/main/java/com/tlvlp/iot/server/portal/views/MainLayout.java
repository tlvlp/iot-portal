package com.tlvlp.iot.server.portal.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.security.access.annotation.Secured;

@Secured("ROLE_USER")
public class MainLayout extends VerticalLayout implements RouterLayout {

    public MainLayout() {

        var menuBar = new MenuBar();
        menuBar.addItem("Units", c -> UI.getCurrent().navigate(UnitList.class));
        menuBar.addItem("Reporting", c -> UI.getCurrent().navigate(Reporting.class));
        menuBar.addItem("Admin", c -> UI.getCurrent().navigate(Admin.class));
        menuBar.addItem("Logout", c -> UI.getCurrent().navigate(LoginView.class));

        add(menuBar);
    }


}
