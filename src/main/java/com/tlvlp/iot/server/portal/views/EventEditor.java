package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.services.Event;
import com.tlvlp.iot.server.portal.services.EventType;
import com.tlvlp.iot.server.portal.services.UnitService;
import com.tlvlp.iot.server.portal.services.UnitUpdateException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class EventEditor extends Dialog {

    public EventEditor(Event event, UnitService unitService, UnitDetails unitDetails) {

        setCloseOnOutsideClick(false);
        setCloseOnEsc(true);

        var errorLabel = new Label();
        errorLabel.setVisible(false);

        var eventTypeSelector = new Select<>(EventType.values());
        var infoField = new TextField();
        var cronField = new TextField();

        var form = new FormLayout();
        form.addFormItem(eventTypeSelector, "Type");
        form.addFormItem(infoField, "Info");
        form.addFormItem(cronField, "CRON");

        var binder = new Binder<Event>();
        binder.forField(eventTypeSelector).bind(
                e -> e.getEventType(),
                (e, eventType) -> e.setEventType(eventType));
        binder.forField(infoField).bind(Event::getInfo, Event::setInfo);
        binder.forField(cronField).bind(Event::getCronSchedule, Event::setCronSchedule);
        binder.readBean(event);

        var saveButton = new Button("Save", e -> {
            binder.writeBeanIfValid(event);
            try {
                if (event.getEventType().equals(EventType.UNKNOWN)) {
                    throw new UnitUpdateException("Event type cannot be UNKNOWN!");
                }
                unitService.addScheduledEventToUnit(event);
                unitDetails.initializePageData();
                close();
            } catch (UnitUpdateException err) {
                errorLabel.setText("Cannot save event: " + err.getMessage());
                errorLabel.setVisible(true);
            }
        });

        var cancelButton = new Button("Cancel", e -> {
            close();
        });

        var buttons = new HorizontalLayout(saveButton, cancelButton);

        add(form, buttons, errorLabel);
    }

}
