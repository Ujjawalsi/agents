package com.example.agents.ldap;
import org.springframework.stereotype.Component;
import javax.naming.Context;
import javax.naming.ldap.LdapContext;
import static com.example.agents.constant.Constant.*;

@Component
public class GetUserDetailFromLDAPByEmail {
    public String getEmailDetailFromLdap(String l_email) {
        String userName="";
        try {
            LdapContext context = ActiveDirectory.getConnection(ldapUser, ldapPassword, DOMAIN);
            context.addToEnvironment(Context.REFERRAL, "follow");
            userName =ActiveDirectory.getUserID(DOMAIN,l_email, context);


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


