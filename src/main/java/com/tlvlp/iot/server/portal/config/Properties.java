package com.tlvlp.iot.server.portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {

    // The service uses environment variables from the Docker container.

    @Value("${API_GATEWAY_PROTOCOL}")
    private String API_GATEWAY_PROTOCOL;

    @Value("${API_GATEWAY_NAME}")
    private String API_GATEWAY_NAME;

    @Value("${API_GATEWAY_PORT}")
    private String API_GATEWAY_PORT;

    @Value("${API_GATEWAY_API_GET_ALL_UNITS}")
    private String API_GATEWAY_API_GET_ALL_UNITS;

    @Value("${API_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS}")
    private String API_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS;

    @Value("${API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS}")
    private String API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS;

    @Value("${API_GATEWAY_API_UNIT_MODULE_CONTROL}")
    private String API_GATEWAY_API_UNIT_MODULE_CONTROL;


    @Value("${API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT}")
    private String API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT;

    @Value("${API_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT}")
    private String API_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT;

    @Value("${API_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE}")
    private String API_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE;

    @Value("${API_GATEWAY_API_AUTHENTICATE_USER}")
    private String API_GATEWAY_API_AUTHENTICATE_USER;

    @Value("${API_GATEWAY_API_GET_ALL_USERS}")
    private String API_GATEWAY_API_GET_ALL_USERS;

    @Value("${API_GATEWAY_API_SAVE_USER}")
    private String API_GATEWAY_API_SAVE_USER;

    @Value("${API_GATEWAY_API_DELETE_USER}")
    private String API_GATEWAY_API_DELETE_USER;

    @Value("${API_GATEWAY_API_GET_ROLES}")
    private String API_GATEWAY_API_GET_ROLES;


    public String getAPI_GATEWAY_URL_BASE() {
        return String.format("%s://%s:%s",
                API_GATEWAY_PROTOCOL,
                API_GATEWAY_NAME,
                API_GATEWAY_PORT);
    }

    public String getAPI_GATEWAY_API_GET_ALL_UNITS() {
        return API_GATEWAY_API_GET_ALL_UNITS;
    }

    public String getAPI_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS() {
        return API_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS;
    }

    public String getAPI_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS() {
        return API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS;
    }

    public String getAPI_GATEWAY_API_UNIT_MODULE_CONTROL() {
        return API_GATEWAY_API_UNIT_MODULE_CONTROL;
    }

    public String getAPI_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT() {
        return API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT;
    }

    public String getAPI_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT() {
        return API_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT;
    }

    public String getAPI_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE() {
        return API_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE;
    }


    public String getAPI_GATEWAY_API_AUTHENTICATE_USER() {
        return API_GATEWAY_API_AUTHENTICATE_USER;
    }

    public String getAPI_GATEWAY_API_GET_ALL_USERS() {
        return API_GATEWAY_API_GET_ALL_USERS;
    }

    public String getAPI_GATEWAY_API_SAVE_USER() {
        return API_GATEWAY_API_SAVE_USER;
    }

    public String getAPI_GATEWAY_API_DELETE_USER() {
        return API_GATEWAY_API_DELETE_USER;
    }

    public String getAPI_GATEWAY_API_GET_ROLES() {
        return API_GATEWAY_API_GET_ROLES;
    }
}
