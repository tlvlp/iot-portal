package com.tlvlp.iot.server.portal.services;

import com.tlvlp.iot.server.portal.config.Properties;
import com.tlvlp.iot.server.portal.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

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
//    public List<String> getRoles() throws UserAdminAcception {
//        try {
//            return restTemplate.getForObject(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_GET_ROLES()),
//                    new ParameterizedTypeReference<List<String>>() {});
//        } catch (Exception e) {
//            var err = "Cannot retrieve user roles: " + e.getMessage();
//            log.error(err);
//            throw new UserAdminAcception(err);
//        }

//    }

//    public void deleteUser(User user) throws UserAdminAcception {
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_DELETE_USER()),
//                    user,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Cannot delete user: " + e.getMessage();
//            log.error(err);
//            throw new UserAdminAcception(err);
//        }

//    }
    //    public void saveUser(User user) throws UserAdminAcception {
//        try {
//            restTemplate.postForEntity(
//                    String.format("http://%s:%s%s",
//                            properties.getAPI_GATEWAY_NAME(),
//                            properties.getAPI_GATEWAY_PORT(),
//                            properties.getAPI_GATEWAY_API_SAVE_USER()),
//                    user,
//                    String.class);
//        } catch (Exception e) {
//            var err = "Cannot save user: " + e.getMessage();
//            log.error(err);
//            throw new UserAdminAcception(err);
//        }
//    }
    //TODO REMOVE MOCK
    public void saveUser(User user) throws UserAdminException {
        log.info("SAVE USER! " + user);
    }

    //TODO REMOVE MOCK
    public void deleteUser(User user) throws UserAdminException {
        log.info("DELETE USER!");
    }


    //TODO REMOVE MOCK
    public List<User> getAllUsers() throws UserAdminException {
        var users = List.of(
                new User()
                        .setUserID("tlvlp")
                        .setActive(true)
                        .setFirstName("Peter")
                        .setLastName("Veres")
                        .setEmail("no@ogfgf.com")
                        .setPassword("")
                        .setRoles(Set.of("ADMIN", "USER")),

                new User()
                        .setUserID("tulp")
                        .setActive(true)
                        .setFirstName("Pierre")
                        .setLastName("Veres")
                        .setEmail("no@ogfgfrerere.com")
                        .setPassword("")
                        .setRoles(Set.of("USER"))
        );

        System.out.println("GENERATED USERS: " + users);

        return users;
    }

    //TODO REMOVE MOCK
    public List<String> getRoles() throws UserAdminException {
        return List.of("ADMIN", "BACKEND", "USER");
    }

}
