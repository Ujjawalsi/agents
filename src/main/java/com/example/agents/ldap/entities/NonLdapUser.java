package com.example.agents.ldap.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "non_ldap_users")
public class NonLdapUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id")
    private Long id ;
    @Column(name = "user_name")
    private String userName;
    private String password;
    private String role;
}
