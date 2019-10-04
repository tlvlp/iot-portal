package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.services.*;
import com.tlvlp.iot.server.portal.services.Module;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Route(value = "units", layout = MainView.class)
@PageTitle("tlvlp IoT Portal - Unit List")
public class UnitList extends VerticalLayout {



    public UnitList(UnitService unitService) {
        try {
            var grid = new Grid<Unit>();
            grid.addColumn(Unit::getProject).setHeader("Project")
                    .setFlexGrow(1)
                    .setSortable(true);
            grid.addColumn(Unit::getName).setHeader("Unit")
                    .setFlexGrow(1)
                    .setSortable(true);
            grid.addColumn(unit -> unit.getModules().size()).setHeader("Modules")
                    .setFlexGrow(1)
                    .setSortable(true);
            grid.addColumn(unit -> unit.getScheduledEventIDs().size()).setHeader("Events")
                    .setFlexGrow(1)
                    .setSortable(true);
            grid.addColumn(u -> u.getActive() ? "Yes" : "No").setHeader("Active")
                    .setFlexGrow(1)
                    .setSortable(true);
            grid.addColumn(u -> DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm").format(u.getLastSeen())).setHeader("Last Seen")
                    .setFlexGrow(1)
                    .setAutoWidth(true)
                    .setSortable(true);

            grid.setWidthFull();
            grid.setHeightByRows(true);

            grid.setSelectionMode(Grid.SelectionMode.NONE);

            grid.setMultiSort(true);
            grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUnitPreview));
            grid.setSelectionMode(Grid.SelectionMode.NONE);
            grid.setItems(unitService.getUnitList());

            add(grid);
        } catch (UnitRetrievalException e) {
            //todo
            // error popup
        }
    }

    private HorizontalLayout getUnitPreview(Unit selectedUnit) {
        var detailsButton = new Button("Details", event -> {
            ComponentUtil.setData(UI.getCurrent(), "unitID", selectedUnit.getUnitID());
            UI.getCurrent().navigate(UnitDetails.class);
        });
        detailsButton.setAutofocus(true);
        detailsButton.setWidth("7em");

        var moduleGrid = new Grid<Module>();
        moduleGrid.addColumn(Module::getModuleType).setAutoWidth(true);
        moduleGrid.addColumn(Module::getModuleName).setFlexGrow(1);
        moduleGrid.addColumn(Module::getValue).setFlexGrow(10);
        moduleGrid.setWidthFull();
        moduleGrid.setHeightByRows(true);
        moduleGrid.setItems(new ArrayList<>(selectedUnit.getModules()));

        return new HorizontalLayout(detailsButton, moduleGrid);
    }

}
