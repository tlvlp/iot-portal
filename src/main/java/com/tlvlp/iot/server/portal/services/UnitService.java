package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.Module;
import com.tlvlp.iot.server.portal.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private static final Logger log = LoggerFactory.getLogger(UnitService.class);
    private RestTemplateBuilder restTemplateBuilder;
    private Properties properties;

    public UnitService(RestTemplateBuilder restTemplateBuilder, Properties properties) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.properties = properties;
    }

    public List<UnitBasic> getUnitListWithModules() throws UnitRetrievalException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .exchange(
                        String.format("%s%s",
                                properties.getAPI_GATEWAY_URL_BASE(),
                                properties.getAPI_GATEWAY_API_GET_ALL_UNITS()),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<UnitBasic>>() {
                        })
                    .getBody();
        } catch (Exception e) {
            var err = "Unit list cannot be retrieved: " + e.getMessage();
            log.error(err);
            throw new UnitRetrievalException(err);
        }
    }

    public UnitComposition getUnitWithSchedulesAndLogs(String unitID) throws UnitRetrievalException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            var unitComposition =
                    restTemplateBuilder
                        .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                        .build()
                        .getForObject(
                            String.format("%s%s?unitID=%s&timeFrom=%s&timeTo=%s",
                                    properties.getAPI_GATEWAY_URL_BASE(),
                                    properties.getAPI_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS(),
                                    unitID,
                                    LocalDateTime.now().minusWeeks(1),
                                    LocalDateTime.now()),
                            UnitComposition.class);
            checkUnitCompositionElementsPresent(unitComposition);
            return unitComposition
                    .setModules(getUpdatedModulesWithParsedDetails(unitComposition.getUnit().getModules()))
                    .setEvents(getUpdatedEvents(unitComposition.getEvents()));
        } catch (Exception e) {
            var err = "Unit cannot be retrieved: " + e.getMessage();
            log.error(err);
            throw new UnitRetrievalException(err);
        }
    }

    private void checkUnitCompositionElementsPresent(UnitComposition unitComposition) throws UnitRetrievalException {
        if (unitComposition == null
                || unitComposition.getUnit() == null
                || unitComposition.getEvents() == null
                || unitComposition.getLogs() == null) {
            throw new UnitRetrievalException("Missing unit details! Expecting: unit, events, logs");
        }
    }


    private List<Module> getUpdatedModulesWithParsedDetails(List<Module> modules) {
        return modules
                .stream()
                .map(module -> {
                    var id = module.getModuleID();
                    return module
                            .setModuleType(id.split("\\|")[0])
                            .setModuleName(id.split("\\|")[1]);
                })
                .collect(Collectors.toList());
    }

    private List<Event> getUpdatedEvents(List<Event> events) {
        return events
                .stream()
                .map(event -> event.setEventType(getEventType(event)))
                .collect(Collectors.toList());
    }

    private EventType getEventType(Event event) {
        var targetUrl = event.getTargetURL();
        if (targetUrl.contains("mqtt")) {
            return EventType.RELAY_CONTROL;
        } else {
            log.error("Unknown event! Unable to find type for event: " + event.getEventID());
            return EventType.UNKNOWN;
        }
    }

    public void changeRelayState(Module module) throws UnitUpdateException {
        var currentState = module.getValue();
        module.setValue(currentState == 0d ? 1d : 0d);
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .postForEntity(
                        String.format("%s%s",
                                properties.getAPI_GATEWAY_URL_BASE(),
                                properties.getAPI_GATEWAY_API_UNIT_MODULE_CONTROL()),
                        module,
                        String.class);
        } catch (Exception e) {
            var err = "Problem with sending Module control message: " + e.getMessage();
            log.error(err);
            throw new UnitUpdateException(err);
        }
    }

    public void deleteScheduledEventFromUnit(Event event, String unitID) throws UnitUpdateException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .postForEntity(
                        String.format("%s%s",
                                properties.getAPI_GATEWAY_URL_BASE(),
                                properties.getAPI_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT()),
                        Map.of("unitID", unitID,
                                "event", event),
                        String.class);
        } catch (Exception e) {
            var err = "Problem with deleting Event from Unit: " + e.getMessage();
            log.error(err);
            throw new UnitUpdateException(err);
        }
    }

    public void addScheduledEventToUnit(Event event, String unitID) throws UnitUpdateException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .postForEntity(
                        String.format("%s%s",
                                properties.getAPI_GATEWAY_URL_BASE(),
                                properties.getAPI_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT()),
                        Map.of("unitID", unitID,
                                "event", event),
                        String.class);
        } catch (Exception e) {
            var err = "Problem with adding Event to Unit: " + e.getMessage();
            log.error(err);
            throw new UnitUpdateException(err);
        }
    }
}
