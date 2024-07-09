package com.example.gestoreCampi.support.authentication;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import java.security.Principal;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
@Log4j2
public class Utils {


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


}