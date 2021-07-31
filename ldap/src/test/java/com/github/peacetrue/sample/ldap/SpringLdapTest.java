package com.github.peacetrue.sample.ldap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author : xiayx
 * @since : 2021-07-18 11:39
 **/
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/ldap.xml")
class SpringLdapTest {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Test
    void basic() {
        List<String> organizationalRoles = LdapUtils.findAllBySpring(ldapTemplate);
        Assertions.assertTrue(organizationalRoles.contains("root"));
    }
}
