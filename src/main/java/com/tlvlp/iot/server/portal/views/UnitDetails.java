package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.services.Event;
import com.tlvlp.iot.server.portal.services.Log;
import com.tlvlp.iot.server.portal.services.Unit;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

@Route(value = "unit-details", layout = MainView.class)
@PageTitle("tlvlp IoT Portal - Unit Details")
public class UnitDetails extends VerticalLayout {

    public UnitDetails() {
        var unit = (Unit) ComponentUtil.getData(UI.getCurrent(), "selectedUnit");
        // Todo get composite unit details Map
        add(
                getLabel("Unit Details:"),
                getUnitDetailsForm(unit),
                getLabel("Modules:"),
                getModulesGrid(unit),  //todo add module list
                getLabel("Scheduled Events:"),
                getEventsGrid(unit),   // todo add evnet list
                getLabel("Generate Report:"),
                getReportingForm());
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
        binder.forField(activeField).bind(u -> u.isActive() ? "Yes" : "No", null);
        binder.forField(lastSeenField).bind(u ->
                DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm").format(u.getLastSeen()), null);
        binder.setReadOnly(true);
        binder.readBean(unit);

        return form;
    }

    private Grid<Module> getModulesGrid(Unit unit) {
        var grid = new Grid<Module>();
//        modulesGrid.addComponentColumn()
//        modulesGrid.

        grid.setItems(); //TODO setitems
        return grid;
    }

    private Grid<Event> getEventsGrid(Unit unit) {
        var grid = new Grid<Event>();


        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setItems(); //TODO setitems
        return grid;
    }

    private Grid<Log> getLogsGrid(Unit unit) {
        var grid = new Grid<Log>();
        //TODO load logs to the grid

        return grid;
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
}


