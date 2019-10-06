package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.Average;
import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportingService {

    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public ReportingService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

//    public List<Average> getReports(ReportingQuery reportingQuery) throws ReportingException {
//        try {
//            return restTemplate.getForObject(
//                    String.format("http://%s:%s%s?unitID=%s&moduleID=%s&timeFrom=%s&timeTo=%s&requestedScopes=%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE(),
//                            reportingQuery.getUnit().getUnitID(),
//                            reportingQuery.getModule().getModuleID(),
//                            reportingQuery.getTimeFrom(),
//                            reportingQuery.getTimeTo(),
//                            String.join(",", reportingQuery.getRequestedScopes())),
//                    new ParameterizedTypeReference<List<Average>>() {});
//        } catch (Exception e) {
//            var err = "Cannot retrieve reports: " + e.getMessage();
//            log.error(err);
//            throw new ReportingException(err);
//        }
//    }

    //TODO: REMOVE TEST METHOD
    public List<Average> getReports(ReportingQuery reportingQuery) throws ReportingException {
        return List.of(
                new Average().setScope(ChronoUnit.DAYS).setDate("2018-01-10").setValue(21.0),
                new Average().setScope(ChronoUnit.DAYS).setDate("2018-01-11").setValue(11.0),
                new Average().setScope(ChronoUnit.DAYS).setDate("2018-01-12").setValue(12.0),
                new Average().setScope(ChronoUnit.MONTHS).setDate("2018-02").setValue(24.0),
                new Average().setScope(ChronoUnit.MONTHS).setDate("2018-03").setValue(20.0)
        );
    }
}
