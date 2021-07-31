package com.github.peacetrue.sample.ldap;

import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * @author : xiayx
 * @since : 2021-07-18 11:20
 **/
public abstract class LdapUtils {

    private static final String HOST = "192.168.150.27";
    private static final String PORT = "389";
    private static final String BASE_DN = "dc=peacetrue,dc=cn";

    public static List<String> findAllByJdk() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + HOST + ":" + PORT + "/" + BASE_DN);

        DirContext context = new InitialDirContext(env);
        NamingEnumeration<SearchResult> results = context.search("", "(objectClass=top)", getSearchControls());
        Stream<SearchResult> stream = toStream(results);
        List<String> list = stream.map(SearchResult::getAttributes).map(LdapUtils::formatGraceful).collect(Collectors.toList());
        results.close();
        context.close();
        return list;
    }

    private static SearchControls getSearchControls() {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return controls;
    }

    public static <T> Stream<T> toStream(Enumeration<T> enumeration) {
        EnumerationIterator<T> iterator = new EnumerationIterator<>(enumeration);
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false);
    }

    public static List<String> findAllBySpring(LdapTemplate ldapTemplate) {
        return ldapTemplate.search(
                LdapQueryBuilder.query().where("objectClass").is("top"),
                LdapUtils::formatGraceful
        );
    }

    public static String format(Attributes attributes) {
        EnumerationIterator<? extends Attribute> iterator = new EnumerationIterator<>(attributes.getAll());
        Spliterator<? extends Attribute> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false)
                .map(Attribute::toString)
                .filter(attr -> attr.length() <= 24)
                .collect(Collectors.joining("; "))
                ;
    }

    public static String formatGraceful(Attributes attributes) {
        final List<String> ids = Arrays.asList("dn", "ou", "cn", "dc");
        for (String id : ids) {
            final Attribute attribute = attributes.get(id);
            if (attribute == null) continue;
            return toStream(getValues(attribute)).map(Object::toString).collect(Collectors.joining(","));
        }
        throw new IllegalStateException(toStream(attributes.getIDs()).collect(Collectors.joining(",")));
    }

    private static NamingEnumeration<?> getValues(Attribute attribute) {
        try {
            return attribute.getAll();
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String formatById(Attributes attributes) {
        EnumerationIterator<String> iterator = new EnumerationIterator<>(attributes.getIDs());
        Spliterator<String> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT);
        return StreamSupport.stream(spliterator, false).collect(Collectors.joining("; "));
    }
}
