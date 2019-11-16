package com.tlvlp.iot.server.portal.entities;

import java.util.List;

public class UnitComposition {

    private UnitBasic unit;
    private List<UnitLog> logs;
    private List<Event> events;
    private List<Module> modules;

    @Override
    public String toString() {
        return "UnitComposition{" +
                "unit=" + unit +
                ", logs=" + logs +
                ", events=" + events +
                ", modules=" + modules +
                '}';
    }

    public UnitBasic getUnit() {
        return unit;
    }

    public UnitComposition setUnit(UnitBasic unit) {
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

    public List<Module> getModules() {
        return modules;
    }

    public UnitComposition setModules(List<Module> modules) {
        this.modules = modules;
        return this;
    }
}
