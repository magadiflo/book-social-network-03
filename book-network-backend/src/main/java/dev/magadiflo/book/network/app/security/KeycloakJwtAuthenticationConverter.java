package dev.magadiflo.book.network.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(jwt,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(jwt).stream(),
                        this.extractResourceRoles(jwt).stream()
                ).collect(Collectors.toSet()));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Map<String, List<String>>> resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var eternal = (Map<String, List<String>>) resourceAccess.get("account");

        return eternal.get("roles").stream()
                .map(roleName -> "ROLE_" + roleName.replace("-", "_"))
                .peek(roleName -> log.info("roleName: {}", roleName))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
