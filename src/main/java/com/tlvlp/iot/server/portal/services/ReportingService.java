package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.Average;
import com.tlvlp.iot.server.portal.entities.ReportingQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {

    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);
    private RestTemplateBuilder restTemplateBuilder;
    private Properties properties;

    public ReportingService(RestTemplateBuilder restTemplateBuilder, Properties properties) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.properties = properties;
    }

    public List<Average> getReports(ReportingQuery reportingQuery) throws ReportingException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .exchange(
                        String.format("%s%s?unitID=%s&moduleID=%s&timeFrom=%s&timeTo=%s&requestedScopes=%s",
                                properties.getAPI_GATEWAY_URL_BASE(),
                                properties.getAPI_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE(),
                                reportingQuery.getUnit().getUnitID(),
                                reportingQuery.getModule().getModuleID(),
                                reportingQuery.getTimeFrom(),
                                reportingQuery.getTimeTo(),
                                String.join(",", reportingQuery.getRequestedScopes())),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Average>>() {})
                    .getBody();
        } catch (Exception e) {
            var err = "Cannot retrieve reports: " + e.getMessage();
            log.error(err);
            throw new ReportingException(err);
        }
    }

}
