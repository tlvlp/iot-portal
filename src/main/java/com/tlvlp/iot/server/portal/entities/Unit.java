package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Unit {

    private String unitID;
    private String name;
    private String project;
    private Boolean active;
    private String controlTopic;
    private LocalDateTime lastSeen;
    private List<Module> modules;
    private List<String> scheduledEventIDs;
    private List<Event> scheduledEvents;
    private List<UnitLog> logs;

    @Override
    public String toString() {
        return "Unit{" +
                "unitID='" + unitID + '\'' +
                ", name='" + name + '\'' +
                ", project='" + project + '\'' +
                ", active=" + active +
                ", controlTopic='" + controlTopic + '\'' +
                ", lastSeen=" + lastSeen +
                ", modules=" + modules +
                ", scheduledEventIDs=" + scheduledEventIDs +
                ", scheduledEvents=" + scheduledEvents +
                ", logs=" + logs +
                '}';
    }

    public String getUnitID() {
        return unitID;
    }

    public Unit setUnitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Unit setName(String name) {
        this.name = name;
        return this;
    }

    public String getProject() {
        return project;
    }

    public Unit setProject(String project) {
        this.project = project;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Unit setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getControlTopic() {
        return controlTopic;
    }

    public Unit setControlTopic(String controlTopic) {
        this.controlTopic = controlTopic;
        return this;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public Unit setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
        return this;
    }

    public List<Module> getModules() {
        return modules;
    }

    public Unit setModules(List<Module> modules) {
        this.modules = modules;
        return this;
    }

    public List<String> getScheduledEventIDs() {
        return scheduledEventIDs;
    }

    public Unit setScheduledEventIDs(List<String> scheduledEventIDs) {
        this.scheduledEventIDs = scheduledEventIDs;
        return this;
    }

    public List<Event> getScheduledEvents() {
        return scheduledEvents;
    }

    public Unit setScheduledEvents(List<Event> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
        return this;
    }

    public List<UnitLog> getLogs() {
        return logs;
    }

    public Unit setLogs(List<UnitLog> logs) {
        this.logs = logs;
        return this;
    }
}