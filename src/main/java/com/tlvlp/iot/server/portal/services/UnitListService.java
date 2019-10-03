package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class UnitListService {

    private RestTemplate restTemplate;
    private Properties properties;

    public UnitListService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

//    public List<Unit> getUnitList() {
//        return restTemplate.exchange(
//                String.format("http://%s:%s%s",
//                        properties.getAPI_GATEWAY_NAME(),
//                        properties.getAPI_GATEWAY_PORT(),
//                        properties.getAPI_GATEWAY_API_GET_ALL_UNITS()),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Unit>>(){})
//                .getBody();
//    }

    private List<Unit> parseModulesForUI(List<Unit> unitList) {
        return unitList
                .stream()
                .map(unit -> {
                    var parsedModules = unit.getModules()
                            .stream()
                            .map(module -> {
                                var id = module.getModuleID();
                                return module.setModuleType(id.split("\\|")[0]).setModuleName(id.split("\\|")[1]); })
                            .collect(Collectors.toSet());
                    return unit.setModules(parsedModules);
                })
                .collect(Collectors.toList());
    }

    public List<Unit> getUnitList() {
        return parseModulesForUI(
                IntStream.range(1, 21)
                        .mapToObj(i -> new Unit()
                                .setActive(i % 2 == 0)
                                .setName("Unit_" + i)
                                .setLastSeen(LocalDateTime.now().minusMinutes(i))
                                .setProject(i%2 ==0 ? "TestProject" : "BazsalikOn")
                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                .setScheduledEvents(Set.of())
                                .setModules(Set.of(
                                        new Module().setModuleID("light|growlight1")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i),
                                        new Module().setModuleID("light|growlight2")
                                                .setUnitID(i%2 ==0 ? "TestProject_Unit_" + i : "BazsalikOn_Unit_" + i)
                                                .setValue((double) i-1))))
                        .collect(Collectors.toList()));
    }

}
