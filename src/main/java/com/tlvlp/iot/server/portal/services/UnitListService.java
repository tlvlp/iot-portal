package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Unit> getUnitList() {
        return IntStream.range(1, 21)
                .mapToObj(i -> new Unit()
                        .setActive(true)
                        .setName("TestUnit_" + i)
                        .setLastSeen(LocalDateTime.now().minusMinutes(i))
                        .setProject("TestProject").setUnitID("TestProject-TestUnit_" + i)
                        .setScheduledEvents(Set.of())
                        .setModules(Set.of(new Module(), new Module())))
                .collect(Collectors.toList());
    }

}
