package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import com.tlvlp.iot.server.portal.services.ReportingException;
import com.tlvlp.iot.server.portal.services.UnitService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "reporting", layout = MainView.class)
@PageTitle("tlvlp IoT Portal - Reporting")
public class Reporting extends VerticalLayout {

    private UnitService unitService;

    public Reporting(UnitService unitService) {
        this.unitService = unitService;
        initializePageData();
    }


    public void initializePageData() {
        try {
            var reportingQuery = Optional.of(ComponentUtil.getData(UI.getCurrent(), ReportingQuery.class))
                    .orElse(new ReportingQuery());
            var reports = Optional.of(unitService.getReports(reportingQuery))
                    .orElseThrow(new ReportingException("Unable to generate reports"));

            //TODO display report

            add(new ReportingForm(), new ReportingViewer());

        } catch (ReportingException | RuntimeException e) {
            showNotification("Report details cannot be retrieved: " + e.getMessage());
            UI.getCurrent().navigate(UnitList.class);
        }
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }
}
