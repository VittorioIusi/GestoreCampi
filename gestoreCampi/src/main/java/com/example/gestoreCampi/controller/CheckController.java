package com.example.gestoreCampi.controller;

import com.example.gestoreCampi.configurations.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class CheckController {


    @GetMapping("/simple")
    public ResponseEntity checkSimple(){
        return new ResponseEntity("tutto ok richiesta effetuata", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/logged")
    public ResponseEntity checkLogged() {
        return new ResponseEntity("Check status ok, hi " + Utils.getEmail() + "!", HttpStatus.OK);
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    public ResponseEntity checkAdmin() {
        return new ResponseEntity("Check status ok, hi " + Utils.getEmail() + "! ciao capo", HttpStatus.OK);
    }
}
