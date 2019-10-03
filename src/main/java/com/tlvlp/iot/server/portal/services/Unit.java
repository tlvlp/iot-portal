package com.tlvlp.iot.server.portal.services;

import java.time.LocalDateTime;
import java.util.Set;

public class Unit {

    private String unitID;
    private String name;
    private String project;
    private Boolean active;
    private String controlTopic;
    private LocalDateTime lastSeen;
    private Set<Module> modules;
    private Set<String> scheduledEvents;


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

    public Boolean isActive() {
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

    public Set<Module> getModules() {
        return modules;
    }

    public Unit setModules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Set<String> getScheduledEvents() {
        return scheduledEvents;
    }

    public Unit setScheduledEvents(Set<String> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
        return this;
    }
}