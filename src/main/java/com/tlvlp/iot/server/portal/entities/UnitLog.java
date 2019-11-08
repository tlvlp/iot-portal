package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;

public class UnitLog {

    private LocalDateTime arrived;
    private String logEntry;

    @Override
    public String toString() {
        return "Log{" +
                "arrived=" + arrived +
                ", logEntry='" + logEntry + '\'' +
                '}';
    }

    public LocalDateTime getArrived() {
        return arrived;
    }

    public UnitLog setArrived(LocalDateTime arrived) {
        this.arrived = arrived;
        return this;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public UnitLog setLogEntry(String logEntry) {
        this.logEntry = logEntry;
        return this;
    }
}
