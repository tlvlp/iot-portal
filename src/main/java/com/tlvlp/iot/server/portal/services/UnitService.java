package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UnitService {

    private static final Logger log = LoggerFactory.getLogger(UnitService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public UnitService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

//    public List<Unit> getUnitList() throws UnitRetrievalException {
//        try {
//            var unitList = restTemplate.exchange(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_ALL_UNITS()),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Unit>>() {
//                    })
//                    .getBody();
//            return getUnitListWithParsesModules(unitList);
//        } catch (Exception e) {
//            throw new UnitRetrievalException("Unit list cannot be retrieved: " + e.getMessage());
//        }
//    }

    private List<Unit> getUnitListWithParsesModules(List<Unit> unitList) {
        return unitList
                .stream()
                .map(unit -> {
                    var parsedModules = unit.getModules()
                            .stream()
                            .map(module -> {
                                var id = module.getModuleID();
                                return module.setModuleType(id.split("\\|")[0]).setModuleName(id.split("\\|")[1]); })
                            .collect(Collectors.toList());
                    return unit.setModules(parsedModules);
                })
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
//            return unitComposition.getUnit()
//                    .setScheduledEvents(unitComposition.getEvents())
//                    .setLogs(unitComposition.getLogs());
//        } catch (Exception e) {
//            var err = "Unit cannot be retrieved" + e.getMessage();
//            log.error(err);
//            throw new UnitRetrievalException(err);
//        }
//    }

//    public void changeRelayStateFor(Module module) throws ModuleStateChangeException {
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s?unitID=%s&timeFrom=%s&timeTo=%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_UNIT_MODULE_CONTROL()),
//                    module,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Problem with sending Module control message: " + e.getMessage();
//            log.error(err);
//            throw new ModuleStateChangeException(err);
//        }
//    }

    //TODO: REMOVE TEST DATA
    public void changeRelayStateFor(Module module) throws ModuleStateChangeException {

    }

    //TODO: REMOVE TEST DATA
    public Unit getUnitWithSchedulesAndLogs(String unitID) throws UnitRetrievalException {
        var unitList = getUnitListWithParsesModules(
                IntStream.range(1, 101)
                        .mapToObj(i -> new Unit()
                                .setActive(i % 2 == 0)
                                .setName("Unit_" + i)
                                .setLastSeen(LocalDateTime.now().minusMinutes(i))
                                .setProject(i%2 ==0 ? "TestProject" : "BazsalikOn")
                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                .setScheduledEventIDs(List.of())
                                .setModules(List.of(
                                        new Module().setModuleID("light|growlight1")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i),
                                        new Module().setModuleID("light|growlight2")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i-1))))
                        .collect(Collectors.toList()));

        var unitSelected = unitList.stream().filter(unit -> unit.getUnitID().equals(unitID)).findFirst().orElseGet(Unit::new);

        return unitSelected
                .setScheduledEvents(List.of(new Event()))
                .setLogs(List.of(new Log().setArrived(LocalDateTime.now().minusHours(10)).setLogEntry("Something interesting happened")));
    }

    //TODO: REMOVE TEST DATA
    public List<Unit> getUnitList() throws UnitRetrievalException {
        return getUnitListWithParsesModules(
                IntStream.range(1, 101)
                        .mapToObj(i -> new Unit()
                                .setActive(i % 2 == 0)
                                .setName("Unit_" + i)
                                .setLastSeen(LocalDateTime.now().minusMinutes(i))
                                .setProject(i%2 ==0 ? "TestProject" : "BazsalikOn")
                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                .setScheduledEventIDs(List.of())
                                .setModules(List.of(
                                        new Module().setModuleID("light|growlight1")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i),
                                        new Module().setModuleID("light|growlight2")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i-1))))
                        .collect(Collectors.toList()));
    }
}
