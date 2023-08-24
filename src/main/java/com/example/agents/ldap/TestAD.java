package com.example.agents.ldap;

import com.example.agents.ldap.ActiveDirectory.User;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestAD {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {





            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");//2023-01-19 11:15:00
            Date dt = sdf.parse("2023-01-20 00:00:00");
            System.out.println("dt: "+dt);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");//2023-01-19 11:15:00
            String dt1 = sdf1.format(dt);
            System.out.println(dt1);








//			Ambition@201301
            LdapContext context = ActiveDirectory.getConnection("manoj.gupta", "Ambition@201301", "velocis.in");
            User user = ActiveDirectory.getUser("ranvijay", context);
            System.out.println(user.getMail());
            System.out.println(user.getCommonName());
            System.out.println(user.toString());
            context.close();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
