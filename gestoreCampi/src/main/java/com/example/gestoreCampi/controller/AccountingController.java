package com.example.gestoreCampi.controller;

import com.example.gestoreCampi.entities.User;
import com.example.gestoreCampi.services.AccountingService;
import com.example.gestoreCampi.support.exception.EmailAlreadyUsedException;
import com.example.gestoreCampi.support.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.Data;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountingController {
    @Autowired
    private AccountingService accountingService;

    @PostMapping("/add")
    public ResponseEntity create(@RequestBody @Valid UserReq userReq){
        try{
            User added = accountingService.addUser(userReq.getUser(),userReq.getPassword());
            return new ResponseEntity(added, HttpStatus.OK);
        }
        catch(EmailAlreadyUsedException e){
            return new ResponseEntity("user already exists",HttpStatus.BAD_REQUEST);
        }
    }//create


    //@PreAuthorize("hasRole('admin')")
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam @Valid User user){
        if(user.getEmail()!=null) {
            try {
                accountingService.deleteUserByEmail(user.getEmail());
            } catch (UserNotFoundException e) {
                return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
            }
        }else
            return new ResponseEntity("email not found ", HttpStatus.BAD_REQUEST);

        return new ResponseEntity("User "+user.getEmail()+" succesfully deleted",HttpStatus.OK);
    }//delete


    @PostMapping("/delete/email")
    public ResponseEntity deleteEmail(@RequestParam String email){
        try {
            accountingService.deleteUserByEmail(email);
        } catch (UserNotFoundException e) {
            return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("User "+email+" succesfully deleted",HttpStatus.OK);
    }//deleteEmail



    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int numPage,
                                 @RequestParam(defaultValue = "10") int dimPage,
                                 @RequestParam(value = "sortBy", defaultValue = "email") String sortBy){
        List<User> result = accountingService.showAllUsers(numPage, dimPage, sortBy);
        if(result.size() <= 0)
            return new ResponseEntity("No user registered in the system",HttpStatus.OK);
        return new ResponseEntity(result,HttpStatus.OK);
    }//getAll


    @GetMapping("/all")
    public ResponseEntity getAll(){
        List<User>result = accountingService.showAllUsers();
        if(result.size() <= 0)
            return new ResponseEntity("No user registered in the system",HttpStatus.OK);
        return new ResponseEntity(result,HttpStatus.OK);
    }//getAll


    @GetMapping("/token")
    public ResponseEntity getToken(String user,String pass) throws IOException {
        String result = "";
        HttpPost post = new HttpPost("http://localhost:8080/realms/myrealm/protocol/openid-connect/token");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<BasicNameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "password"));
        parameters.add(new BasicNameValuePair("client_id", "myclient"));
        parameters.add(new BasicNameValuePair("client_secret", "u5kadDfKitzeDg8yux824bRa9tvvvlWW"));
        parameters.add(new BasicNameValuePair("username", user));
        parameters.add(new BasicNameValuePair("password", pass));
        post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            result = EntityUtils.toString(response.getEntity());
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }//getToken


}//AccountingController

@Data
class UserReq{
    private User user;
    private String password;
}
