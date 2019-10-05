package com.tlvlp.iot.server.portal.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ReportingService {

    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public ReportingService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    //TODO Parse Map<ChronoUnit, TreeMap<String, Double>> into ReportingData??
//    public Map<ChronoUnit, TreeMap<String, Double>> getReports(ReportingQuery reportingQuery) throws ReportingException {
//        try {
//            return restTemplate.getForObject(
//                    String.format("http://%s:%s%s?unitID=%s&moduleID=%s&timeFrom=%s&timeTo=%s&requestedScopes=%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE(),
//                            reportingQuery.getUnitID(),
//                            reportingQuery.getModuleID(),
//                            reportingQuery.getTimeFrom(),
//                            reportingQuery.getTimeTo(),
//                            String.join(",", reportingQuery.getRequestedScopes())),
//                    Map.class);
//        } catch (Exception e) {
//            var err = "Problem with retrieving reports: " + e.getMessage();
//            log.error(err);
//            throw new ReportingException(err);
//        }
//
//    }

    //TODO: REMOVE TEST METHOD
    public Map<ChronoUnit, TreeMap<String, Double>> getReports(ReportingQuery reportingQuery) throws ReportingException {
        String report = "{\n" +
                "    \"MINUTES\": {\n" +
                "        \"2019-10-01T11:42\": 85.0,\n" +
                "        \"2019-10-01T11:43\": 82.0,\n" +
                "        \"2019-10-01T11:47\": 85.0,\n" +
                "        \"2019-10-01T12:24\": 85.0,\n" +
                "        \"2019-10-01T12:28\": 85.0\n" +
                "    },\n" +
                "    \"MONTHS\": {\n" +
                "        \"2019-10\": 83.125\n" +
                "    },\n" +
                "    \"DAYS\": {\n" +
                "        \"2019-10-01\": 83.125\n" +
                "    },\n" +
                "    \"YEARS\": {\n" +
                "        \"2019\": 83.125\n" +
                "    },\n" +
                "    \"HOURS\": {\n" +
                "        \"2019-10-01T11:00\": 82.5,\n" +
                "        \"2019-10-01T12:00\": 85.0\n" +
                "    }\n" +
                "}";

        try {
            return new ObjectMapper().readValue(
                    report,
                    new TypeReference<Map<ChronoUnit, TreeMap<String, Double>>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
