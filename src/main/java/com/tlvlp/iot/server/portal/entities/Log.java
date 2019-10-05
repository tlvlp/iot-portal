package com.tlvlp.iot.server.portal.entities;

import java.time.LocalDateTime;

public class Log {

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

    public Log setArrived(LocalDateTime arrived) {
        this.arrived = arrived;
        return this;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public Log setLogEntry(String logEntry) {
        this.logEntry = logEntry;
        return this;
    }
}
