package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;
import java.util.Set;

public class ReportingQuery {

    private String unitID;
    private String moduleID;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private Set<String> requestedScopes;

    @Override
    public String toString() {
        return "ReportingQuery{" +
                "unitID='" + unitID + '\'' +
                ", moduleID='" + moduleID + '\'' +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", requestedScopes=" + requestedScopes +
                '}';
    }

    public String getUnitID() {
        return unitID;
    }

    public ReportingQuery setUnitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public String getModuleID() {
        return moduleID;
    }

    public ReportingQuery setModuleID(String moduleID) {
        this.moduleID = moduleID;
        return this;
    }

    public LocalDateTime getTimeFrom() {
        return timeFrom;
    }

    public ReportingQuery setTimeFrom(LocalDateTime timeFrom) {
        this.timeFrom = timeFrom;
        return this;
    }

    public LocalDateTime getTimeTo() {
        return timeTo;
    }

    public ReportingQuery setTimeTo(LocalDateTime timeTo) {
        this.timeTo = timeTo;
        return this;
    }

    public Set<String> getRequestedScopes() {
        return requestedScopes;
    }

    public ReportingQuery setRequestedScopes(Set<String> requestedScopes) {
        this.requestedScopes = requestedScopes;
        return this;
    }
}
