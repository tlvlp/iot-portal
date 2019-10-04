package com.tlvlp.iot.server.portal.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin", layout = MainView.class)
@PageTitle("tlvlp IoT Portal - Admin")
public class Admin extends VerticalLayout {

    public Admin() {
        add(new String("Hello from admin"));
    }

}
