package com.example.gestoreCampi.controller;

import com.example.gestoreCampi.entities.User;
import com.example.gestoreCampi.services.AccountingService;
import com.example.gestoreCampi.services.BookingService;
import com.example.gestoreCampi.configurations.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private BookingService bookingService;

    @RequestMapping("/")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity home(){
        if(Utils.getEmail() != null)
            return new ResponseEntity("Hello "+ Utils.getEmail()+", welcome back on Partitina?", HttpStatus.OK);
        return new ResponseEntity("Not logged",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/getUser")
    //@PreAuthorize("hasRole('user')")
    public User getUser() {
        System.out.println("sono nel getuser");
        String email=Utils.getEmail();
        return accountingService.showByEmail(email);
    }
    @RequestMapping("/profilo")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity getProfile() {
        User user=getUser();
        return new ResponseEntity(user,HttpStatus.OK);
    }

}//HomeController
