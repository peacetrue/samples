package com.github.peacetrue.sample.ldap;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ldap.core.LdapTemplate;

import java.util.List;

/**
 * @author : xiayx
 * @see <a href="https://docs.spring.io/spring-ldap/docs/current/reference/">Spring LDAP Reference 2.3.4.RELEASE</a>
 * @since : 2021-07-17 14:26
 **/
@Slf4j
class LdapTest {

    /** 使用 JDK 自带的目录服务 */
    @Test
    void jdk() {
        List<String> organizationalRoles = LdapUtils.findOrganizationalRoles();
        Assertions.assertTrue(organizationalRoles.contains("root"));
    }

    /** 使用 Spring 提供的目录服务 */
    @Test
    void spring() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("/ldap.xml"));
        LdapTemplate ldapTemplate = beanFactory.getBean(LdapTemplate.class);
        List<String> organizationalRoles = LdapUtils.findOrganizationalRoles(ldapTemplate);
        Assertions.assertTrue(organizationalRoles.contains("root"));
    }
}
