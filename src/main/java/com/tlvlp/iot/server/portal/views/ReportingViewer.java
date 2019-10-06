package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.Average;
import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import com.tlvlp.iot.server.portal.services.ReportingException;
import com.tlvlp.iot.server.portal.services.ReportingService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ReportingViewer extends VerticalLayout {

    private ReportingService reportingService;
    private Grid<Average> grid;

    public ReportingViewer(ReportingService reportingService) {
        this.reportingService = reportingService;
        grid = new Grid<>();
        grid.addColumn(Average::getScope).setHeader("Scope")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Average::getDate).setHeader("Date")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(Average::getValue).setHeader("Value")
                .setSortable(true)
                .setFlexGrow(10);
        add(grid);
    }

    public void initializeData(ReportingQuery query) {
        try {
            grid.setItems(reportingService.getReports(query));
        } catch (ReportingException | RuntimeException e) {
            showNotification(e.getMessage());
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
