package com.tlvlp.iot.server.portal.entities;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Average {

    private ChronoUnit scope;
    private String date;
    private Double value;

    @Override
    public String toString() {
        return "Average{" +
                "scope=" + scope +
                ", date='" + date + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Average average = (Average) o;
        return scope == average.scope &&
                Objects.equals(date, average.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, date);
    }

    public ChronoUnit getScope() {
        return scope;
    }

    public Average setScope(ChronoUnit scope) {
        this.scope = scope;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Average setDate(String date) {
        this.date = date;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public Average setValue(Double value) {
        this.value = value;
        return this;
    }
}
