package com.tlvlp.iot.server.portal.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "tlvlp IoT Portal", shortName = "tlvlpIotPortal")
public class MainView extends VerticalLayout implements RouterLayout {

    public MainView() {

        var menuBar = new MenuBar();
        menuBar.addItem("Units", c -> UI.getCurrent().navigate(UnitList.class));
        menuBar.addItem("Admin", c -> UI.getCurrent().navigate(Admin.class));
        menuBar.addItem("Logout", c -> UI.getCurrent().navigate(Login.class));

        add(menuBar);
    }

}
