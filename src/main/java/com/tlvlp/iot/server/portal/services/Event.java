package com.tlvlp.iot.server.portal.services;

import java.time.LocalDateTime;
import java.util.Map;

public class Event {

    private String eventID;
    private String cronSchedule;
    private String targetURL;
    private EventType eventType;
    private String info;
    private LocalDateTime lastUpdated;
    private Map payload;

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", cronSchedule='" + cronSchedule + '\'' +
                ", targetURL='" + targetURL + '\'' +
                ", type=" + eventType +
                ", info='" + info + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", payload=" + payload +
                '}';
    }

    public String getEventID() {
        return eventID;
    }

    public Event setEventID(String eventID) {
        this.eventID = eventID;
        return this;
    }

    public String getCronSchedule() {
        return cronSchedule;
    }

    public Event setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
        return this;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public Event setTargetURL(String targetURL) {
        this.targetURL = targetURL;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Event setInfo(String info) {
        this.info = info;
        return this;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Event setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public Map getPayload() {
        return payload;
    }

    public Event setPayload(Map payload) {
        this.payload = payload;
        return this;
    }
}
