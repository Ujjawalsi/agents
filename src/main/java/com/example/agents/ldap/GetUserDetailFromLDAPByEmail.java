package com.example.agents.ldap;

import com.example.agents.constant.Constant;
import com.example.agents.ldap.controller.AuthenticateUserAPI;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.ldap.LdapContext;
import java.io.InputStream;
import java.util.Properties;

import static com.example.agents.constant.Constant.*;

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
            LdapContext context = ActiveDirectory.getConnection(ldapUser, ldapPassword, DOMAIN);
            context.addToEnvironment(Context.REFERRAL, "follow");
            userName =ActiveDirectory.getUserID(DOMAIN,l_email, context);
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


