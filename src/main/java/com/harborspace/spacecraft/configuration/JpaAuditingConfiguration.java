package com.harborspace.spacecraft.configuration;

import java.util.Optional;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> {
            String res = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                Object authDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
                if(authDetails instanceof SimpleKeycloakAccount) {
                    SimpleKeycloakAccount auth = (SimpleKeycloakAccount) authDetails;
                    res = auth.getKeycloakSecurityContext().getToken().getSubject();
                }
            }
            return Optional.ofNullable(res);
        };
    }
}