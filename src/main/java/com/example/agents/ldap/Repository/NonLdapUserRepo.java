package com.example.agents.ldap.Repository;

import com.example.agents.ldap.entities.NonLdapUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonLdapUserRepo extends JpaRepository<NonLdapUser, Long> {
    NonLdapUser findByUserNameAndPassword(String userName, String password);
}
