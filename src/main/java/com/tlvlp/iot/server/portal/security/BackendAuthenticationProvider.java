package com.tlvlp.iot.server.portal.security;

import com.tlvlp.iot.server.portal.services.UserAdminException;
import com.tlvlp.iot.server.portal.services.UserAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BackendAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(BackendAuthenticationProvider.class);
    private UserAdminService userAdminService;

    public BackendAuthenticationProvider(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userID = authentication.getName();
        var password = authentication.getCredentials().toString();
        try {
            var user = userAdminService.getUserDetailsAfterAuthentication(userID, password);
            var grantedAuthorities = user.getRoles()
                    .stream()
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(userID, password, grantedAuthorities);
        } catch (UserAdminException e) {
            var err = "Authentication failed for User ID: " + userID;
            log.error(err);
            throw new BadCredentialsException(err);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
