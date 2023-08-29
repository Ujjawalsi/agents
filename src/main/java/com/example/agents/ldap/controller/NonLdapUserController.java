package com.example.agents.ldap.controller;

import com.example.agents.ldap.entities.NonLdapUser;
import com.example.agents.ldap.service.NonLdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nonLdap")
public class NonLdapUserController {

    @Autowired
    private NonLdapUserService nonLdapUserService;
    @PostMapping("/user")
    public ResponseEntity<com.example.agents.ldap.entities.NonLdapUser> adduser(@RequestBody NonLdapUser nonLdapUser){
     NonLdapUser savedUser =  this.nonLdapUserService.addUser(nonLdapUser);
     return new ResponseEntity<>(savedUser, HttpStatus.CREATED);


    }
}
