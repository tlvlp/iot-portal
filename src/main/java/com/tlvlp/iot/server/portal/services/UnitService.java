package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.Module;
import com.tlvlp.iot.server.portal.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UnitService {

    private static final Logger log = LoggerFactory.getLogger(UnitService.class);
    private Properties properties;

    public UnitService(Properties properties) {
        this.properties = properties;
    }

    public List<Unit> getUnitListWithModules() throws UnitRetrievalException {
        var unitListRaw = getUnitList();
        return updateUnitListWithParsedModules(unitListRaw);
    }

//    private List<Unit> getUnitList() throws UnitRetrievalException {
//        try {
//            return restTemplate.exchange(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_ALL_UNITS()),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Unit>>() {
//                    })
//                    .getBody();
//        } catch (Exception e) {
//            throw new UnitRetrievalException("Unit list cannot be retrieved: " + e.getMessage());
//        }
//    }

    private List<Unit> updateUnitListWithParsedModules(List<Unit> unitList) {
        return unitList
                .stream()
                .map(this::updateUnitWithParsedModules)
                .collect(Collectors.toList());
    }

//    public Unit getUnitWithSchedulesAndLogs(String unitID) throws UnitRetrievalException {
//        try {
//            UnitComposition unitComposition = restTemplate.getForObject(
//                    String.format("http://%s:%s%s?unitID=%s&timeFrom=%s&timeTo=%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS(),
//                            unitID,
//                            LocalDateTime.now().minusWeeks(1),
//                            LocalDateTime.now()),
//                    UnitComposition.class);
//            var unit = unitComposition.getUnit();
//            var parsedModules = getParsedModules(unit.getModules());
//            var parsedEvents = getParsedEvents(unitComposition.getEvents());
//            return unit
//                    .setModules(parsedModules)
//                    .setScheduledEvents(parsedEvents)
//                    .setLogs(unitComposition.getLogs());
//        } catch (Exception e) {
//            var err = "Unit cannot be retrieved" + e.getMessage();
//            log.error(err);
//            throw new UnitRetrievalException(err);
//        }
//    }

    private Unit updateUnitWithParsedModules(Unit unit) {
        var parsedModules = getParsedModules(unit.getModules());
        return unit.setModules(parsedModules);
    }

    private List<Module> getParsedModules(List<Module> modules) {
        return modules
                .stream()
                .map(module -> {
                    var id = module.getModuleID();
                    return module.setModuleType(id.split("\\|")[0]).setModuleName(id.split("\\|")[1]);
                })
                .collect(Collectors.toList());
    }

    private List<Event> getParsedEvents(List<Event> events) {
        return events
                .stream()
                .map(event -> event.setEventType(getEventType(event)))
                .collect(Collectors.toList());
    }

    private EventType getEventType(Event event) {
        var targetUrl = event.getTargetURL();
        if (targetUrl.contains(properties.getAPI_GATEWAY_API_UNIT_MODULE_CONTROL())) {
            return EventType.RELAY_CONTROL;
        } else {
            log.error("Unknown event! Unable to find type for event: " + event.getEventID());
            return EventType.UNKNOWN;
        }
    }

//    public void changeRelayStateFor(Module module) throws UnitUpdateException {
//        var currentState = module.getValue();
//        module.setValue(currentState == 0d ? 1d : 0d);
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_UNIT_MODULE_CONTROL()),
//                    module,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Problem with sending Module control message: " + e.getMessage();
//            log.error(err);
//            throw new UnitUpdateException(err);
//        }
//    }

//    public void deleteScheduledEventFromUnit(Event event) throws UnitUpdateException {
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT()),
//                    event,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Problem with deleting Event from Unit: " + e.getMessage();
//            log.error(err);
//            throw new UnitUpdateException(err);
//        }
//    }

//    public void addScheduledEventToUnit(Event event) throws UnitUpdateException {
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT()),
//                    event,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Problem with adding Event to Unit: " + e.getMessage();
//            log.error(err);
//            throw new UnitUpdateException(err);
//        }
//    }

    //TODO: REMOVE TEST METHOD
    public void changeRelayStateFor(Module module) throws UnitUpdateException {
        log.info("CHANGING RELAY STATE");
    }

    //TODO: REMOVE TEST METHOD
    public Unit getUnitWithSchedulesAndLogs(String unitID) throws UnitRetrievalException {
        //mock selected unit
        var unitList = getUnitList();
        Unit unitSelected = unitList.stream().filter(unit -> unit.getUnitID().equals(unitID)).findFirst().orElseGet(Unit::new);
        // update it with events and logs
        unitSelected
                .setScheduledEvents(List.of(new Event()
                        .setCronSchedule("* * * * *")
                        .setEventID("eventID")
                        .setInfo("Test event").setLastUpdated(LocalDateTime.now())
                        .setPayload(Map.of("pay", "load"))
                        .setTargetURL("http://test/modulecontrolfake")))
                .setLogs(List.of(new Log().setArrived(LocalDateTime.now().minusHours(10)).setLogEntry("Something interesting happened")));
        var parsedModules = getParsedModules(unitSelected.getModules());
        var parsedEvents = getParsedEvents(unitSelected.getScheduledEvents());
        unitSelected.setModules(parsedModules).setScheduledEvents(parsedEvents);
        return unitSelected;
    }

    //TODO: REMOVE TEST METHOD
    private List<Unit> getUnitList() throws UnitRetrievalException {
        return IntStream.range(1, 101)
                .mapToObj(i -> new Unit()
                        .setActive(i % 2 == 0)
                        .setName("Unit_" + i)
                        .setLastSeen(LocalDateTime.now().minusMinutes(i))
                        .setProject(i % 2 == 0 ? "TestProject" : "BazsalikOn")
                        .setUnitID(i % 2 == 0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                        .setScheduledEventIDs(List.of())
                        .setModules(List.of(
                                new Module().setModuleID("light|growlightpercent")
                                        .setUnitID(i % 2 == 0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                        .setValue((double) i),
                                new Module().setModuleID("relay|growlightrelay")
                                        .setUnitID(i % 2 == 0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                        .setValue(1d))))
                .map(this::updateUnitWithParsedModules)
//                .peek(unit -> log.info("GENERATING RAW UNIT: " + unit.toString()))
                .collect(Collectors.toList());
    }

    //TODO: REMOVE TEST METHOD
    public void deleteScheduledEventFromUnit(Event event) throws UnitUpdateException {
        System.out.println("DELETE EVENT: " + event);
    }

    //TODO: REMOVE TEST METHOD
    public void addScheduledEventToUnit(Event event) throws UnitUpdateException {
        System.out.println("SAVE EVENT: " + event);
    }
}
