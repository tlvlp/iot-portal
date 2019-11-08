package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminService {
    private static final Logger log = LoggerFactory.getLogger(UserAdminService.class);

    private RestTemplateBuilder restTemplateBuilder;
    private Properties properties;

    public UserAdminService(RestTemplateBuilder restTemplateBuilder, Properties properties) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.properties = properties;
    }

    public User getUserDetailsAfterAuthentication(String userID, String password) throws UserAdminException {
        try {
            return restTemplateBuilder
                    .basicAuthentication(userID, password)
                    .build()
                    .getForObject(
                            String.format("https://%s:%s%s",
                                    properties.getAPI_GATEWAY_NAME(),
                                    properties.getAPI_GATEWAY_PORT_TLS(),
                                    properties.getAPI_GATEWAY_API_AUTHENTICATE_USER()),
                            User.class);
        } catch (Exception e) {
            var err = "Cannot retrieve users: " + e.getMessage();
            log.error(err);
            throw new UserAdminException(err);
        }
    }

    public List<User> getAllUsers() throws UserAdminException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .exchange(
                            String.format("https://%s:%s%s",
                                    properties.getAPI_GATEWAY_NAME(),
                                    properties.getAPI_GATEWAY_PORT_TLS(),
                                    properties.getAPI_GATEWAY_API_GET_ALL_USERS()),
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<User>>() {
                            })
                    .getBody();
        } catch (Exception e) {
            var err = "Cannot retrieve users: " + e.getMessage();
            log.error(err);
            throw new UserAdminException(err);
        }
    }

    public List<String> getRoles() throws UserAdminException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .exchange(
                            String.format("https://%s:%s%s",
                                    properties.getAPI_GATEWAY_NAME(),
                                    properties.getAPI_GATEWAY_PORT_TLS(),
                                    properties.getAPI_GATEWAY_API_GET_ROLES()),
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<String>>() {
                            })
                    .getBody();
        } catch (Exception e) {
            var err = "Cannot retrieve user roles: " + e.getMessage();
            log.error(err);
            throw new UserAdminException(err);
        }
    }

    public void deleteUser(User user) throws UserAdminException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .postForEntity(
                            String.format("https://%s:%s%s",
                                    properties.getAPI_GATEWAY_NAME(),
                                    properties.getAPI_GATEWAY_PORT_TLS(),
                                    properties.getAPI_GATEWAY_API_DELETE_USER()),
                            user,
                            String.class);
        } catch (Exception e) {
            var err = "Cannot delete user: " + e.getMessage();
            log.error(err);
            throw new UserAdminException(err);
        }
    }

    public void saveUser(User user) throws UserAdminException {
        try {
            if (user.getRoles().contains("BACKEND")) {
                throw new UserAdminException("Users cannot have a BACKEND role!");
            }
            var auth = SecurityContextHolder.getContext().getAuthentication();
            restTemplateBuilder
                    .basicAuthentication(auth.getName(), auth.getCredentials().toString())
                    .build()
                    .postForEntity(
                            String.format("https://%s:%s%s",
                                    properties.getAPI_GATEWAY_NAME(),
                                    properties.getAPI_GATEWAY_PORT_TLS(),
                                    properties.getAPI_GATEWAY_API_SAVE_USER()),
                            user,
                            String.class);
        } catch (Exception e) {
            var err = "Cannot save user: " + e.getMessage();
            log.error(err);
            throw new UserAdminException(err);
        }
    }
}
