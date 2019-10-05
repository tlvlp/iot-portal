package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import com.tlvlp.iot.server.portal.services.ReportingException;
import com.tlvlp.iot.server.portal.services.ReportingService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ReportingViewer extends VerticalLayout {

    private ReportingService reportingService;
    private Grid<Map<String, Double>> grid;

    public ReportingViewer(ReportingService reportingService) {
        this.reportingService = reportingService;
        grid = new Grid<>();


        //todo grid structure

    }

    public void initializeData(ReportingQuery query) {
        try {
            //todo load if present leave if not
            Optional<Map<ChronoUnit, TreeMap<String, Double>>> reports =
                    Optional.of(reportingService.getReports(query));

            //todo init grid with
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
