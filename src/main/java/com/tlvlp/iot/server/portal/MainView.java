package com.tlvlp.iot.server.portal;

import com.tlvlp.iot.server.portal.views.UnitList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.access.annotation.Secured;

@Route
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PWA(name = "tlvlp IoT portal", shortName = "tlvlpIotPortal")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        UI.getCurrent().navigate(UnitList.class);
    }
}
