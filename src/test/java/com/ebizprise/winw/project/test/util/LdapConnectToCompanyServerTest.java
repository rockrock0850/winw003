package com.ebizprise.winw.project.test.util;

import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.io.IOException;
import java.util.Hashtable;

public class LdapConnectToCompanyServerTest {
    private String ldapUrl;
    private String ldapDc;

    @Test
    public void testLoginAD() throws IOException {
        this.loginLdap(this.getLdapContext("Builder", "builder"), "Builder");
    }

    private void loginLdap(DirContext ctx, String userName) {
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            // NOTE: The attributes mentioned in array below are the ones that
            // will be retrieved, you can add more.
            String[] attrIDs = {"distinguishedName", "sn", "givenname", "mail", "telephonenumber", "canonicalName",
                    "userAccountControl", "accountExpires", "cn", "ou", "memberOf", "ROCID"};
            constraints.setReturningAttributes(attrIDs);
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<?> answer = ctx.search("OU=eBizprise,DC=ebizprise,DC=corp", "(&(objectClass=user))", searchControls);

            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println(attrs.get("distinguishedName"));
            } else {
                throw new Exception("Invalid User");
            }


            NamingEnumeration<?> answer2 = ctx.search("OU=eBizprise,DC=ebizprise,DC=corp", "(&(objectClass=group))", searchControls);
            while (answer2.hasMore()) {
                SearchResult rslt = (SearchResult) answer2.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println(attrs.get("name"));
                System.out.println(attrs.get("distinguishedName"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private DirContext getLdapContext(String userName, String password) {
        DirContext ctx = null;
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");

            // NOTE: replace user@domain.com with a User that is present in your
            // Active Directory/LDAP
            env.put(Context.SECURITY_PRINCIPAL, userName);
            // NOTE: replace userpass with passwd of this user.
            env.put(Context.SECURITY_CREDENTIALS, password);
            // NOTE: replace ADorLDAPHost with your Active Directory/LDAP
            // Hostname or IP.
            env.put(Context.PROVIDER_URL, "ldap://192.168.128.68:389/");
            env.put(Context.REFERRAL, "follow");

            System.out.println("Attempting to Connect...");

            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful.");
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapDc() {
        return ldapDc;
    }

    public void setLdapDc(String ldapDc) {
        this.ldapDc = ldapDc;
    }
}