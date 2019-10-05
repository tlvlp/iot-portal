package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.Module;
import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import com.tlvlp.iot.server.portal.entities.Unit;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class ReportingForm extends VerticalLayout {

    private Binder<ReportingQuery> binder;
    private ReportingQuery query;
    private List<Unit> selectableUnits;
    private List<String> selectableProjects;

    public ReportingForm(List<Unit> units) {
        this.binder = new Binder<>();

        query = new ReportingQuery();

        selectableProjects = units
                .stream()
                .map(Unit::getProject)
                .distinct()
                .collect(Collectors.toList());
        selectableUnits = units;

        var moduleSelector = new ComboBox<Module>();
        moduleSelector.setWidth("20em");
        moduleSelector.setItemLabelGenerator(Module::getModuleID);
        moduleSelector.setRequired(true);

        var unitSelector = new ComboBox<Unit>();
        unitSelector.setWidth("20em");
        unitSelector.setItemLabelGenerator(Unit::getUnitID);
        unitSelector.setRequired(true);
        unitSelector.setItems(selectableUnits);
        unitSelector.addValueChangeListener(event -> {
            var filteredModules = unitSelector.getValue().getModules();
            moduleSelector.setItems(filteredModules);
            moduleSelector.setValue(filteredModules.get(0));
        });

        var projectSelector = new ComboBox<String>();
        projectSelector.setWidth("20em");
        projectSelector.setRequired(true);
        projectSelector.setItems(selectableProjects);
        projectSelector.setValue(selectableProjects.get(0));
        projectSelector.addValueChangeListener(event -> {
            var filteredUnits = selectableUnits.stream()
                    .filter(unit -> unit.getProject().equals(projectSelector.getValue()))
                    .collect(Collectors.toList());
            unitSelector.setItems(filteredUnits);
            unitSelector.setValue(filteredUnits.get(0));
        });

        var timeFromField = new DateTimeField();
        timeFromField.setValue(LocalDateTime.now().minusDays(7).truncatedTo(DAYS));

        var timeToField = new DateTimeField();
        timeToField.setValue(LocalDateTime.now().truncatedTo(DAYS));

        var requestedScopes = new CheckboxGroup<String>();
        requestedScopes.setItems("MINUTES", "HOURS", "DAYS", "MONTHS", "YEARS");
        requestedScopes.setValue(Set.of("DAYS"));
        requestedScopes.setRequired(true);

        var moduleDetailsForm = new FormLayout();
        moduleDetailsForm.addFormItem(projectSelector, "Project");
        moduleDetailsForm.addFormItem(unitSelector, "UnitID");
        moduleDetailsForm.addFormItem(moduleSelector, "Module ID");

        var intervalForm = new FormLayout();
        intervalForm.addFormItem(timeFromField, "Start time");
        intervalForm.addFormItem(timeToField, "End time");

        add(
                new Label("Module details"),
                moduleDetailsForm,
                new Label("Interval"),
                intervalForm,
                new Label("Calculate averages for:"),
                requestedScopes
        );

        binder.forField(unitSelector).bind(ReportingQuery::getUnit, ReportingQuery::setUnit);
        binder.forField(moduleSelector).bind(ReportingQuery::getModule, ReportingQuery::setModule);
        binder.forField(timeFromField).bind(ReportingQuery::getTimeFrom, ReportingQuery::setTimeFrom);
        binder.forField(timeToField).bind(ReportingQuery::getTimeTo, ReportingQuery::setTimeTo);
        binder.forField(requestedScopes).bind(ReportingQuery::getRequestedScopes, ReportingQuery::setRequestedScopes);
    }


    public void setQuery(ReportingQuery query) {
        this.query = query;
        binder.readBean(query);
    }

    public ReportingQuery getQuery() {
        binder.writeBeanIfValid(query);
        return query;
    }
}
