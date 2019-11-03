package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import com.tlvlp.iot.server.portal.entities.Unit;
import com.tlvlp.iot.server.portal.services.ReportingService;
import com.tlvlp.iot.server.portal.services.UnitRetrievalException;
import com.tlvlp.iot.server.portal.services.UnitService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.NoSuchElementException;

@Route(value = "reporting", layout = MainLayout.class)
@PageTitle("tlvlp IoT Portal - Reporting")
@Secured("ROLE_USER")
public class Reporting extends VerticalLayout {

    private ReportingForm form;
    private ReportingViewer viewer;
    private UnitService unitService;
    private ReportingService reportingService;


    public Reporting(ReportingService reportingService, UnitService unitService) {
        this.form = null;
        this.viewer = null;
        this.unitService = unitService;
        this.reportingService = reportingService;
        initializeData();
    }

    private void initializeData() {
        try {
            var unitList = getUnitListFromBackend();
            form = new ReportingForm(unitList);
            viewer = new ReportingViewer(reportingService);
            loadQueryFromEnv();
            var generateReportButton = new Button("Generate Reports", event -> generateReport());
            var refreshDataButton = new Button("Refresh Data", event -> refreshData());
            removeAll();
            add(form, new HorizontalLayout(generateReportButton, refreshDataButton), viewer);
        } catch (NoSuchElementException | UnitRetrievalException e) {
            showNotification("Unit list cannot be retrieved" + e.getMessage());
        }
    }

    private void refreshData() {
        saveQueryToEnv();
        initializeData();
    }

    private void loadQueryFromEnv() {
        var query = ComponentUtil.getData(UI.getCurrent(), ReportingQuery.class);
        if (query != null) {
            form.setQuery(query);
            viewer.initializeData(query);
        }
    }

    private void saveQueryToEnv() {
        ComponentUtil.setData(UI.getCurrent(), ReportingQuery.class, form.getQuery());
    }

    private List<Unit> getUnitListFromBackend() throws UnitRetrievalException {
        return unitService.getUnitListWithModules();
    }

    private void generateReport() {
        saveQueryToEnv();
        viewer.initializeData(form.getQuery());
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

}
