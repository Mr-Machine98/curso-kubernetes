package com.mrmachine.usuarios;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
                .authorizeHttpRequests(requests -> requests
                		.requestMatchers("/authorized").permitAll()
                		.requestMatchers(HttpMethod.GET, "/all-info-pod", "/", "/{id}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                		.requestMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_write")
                		.requestMatchers(HttpMethod.PUT, "/{id}").hasAuthority("SCOPE_write")
                		.requestMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
                		.anyRequest()
                		.authenticated()
                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/ms-usuarios-client"))
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(oAuth2 -> oAuth2.jwt(withDefaults()))
                .build();
	}
}
