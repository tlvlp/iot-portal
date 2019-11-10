package com.tlvlp.iot.server.portal.security;

import com.tlvlp.iot.server.portal.views.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form</li>

 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/" + LoginView.ROUTE;
	private static final String LOGIN_FAILURE_URL = "/" + LoginView.ROUTE;
	private static final String LOGIN_URL = "/" + LoginView.ROUTE;
	private static final String LOGOUT_SUCCESS_URL = "/" + LoginView.ROUTE;

    private BackendAuthenticationProvider authProvider;

    public SecurityConfiguration(BackendAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.requestCache().requestCache(new CustomRequestCache())
				.and()
				.authorizeRequests()
					.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
					.anyRequest().authenticated()
				.and()
				.formLogin()
					.loginPage(LOGIN_URL).permitAll()
					.loginProcessingUrl(LOGIN_PROCESSING_URL)
					.failureUrl(LOGIN_FAILURE_URL)
				.and()
				.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider)
                .eraseCredentials(false);
    }

//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		// typical logged in user with some privileges
//		UserDetails normalUser =
//				User.withUsername("user")
//						.password(passwordEncoder().encode("pass"))
//						.roles("USER")
//						.build();
//
//		// admin user with all privileges
//		UserDetails adminUser =
//				User.withUsername("admin")
//						.password(passwordEncoder().encode("pass"))
//						.roles("USER", "ADMIN")
//						.build();
//
//		return new InMemoryUserDetailsManager(normalUser, adminUser);
//	}

	@Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",
				// the standard favicon URI
				"/favicon.ico",
				// the robots exclusion standard
				"/robots.txt",
				// web application manifest
				"/manifest.webmanifest",
				"/sw.js",
				"/offline-page.html",
				// icons and images
				"/icons/**",
				"/images/**",
				// (development mode) static resources
				"/frontend/**",
				// (development mode) webjars
				"/webjars/**",
				// (development mode) H2 debugging console
				"/h2-console/**",
				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**");
	}
}
