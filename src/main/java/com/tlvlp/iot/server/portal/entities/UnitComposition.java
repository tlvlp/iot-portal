package com.tlvlp.iot.server.portal.entities;

import java.util.List;

public class UnitComposition {

    private Unit unit;
    private List<UnitLog> logs;
    private List<Event> events;

    public Unit getUnit() {
        return unit;
    }

    public UnitComposition setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public List<UnitLog> getLogs() {
        return logs;
    }

    public UnitComposition setLogs(List<UnitLog> logs) {
        this.logs = logs;
        return this;
    }

    public List<Event> getEvents() {
        return events;
    }

    public UnitComposition setEvents(List<Event> events) {
        this.events = events;
        return this;
    }
}
