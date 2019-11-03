package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.views.Admin;
import com.tlvlp.iot.server.portal.views.LoginView;
import com.tlvlp.iot.server.portal.views.Reporting;
import com.tlvlp.iot.server.portal.views.UnitList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.access.annotation.Secured;

@Secured("ROLE_USER")
public class Menu extends VerticalLayout implements RouterLayout {

    public Menu() {

        var menuBar = new MenuBar();
        menuBar.addItem("Units", c -> UI.getCurrent().navigate(UnitList.class));
        menuBar.addItem("Reporting", c -> UI.getCurrent().navigate(Reporting.class));
        menuBar.addItem("Admin", c -> UI.getCurrent().navigate(Admin.class));
        menuBar.addItem("Logout", c -> UI.getCurrent().navigate(LoginView.class));

        add(menuBar);
    }


}
