package com.tlvlp.iot.server.portal.views;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeField extends CustomField<LocalDateTime> {

    private final DatePicker datePicker = new DatePicker();
    private final TimePicker timePicker = new TimePicker();

    public DateTimeField() {

        datePicker.setWidth("8em");
        timePicker.setWidth("8em");

        add(datePicker, timePicker);
    }

    @Override
    protected LocalDateTime generateModelValue() {
        final LocalDate date = datePicker.getValue();
        final LocalTime time = timePicker.getValue();
        return date != null && time != null ?
                LocalDateTime.of(date, time) :
                null;
    }

    @Override
    protected void setPresentationValue(
            LocalDateTime newPresentationValue) {
        datePicker.setValue(newPresentationValue != null ?
                newPresentationValue.toLocalDate() :
                null);
        timePicker.setValue(newPresentationValue != null ?
                newPresentationValue.toLocalTime() :
                null);

    }
}
