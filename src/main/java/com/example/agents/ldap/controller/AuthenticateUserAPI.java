package com.example.agents.ldap.controller;

import com.example.agents.constant.Constant;
import com.example.agents.ldap.service.NonLdapUserService;
import com.example.agents.ldap.ActiveDirectory;
import com.example.agents.ldap.ActiveDirectory.User;
import com.example.agents.ldap.entities.NonLdapUser;
import com.example.agents.user.auth.model.UserBodyModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
public class AuthenticateUserAPI {


    @Autowired
    private NonLdapUserService nonLdapUserService;
    @RequestMapping(value="/authenticate")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> authenticateUser(@RequestBody UserBodyModel body) throws IOException {


        System.out.println(body.toString());
        User user = null;
        try {
//            Properties prop = new Properties();
//            InputStream input=AuthenticateUserAPI.class.getClassLoader().getResourceAsStream("com/example/agents/ldap/ldap.properties");
//            prop.load(input);
//            String flag = prop.getProperty("isLdap");
//
//			   if(cursor.size()==1)
//				   prop.setProperty("isLdap", "false");
//			   else
//				   prop.setProperty("isLdap", "true");
//
//			   flag=prop.getProperty("isLdap");
//
//			   System.out.println(prop.getProperty("isLdap"));


            if (Constant.flag.equals("true")) {
//                LdapContext context = ActiveDirectory.getConnection(body.getUserName(), body.getPassword(), prop.getProperty("DOMAIN"));
                LdapContext context = ActiveDirectory.getConnection(body.getUserName(), body.getPassword(), Constant.DOMAIN);
                user = ActiveDirectory.getUser(body.getUserName(), context);
//                System.out.println(prop.getProperty("DOMAIN"));
                System.out.println(Constant.DOMAIN);
                System.out.println(user.toString());
                System.out.println(user.getCommonName());
            }
            else {
                NonLdapUser dbUser = nonLdapUserService.findByUserNameAndPassword(body.getUserName(), body.getPassword());
                if (dbUser != null) {

//                    JSONObject role = new JSONObject(dbUser.getRole());
//                    if (role instanceof List) {
//                        List<String> roleArray = (List<String>) role;
//                        String[] roleStringArray = roleArray.toArray(new String[0]);
//                        user = ActiveDirectory.getUserr(body.getUserName(), roleStringArray);
//                        System.out.println(user.getRole().toString());

                    List<String> role = dbUser.getRole();
                    List<String> roleArray = (List<String>) role;
                       String[] roleStringArray = roleArray.toArray(new String[0]);
                    user = ActiveDirectory.getUserr(body.getUserName(), roleStringArray);
                    System.out.println(user.getRole().toString());
                    return new ResponseEntity<>(user, HttpStatus.OK);

                } else {

                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }

        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}

