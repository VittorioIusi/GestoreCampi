package com.example.gestoreCampi.services;

import com.example.gestoreCampi.entities.User;
import com.example.gestoreCampi.repositories.UserRepository;
import com.example.gestoreCampi.support.exception.EmailAlreadyUsedException;
import com.example.gestoreCampi.support.exception.UserNotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;





@Service
public class AccountingService {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${auth-properties.clientid}")
    private String clientId;

    @Value("${auth-properties.clientsecret}")
    private String clientSecret;

    @Value("${auth-properties.usernameadmin}")
    private String usernameAdmin;

    @Value("${auth-properties.passwordadmin}")
    private String passwordAdmin;

    @Value("${auth-properties.role}")
    private String role;

    @Autowired
    private UserRepository userRepo;


    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void deleteUserByEmail(String email) throws UserNotFoundException {
        if(!userRepo.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(usernameAdmin)
                .password(passwordAdmin)
                .build();

        User user = userRepo.findUserByEmail(email);
        userRepo.deleteByEmail(email);
        List<UserRepresentation>userList = keycloak.realm(realm).users().search(user.getEmail());
        for(UserRepresentation u : userList) {
            if (u.getUsername().equals(user.getEmail())) {
                keycloak.realm(realm).users().delete(u.getId());
            }
        }
    }//deleteUserByEmail


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User addUser(User user,String password) throws EmailAlreadyUsedException {
        if(userRepo.existsByEmail(user.getEmail()))
            throw new EmailAlreadyUsedException();

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(usernameAdmin)
                .password(passwordAdmin)
                .build();

        //defineUser
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

        //get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        //create user
        Response response = usersResource.create(userRepresentation);
        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userR = usersResource.get(userId);

        //define passCredential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        //set password credential
        userR.resetPassword(passwordCred);

        //get client
        ClientRepresentation app1Client = realmResource.clients().findByClientId(clientId).get(0);

        //get client role
        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(role).toRepresentation();

        //assign client level role to user
        userR.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
        return userRepo.save(user);

    }//addUser




    @Transactional(readOnly = true)
    public List<User>showAllUsers(){
        return userRepo.findAll();
    }//showAllUser


    @Transactional(readOnly = true)
    public List<User>showAllUsers(int numP,int dimP,String sortBy){
        Pageable pageable= PageRequest.of(numP,dimP, Sort.by(sortBy));
        Page<User> p=userRepo.findAll(pageable);
        if(p.hasContent())
            return p.getContent();
        return new ArrayList<>();
    }//showAllUser



    @Transactional(readOnly = true)
    public User showByEmail(String email){
        return userRepo.findByEmail(email);
    }//showByEmail


}//AccountingService
