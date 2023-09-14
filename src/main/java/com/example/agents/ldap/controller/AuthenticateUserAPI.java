package com.example.agents.ldap.controller;
import com.example.agents.ldap.service.NonLdapUserService;
import com.example.agents.ldap.ActiveDirectory;
import com.example.agents.ldap.ActiveDirectory.User;
import com.example.agents.ldap.entities.NonLdapUser;
import com.example.agents.user.auth.model.UserBodyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
public class AuthenticateUserAPI {

    @Value("${user.flag}")
    private String userFlag;

    @Value("${domain.name}")
    private String domainName;


    @Autowired
    private NonLdapUserService nonLdapUserService;

    @RequestMapping(value="/authenticate")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> authenticateUser(@RequestBody UserBodyModel body) throws IOException {

        System.out.println(body.toString());
        User user = null;
        try {
            if (userFlag.equals("true")) {
                LdapContext context = ActiveDirectory.getConnection(body.getUserName(), body.getPassword(), domainName);
                user = ActiveDirectory.getUser(body.getUserName(), context);
            }
            else {
                NonLdapUser dbUser = nonLdapUserService.findByUserNameAndPassword(body.getUserName(), body.getPassword());
                if (dbUser != null) {
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

