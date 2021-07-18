package com.github.peacetrue.sample.ldap;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * @author : xiayx
 * @since : 2021-07-18 11:20
 **/
public interface LdapUtils {


    static List<String> findOrganizationalRoles() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://192.168.150.27:389/dc=peacetrue,dc=cn");

        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        List<String> list = new LinkedList<>();
        NamingEnumeration<SearchResult> results = null;
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", "(objectClass=organizationalRole)", controls);

            while (results.hasMore()) {
                SearchResult searchResult = results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute attr = attributes.get("cn");
                String cn = attr.get().toString();
                list.add(cn);
            }
        } catch (NameNotFoundException e) {
            // The base context was not found.
            // Just clean up and exit.
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        return list;
    }

    static List<String> findOrganizationalRoles(LdapTemplate ldapTemplate) {
        return ldapTemplate.search(
                query().where("objectClass").is("organizationalRole"),
                (AttributesMapper<String>) attrs -> attrs.get("cn").get().toString()
        );
    }
}
