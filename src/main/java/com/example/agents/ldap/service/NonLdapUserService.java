package com.example.agents.ldap.service;

import com.example.agents.ldap.entities.NonLdapUser;

public interface NonLdapUserService {

   NonLdapUser findByUserNameAndPassword(String userName, String password);

   NonLdapUser addUser(NonLdapUser nonLdapUser);

}
