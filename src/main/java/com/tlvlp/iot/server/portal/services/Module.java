package com.tlvlp.iot.server.portal.services;

public class Module {

    private String unitID;
    private String moduleID;
    private String moduleType;
    private String moduleName;
    private Double value;

    @Override
    public String toString() {
        return "Module{" +
                "unitID='" + unitID + '\'' +
                ", moduleID='" + moduleID + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", value=" + value +
                '}';
    }

    public String getUnitID() {
        return unitID;
    }

    public Module setUnitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public String getModuleID() {
        return moduleID;
    }

    public Module setModuleID(String moduleID) {
        this.moduleID = moduleID;
        return this;
    }

    public String getModuleType() {
        return moduleType;
    }

    public Module setModuleType(String moduleType) {
        this.moduleType = moduleType;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public Module setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public Module setValue(Double value) {
        this.value = value;
        return this;
    }
}
