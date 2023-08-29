package com.example.agents.ldap.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "non_ldap_users")
public class NonLdapUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id")
    private Long id ;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(unique = true)
    private String password;
    private List<String> role;
}
