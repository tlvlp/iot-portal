package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.Module;
import com.tlvlp.iot.server.portal.entities.*;
import com.tlvlp.iot.server.portal.services.UnitRetrievalException;
import com.tlvlp.iot.server.portal.services.UnitService;
import com.tlvlp.iot.server.portal.services.UnitUpdateException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

@Route(value = "unit-details", layout = MainLayout.class)
@PageTitle("tlvlp IoT portal - Unit Details")
@Secured("ROLE_USER")
public class UnitDetails extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(UnitDetails.class);
    private UnitService unitService;
    private String unitID;

    public UnitDetails(UnitService unitService) {
        this.unitService = unitService;
        initializePageData();
    }

    public void initializePageData() {
        try {
            var unitID = (String) ComponentUtil.getData(UI.getCurrent(), "unitID");
            if (unitID == null) {
                throw new UnitRetrievalException("Unit ID was not passed between views!");
            }
            var unitComposition = unitService.getUnitWithSchedulesAndLogs(unitID);
            if (unitComposition == null) {
                throw new UnitRetrievalException("Unit not found at the server");
            }
            this.unitID = unitComposition.getUnit().getUnitID();
            removeAll();
            add(
                    getLabel("Unit Details:"),
                    getUnitDetailsForm(unitComposition),
                    getSeparator(),
                    getLabel("Modules:"),
                    getModulesGrid(unitComposition.getModules()),
                    getSeparator(),
                    getLabel("Scheduled Events:"),
                    getEventsGrid(unitComposition.getEvents()),
                    getSeparator(),
                    getLabel("Generate Report:"),
                    getReportingForm(unitComposition.getUnit()),
                    getSeparator(),
                    getLabel("Unit Logs (last 7 days)"),
                    getLogsGrid(unitComposition.getLogs()));
        } catch (Exception e) {
            e.printStackTrace();
            String err = "Unit details cannot be retrieved: ";
            log.error(err);
            showNotification(err);
            UI.getCurrent().navigate(UnitList.class);
        }
    }

    private Label getLabel(String text) {
        return new Label(text);
    }

    private HorizontalLayout getSeparator() {
        var separator = new HorizontalLayout();
        separator.setHeight("2em");
        return separator;
    }

    private FormLayout getUnitDetailsForm(UnitComposition unit) {
        var unitIDField = new TextField();
        var projectField = new TextField();
        var nameField = new TextField();
        var activeField = new TextField();
        var lastSeenField = new TextField();

        var form = new FormLayout();
        form.addFormItem(unitIDField, "ID");
        form.addFormItem(projectField, "Project");
        form.addFormItem(nameField, "Name");
        form.addFormItem(activeField, "Active");
        form.addFormItem(lastSeenField, "Last seen");
        form.addFormItem(new Button("Refresh Data", event -> initializePageData()), "");

        var binder = new Binder<UnitComposition>();
        binder.forField(unitIDField).bind(u -> u.getUnit().getUnitID(), null);
        binder.forField(projectField).bind(u -> u.getUnit().getProject(), null);
        binder.forField(nameField).bind(u -> u.getUnit().getName(), null);
        binder.forField(activeField).bind(u -> u.getUnit().getActive() ? "Yes" : "No", null);
        binder.forField(lastSeenField).bind(u ->
                DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(u.getUnit().getLastSeen()), null);
        binder.setReadOnly(true);
        binder.readBean(unit);

        return form;
    }

    private VerticalLayout getReportingForm(UnitBasic unit) {
        if (unit.getModules().isEmpty()) {
            return new VerticalLayout(new Label("Cannot generate reports for Unit: No modules found"));
        }
        var form = new ReportingForm(List.of(unit));
        var unitQuery = new ReportingQuery()
                .setUnit(unit)
                .setModule(unit.getModules().get(0))
                .setRequestedScopes(Set.of("DAYS"))
                .setTimeFrom(LocalDateTime.now().minusDays(7).truncatedTo(DAYS))
                .setTimeTo(LocalDateTime.now().truncatedTo(DAYS));
        form.setQuery(unitQuery);

        var reportingButton = new Button("Generate Reports", event -> {
            ComponentUtil.setData(UI.getCurrent(), ReportingQuery.class, form.getQuery());
            UI.getCurrent().navigate(Reporting.class);
        });

        return new VerticalLayout(form, reportingButton);
    }

    private Grid<Module> getModulesGrid(List<Module> modules) {
        var grid = new Grid<Module>();
        grid.addColumn(Module::getModuleType).setHeader("Type").setAutoWidth(true);
        grid.addColumn(Module::getModuleName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Module::getValue).setHeader("Value").setFlexGrow(1);
        grid.addComponentColumn(this::getModuleSpecificComponent).setFlexGrow(10);
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(modules);
        return grid;
    }

    private Component getModuleSpecificComponent(Module module) {
        if (module.getModuleType().equals("relay")) {
            return new Button("Switch", event -> changeRelayState(module));
        }
        return new Label("");
    }

    private void changeRelayState(Module module) {
        try {
            unitService.changeRelayState(module);
        } catch (UnitUpdateException e) {
            showNotification("Relay state cannot be changed: " + e.getMessage());
        }
    }

    private Grid<Event> getEventsGrid(List<Event> events) {
        var grid = new Grid<Event>();
        grid.addColumn(event -> event.getEventType()).setHeader("Type").setAutoWidth(true);
        grid.addColumn(Event::getCronSchedule).setHeader("CRON").setFlexGrow(1);
        grid.addColumn(Event::getInfo).setHeader("Info").setAutoWidth(true);
        grid.addColumn(e -> DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")
                .format(e.getLastUpdated())).setHeader("Last Updated").setFlexGrow(10);
        grid.addComponentColumn(event -> new Button("Edit", e -> popUpEventEditor(event)));
        grid.addComponentColumn(event -> new Button("Delete", e -> deleteEvent(event)));
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(events);
        return grid;
    }

    private void popUpEventEditor(Event selectedEvent) {
        new UnitDetailsEventEditor(selectedEvent, unitID, unitService, this).open();
    }

    private void deleteEvent(Event selectedEvent) {
        try {
            unitService.deleteScheduledEventFromUnit(selectedEvent, unitID);
            initializePageData();
        } catch (UnitUpdateException e) {
            showNotification("Cannot delete event: " + e.getMessage());
        }
    }


    private Grid<UnitLog> getLogsGrid(List<UnitLog> logs) {
        var grid = new Grid<UnitLog>();
        grid.addColumn(log -> DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(log.getArrived()))
                .setHeader("Arrived").setAutoWidth(true);
        grid.addColumn(UnitLog::getLogEntry).setHeader("Entry").setFlexGrow(10);
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(logs);
        return grid;
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

}


