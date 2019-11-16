package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;
import java.util.List;

public class UnitBasic {

    private String unitID;
    private String name;
    private String project;
    private Boolean active;
    private LocalDateTime lastSeen;
    private List<Module> modules;

    @Override
    public String toString() {
        return "Unit{" +
                "unitID='" + unitID + '\'' +
                ", name='" + name + '\'' +
                ", project='" + project + '\'' +
                ", active=" + active +
                ", lastSeen=" + lastSeen +
                ", modules=" + modules +
                '}';
    }

    public String getUnitID() {
        return unitID;
    }

    public UnitBasic setUnitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public String getName() {
        return name;
    }

    public UnitBasic setName(String name) {
        this.name = name;
        return this;
    }

    public String getProject() {
        return project;
    }

    public UnitBasic setProject(String project) {
        this.project = project;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public UnitBasic setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public UnitBasic setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
        return this;
    }

    public List<Module> getModules() {
        return modules;
    }

    public UnitBasic setModules(List<Module> modules) {
        this.modules = modules;
        return this;
    }
}