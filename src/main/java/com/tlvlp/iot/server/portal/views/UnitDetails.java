package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.services.*;
import com.tlvlp.iot.server.portal.services.Module;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "unit-details", layout = MainView.class)
@PageTitle("tlvlp IoT Portal - Unit Details")
public class UnitDetails extends VerticalLayout {

    public UnitDetails(UnitService unitService) {
        try {
            var unitID = (String) ComponentUtil.getData(UI.getCurrent(), "unitID");
            Unit unitWithDetails = unitService.getUnitWithSchedulesAndLogs(unitID);
            add(
                    getLabel("Unit Details:"),
                    getUnitDetailsForm(unitWithDetails),
                    getLabel("Generate Report:"),
                    getReportingForm(),
                    getLabel("Modules:"),
                    getModulesGrid(unitWithDetails.getModules(), unitService),
                    getLabel("Scheduled Events:"),
                    getEventsGrid(unitWithDetails.getScheduledEvents()),
                    getLabel("UnitLogs"),
                    getLogsGrid(unitWithDetails.getLogs()));
        } catch (UnitRetrievalException e) {
            //todo
            // error popup
            // redirect back to unitList
        }

    }

    private Label getLabel(String text) {
        return new Label(text);
    }

    private FormLayout getUnitDetailsForm(Unit unit) {
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

        var binder = new Binder<Unit>();
        binder.forField(unitIDField).bind(Unit::getUnitID, null);
        binder.forField(projectField).bind(Unit::getProject, null);
        binder.forField(nameField).bind(Unit::getName, null);
        binder.forField(activeField).bind(u -> u.getActive() ? "Yes" : "No", null);
        binder.forField(lastSeenField).bind(u ->
                DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm").format(u.getLastSeen()), null);
        binder.setReadOnly(true);
        binder.readBean(unit);

        return form;
    }

    private FormLayout getReportingForm() {
        var form = new FormLayout();
        //TODO
        // use standalone reporting form (create reusable separate class)
        // hide & auto populate evident details
        // save form details to ComponentUtil
        // route to reporting

        return form;
    }

    private Grid<Module> getModulesGrid(List<Module> modules, UnitService unitService) {
        var grid = new Grid<Module>();
        grid.addColumn(Module::getModuleType).setHeader("Type").setAutoWidth(true);
        grid.addColumn(Module::getModuleName).setHeader("Name").setFlexGrow(1);
        grid.addColumn(Module::getValue).setHeader("Value").setFlexGrow(1);
        grid.addComponentColumn(module -> {
            if(module.getModuleType().equals("relay")) {
                return new Button("Switch", event -> changeRelayState(module, unitService));
            }
            return null;
        }).setFlexGrow(10);
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(modules); //TODO setitems
        return grid;
    }

    private void changeRelayState(Module module, UnitService unitService) {
        try {
            unitService.changeRelayStateFor(module);
        } catch (ModuleStateChangeException e) {
            //todo display error popup
        }
    }

    private Grid<Event> getEventsGrid(List<Event> events) {
        var grid = new Grid<Event>();
        //todo grid columns
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(events);
        return grid;
    }

    private Grid<Log> getLogsGrid(List<Log> logs) {
        var grid = new Grid<Log>();
        //todo grid columns
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(logs);
        return grid;
    }
}


