package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.services.Unit;
import com.tlvlp.iot.server.portal.services.UnitListService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "units", layout = MainView.class)
public class UnitList extends VerticalLayout {

    public UnitList(UnitListService unitListService) {

        var unitsGrid = new Grid<Unit>();
        unitsGrid.addColumn(Unit::getProject).setHeader("Project").setSortable(true);
        unitsGrid.addColumn(Unit::getName).setHeader("Unit").setSortable(true);
        unitsGrid.addColumn(Unit::isActive).setHeader("Active").setSortable(true);
        unitsGrid.addColumn(new LocalDateTimeRenderer<>((Unit::getLastSeen))).setHeader("Last Seen").setSortable(true);

        unitsGrid.setItemDetailsRenderer(
                new ComponentRenderer<>(
                        selectedUnit -> new Button("Details",
                                e -> UI.getCurrent().navigate(UnitDetails.class))));

        unitsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        unitsGrid.setItems(unitListService.getUnitList());

        add(unitsGrid);
    }


}
