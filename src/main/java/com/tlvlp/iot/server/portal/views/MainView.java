package com.tlvlp.iot.server.portal.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;


@PageTitle("tlvlp IoT Portal")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PWA(name = "tlvlp IoT Portal", shortName = "tlvlpIotPortal")
public class MainView extends VerticalLayout implements RouterLayout {

    public MainView() {

        var menuBar = new MenuBar();
        menuBar.addItem("Units", c -> UI.getCurrent().navigate(UnitList.class));
        menuBar.addItem("Admin", c -> UI.getCurrent().navigate(Admin.class));
        menuBar.addItem("Reporting", c -> UI.getCurrent().navigate(Reporting.class));
        menuBar.addItem("Logout", c -> UI.getCurrent().navigate(Login.class));

        add(menuBar);
    }


}
