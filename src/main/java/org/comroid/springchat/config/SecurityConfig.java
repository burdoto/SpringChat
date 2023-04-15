package org.comroid.springchat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.comroid.api.Polyfill;
import org.comroid.springchat.SpringChatApplication;import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .anyRequest().authenticated().and()
                .formLogin().disable()
                .oauth2Login().and()
                .rememberMe().and()
                .build();
    }

    @Bean
    protected ClientRegistrationRepository clientRegistrationRepository() throws IOException {
        var mapper = new ObjectMapper();
        var data = (ObjectNode) mapper.readTree(SpringChatApplication.OAUTH_FILE);
        var registrations = new ArrayList<ClientRegistration>();

        var keys = data.fieldNames();
        while (keys.hasNext()) {
            var name = keys.next();
            var registration = ClientRegistration.withRegistrationId(name);
            registration.clientName(name);
            registration.clientId(data.get(name).get("client-id").asText());
            registration.clientSecret(data.get(name).get("client-secret").asText());
            registration.redirectUri(data.get(name).get("redirect-uri").asText());
            registration.scope(Set.of(data.get(name).get("scope").asText()));
            registration.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);

            registrations.add(registration.build());
        }

        return new InMemoryClientRegistrationRepository(registrations);
    }
}