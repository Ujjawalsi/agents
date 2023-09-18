package com.example.agents.ldap;

import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;


@Component
public class SearchUserInAD
{
    static DirContext ldapContext;
    public static void main (String[] args) throws NamingException
    {
        SearchUserInAD ad = new SearchUserInAD();
        ad.searchUserInLDAP("anik.kumar");
//		ad.getConnection("ranvijay@velocis.in", "Velocis@2022");
        System.out.println();
    }

    public boolean authenticateUser(String username, String password) {
        boolean _flag = false;
        try {
            LdapContext context =  getConnection(username, password);
            _flag = true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return _flag;
    }


    @SuppressWarnings("unchecked")
    public LdapContext getConnection(String username, String password) throws NamingException {
        Hashtable props = new Hashtable();
        String principalName = username;
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        if (password!=null)
            props.put(Context.SECURITY_CREDENTIALS, password);
        try{
            Properties prop = new Properties();
            InputStream input=SearchUserInAD.class.getClassLoader().getResourceAsStream("com/example/agents/ldap/ldap.properties");
            prop.load(input);
            String ldapURL = prop.getProperty("PROVIDER_URL");;
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(Context.PROVIDER_URL, ldapURL);
            props.put(Context.SECURITY_AUTHENTICATION, "simple");
            return new InitialLdapContext(props, null);
        }
        catch(javax.naming.CommunicationException e){
            throw new NamingException("Failed to connect to ");
        }
        catch(NamingException e){
            throw new NamingException("Failed to authenticate " + username + "@");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String searchUserInLDAP(String searchUserName) {
        String secGroup = null;
        try
        {
            Properties prop = new Properties();
            InputStream input=SearchUserInAD.class.getClassLoader().getResourceAsStream("com/example/agents/ldap/ldap.properties");
            prop.load(input);
            String PROVIDER_URL= prop.getProperty("PROVIDER_URL");
            String SECURITY_PRINCIPAL= prop.getProperty("SECURITY_PRINCIPAL");
            String SECURITY_CREDENTIALS= prop.getProperty("SECURITY_CREDENTIALS");
            String SEARCH_BASE= prop.getProperty("SEARCH_BASE");
            System.out.println("Reading Active Directory");
            Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL,  PROVIDER_URL);
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapEnv.put(Context.SECURITY_PRINCIPAL, "ranvijay@velocis.in");
            ldapEnv.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
            //		ldapEnv.put(Context.REFERRAL, "follow");
            ldapContext = new InitialDirContext(ldapEnv);

            // Create the search controls
            SearchControls searchCtls = new SearchControls();
            //Specify the attributes to return
            String returnedAtts[]={"sn","cn","email","givenName", "samAccountName","memberOf"};
            searchCtls.setReturningAttributes(returnedAtts);
            //Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //specify the LDAP search filter
            //String searchFilter = "(&(objectClass=user))";
            String searchFilter = "(&(objectClass=user)(sAMAccountName=" + searchUserName + "))";
            //Specify the Base for the search
            String searchBase = SEARCH_BASE;
            //initialize counter to total the results
            int totalResults = 0;
            // Search for objects using the filter
            NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();
                totalResults++;
                Attributes attrs = sr.getAttributes();
                System.err.println(">>>>>>" + attrs.get("cn").get());
//				System.err.println(">>>>>>" + attrs.get("sn").get());
                //			  System.out.println(">>>>>>" + attrs.get("memberOf"));
                NamingEnumeration<?> enum1 = attrs.get("memberOf").getAll();
                while (enum1.hasMoreElements()) {
                    secGroup = (String) enum1.nextElement();
                    System.out.println("secGroup:: "+secGroup);
                    return secGroup;

                }
            }

            System.out.println("Total results: " + totalResults);
            ldapContext.close();
        }
        catch (Exception e)
        {
            System.out.println(" Search error: " + e);
            e.printStackTrace();
            //		  System.exit(-1);
        }
        return secGroup;
    }
}