package com.example.agents.ldap.service.implementation;

import com.example.agents.ldap.Repository.NonLdapUserRepo;
import com.example.agents.ldap.entities.NonLdapUser;
import com.example.agents.ldap.service.NonLdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NonLdapUserImpl implements NonLdapUserService {

    @Autowired
    private NonLdapUserRepo nonLdapUserRepo;
    @Override
    public NonLdapUser findByUserNameAndPassword(String userName, String password) {

        return nonLdapUserRepo.findByUserNameAndPassword(userName, password);
    }

    @Override
    public NonLdapUser addUser(NonLdapUser nonLdapUser) {
        return nonLdapUserRepo.save(nonLdapUser);
    }
}
