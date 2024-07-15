package com.example.gestoreCampi.configurations;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
@Log4j2
public class Utils {


    private Jwt getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        return null;
    }

    public String getEmail() {
        Jwt jwt = getToken();
        if (jwt != null) {
            return jwt.getClaimAsString("email");
        }
        return null;
    }

    public String getId() {
        Jwt jwt = getToken();
        if (jwt != null) {
            return jwt.getClaimAsString("sub");
        }
        return null;
    }


    //CLASSE CHICCO
    /*
    public Principal getPrincipal() {
        return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @SuppressWarnings("unchecked")
    private AccessToken getToken()
    {
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal=(KeycloakPrincipal<KeycloakSecurityContext>) getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext().getToken();
    }

    public String getEmail(){
        return getToken().getEmail();
    }
    public String getId(){
        return getToken().getId();
    }

     */

}