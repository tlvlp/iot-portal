package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;
import java.util.Set;

public class ReportingQuery {

    private Unit unit;
    private Module module;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private Set<String> requestedScopes;

    @Override
    public String toString() {
        return "ReportingQuery{" +
                "unit=" + unit +
                ", module=" + module +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", requestedScopes=" + requestedScopes +
                '}';
    }

    public Unit getUnit() {
        return unit;
    }

    public ReportingQuery setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public Module getModule() {
        return module;
    }

    public ReportingQuery setModule(Module module) {
        this.module = module;
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
