package com.twistercambodia.karasbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {}

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            // Extract resource_access from claims
            final var resourceAccess = Optional.ofNullable((Map<String, Object>) claims.get("resource_access"));

            // Extract karas-frontend from resource_access
            final var karasFrontendAccess = resourceAccess
                    .map(access -> (Map<String, Object>) access.get("karas-frontend"))
                    .orElseThrow(() -> new IllegalArgumentException("karas-frontend not found"));

            // Extract roles as Optional
            final var roles = Optional.ofNullable((List<String>) karasFrontendAccess.get("roles"));

            // Map roles to GrantedAuthority and return as a list
            return roles
                    .stream()  // Create a stream from the Optional
                    .flatMap(List::stream)  // Flatten the List<String>
                    .map(SimpleGrantedAuthority::new)  // Map to SimpleGrantedAuthority
                    .map(GrantedAuthority.class::cast)  // Cast to GrantedAuthority
                    .toList();  // Collect to List
        };
    }


    @Bean
    JwtAuthenticationConverter authenticationConverter(
            Converter<Map<String, Object>, Collection<GrantedAuthority>> authoritiesConverter) {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter
                .setJwtGrantedAuthoritiesConverter(jwt -> {
                    return authoritiesConverter.convert(jwt.getClaims());
                });
        return jwtAuthenticationConverter;
    }

    @Bean
    SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http,
            Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) throws Exception {
        http.oauth2ResourceServer(resourceServer -> {
            resourceServer.jwt(jwtDecoder -> {
                jwtDecoder.jwtAuthenticationConverter(jwtAuthenticationConverter);
            });
        });

        http.sessionManagement(sessions -> {
            sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }).csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/login")
                    .permitAll()
                    .anyRequest()
                    .hasRole("USER");
        });

        return http.build();
    }
}
