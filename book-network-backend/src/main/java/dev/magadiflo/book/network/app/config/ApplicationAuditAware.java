package dev.magadiflo.book.network.app.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * AuditorAware<String>, el String corresponde con el tipo de dato del id del
 * usuario registrado en Keycloak. Necesitamos el tipo de dato del identificador del usuario,
 * pues ese identificador será el que se registre en los campos createdBy y lastModifiedBy
 * de la clase BaseEntity.
 */
public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return Optional.ofNullable(authentication.getName()); //authentication.getName(), nos retorna el identificador del usuario de keycloak
    }
}
