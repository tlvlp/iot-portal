package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.Module;
import com.tlvlp.iot.server.portal.entities.Unit;
import com.tlvlp.iot.server.portal.services.UnitRetrievalException;
import com.tlvlp.iot.server.portal.services.UnitService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Route(value = "unit-list", layout = Menu.class)
@PageTitle("tlvlp IoT Portal - Unit List")
@Secured("ROLE_USER")
public class UnitList extends VerticalLayout implements AfterNavigationObserver {

    private Grid<Unit> grid;
    private UnitService unitService;


    public UnitList(UnitService unitService) {

        this.unitService = unitService;

        grid = new Grid<>();
        grid.addColumn(Unit::getProject).setHeader("Project")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(Unit::getName).setHeader("Unit")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(u -> u.getActive() ? "Yes" : "No").setHeader("Active")
                .setFlexGrow(1)
                .setSortable(true);
        grid.addColumn(u -> DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(u.getLastSeen())).setHeader("Last Seen")
                .setFlexGrow(10)
                .setAutoWidth(true)
                .setSortable(true);

        grid.setWidthFull();
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setMultiSort(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUnitPreview));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        add(grid);
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
        moduleGrid.addColumn(Module::getModuleName).setAutoWidth(true);
        moduleGrid.addColumn(Module::getValue).setFlexGrow(10);
        moduleGrid.setWidthFull();
        moduleGrid.setHeightByRows(true);
        moduleGrid.setItems(new ArrayList<>(selectedUnit.getModules()));

        return new HorizontalLayout(detailsButton, moduleGrid);
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

    private void refreshGridData() throws UnitRetrievalException {
        grid.setItems(unitService.getUnitListWithModules());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        try {
            refreshGridData();
        } catch (UnitRetrievalException e) {
            showNotification("Unit list cannot be retrieved: " + e.getMessage());
        }
    }
}
