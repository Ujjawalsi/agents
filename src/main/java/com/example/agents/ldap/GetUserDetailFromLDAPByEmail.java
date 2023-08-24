package com.example.agents.ldap;

import com.example.agents.ldap.controller.AuthenticateUserAPI;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.ldap.LdapContext;
import java.io.InputStream;
import java.util.Properties;

@Component
//@PropertySource("classpath:com/vel/ldap/ldap.properties")
public class GetUserDetailFromLDAPByEmail {
//	@Value("${DOMAIN}")
//    String domain;
//	@Value("${ldapUser}")
//    String ldapUser;
//	@Value("${ldapPassword}")
//    String ldapPassword;

    public String getEmailDetailFromLdap(String l_email) {
        String userName="";
        try {
            Properties prop = new Properties();
            InputStream input= AuthenticateUserAPI.class.getClassLoader().getResourceAsStream("com/example/agents/ldap/ldap.properties");
            prop.load(input);
            String domain = prop.getProperty("DOMAIN");
            String ldapUser = prop.getProperty("ldapUser");
            String ldapPassword = prop.getProperty("ldapPassword");
            LdapContext context = ActiveDirectory.getConnection(ldapUser, ldapPassword, domain);
            context.addToEnvironment(Context.REFERRAL, "follow");
            userName =ActiveDirectory.getUserID(domain,l_email, context);
            System.out.println("userName= "+userName);


        } catch (Exception e) {
            // TODO: handle exception
        }

        return userName;
    }

    public static void main(String []args) {
        GetUserDetailFromLDAPByEmail l = new GetUserDetailFromLDAPByEmail();
        l.getEmailDetailFromLdap("ranvijay");
    }


}


