package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.ResourceAccessException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 抽象类，防止执行测试方法
 *
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class CertificateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    private static Map<Object, Object> properties = new HashMap<>(System.getProperties());

    @BeforeAll
    static void beforeAll() {
        System.setProperty("javax.net.debug", "ssl,handshake");
    }

    @AfterEach
    void tearDown() throws Exception {
        resetProperties();
        resetSSL();
    }

    protected void resetProperties() {
        Properties properties = new Properties();
        properties.putAll(CertificateTest.properties);
        System.setProperties(properties);
    }

    protected void resetSSL() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        HttpsURLConnection.setDefaultHostnameVerifier(defaultHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    protected String echo(String message) {
        return restTemplate.getForObject("/echo?message={0}", String.class, message);
    }

    protected void assertSuccess(String message) {
        String input = "ok";
        String result = echo(input);
        Assertions.assertEquals(input, result, message);
    }

    protected void assertThrows(String message) {
        ResourceAccessException exception = Assertions.assertThrows(
                ResourceAccessException.class,
                () -> echo("ex"),
                message
        );
        Assertions.assertTrue(Objects.requireNonNull(exception.getMessage()).contains(message));
    }

    protected Properties buildStore(String storeName) {
        Properties properties = new Properties();
        String classpath = findClasspathAbsolute();
        properties.setProperty("javax.net.ssl.keyStore", String.format("%s/%s-key.jks", classpath, storeName));
        properties.setProperty("javax.net.ssl.keyStorePassword", "peacetrue");
        properties.setProperty("javax.net.ssl.trustStore", String.format("%s/%s-trust.jks", classpath, storeName));
        properties.setProperty("javax.net.ssl.trustStorePassword", "peacetrue");
        properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
        return properties;
    }

    private String findClasspathAbsolute() {
        String[] classpathList = System.getProperty("java.class.path").split(File.pathSeparator);
        return Stream.of(classpathList)
                .filter(item -> item.endsWith("resources/main"))
                .findFirst().orElseThrow(() -> new IllegalStateException("classpath not found"));
    }

    @Test
    public void skipSslVerification() throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(new SkipHostnameVerifier());
        HttpsURLConnection.setDefaultSSLSocketFactory(SSLContextUtils.createSslSocketFactory());
        assertSuccess("skipSslVerification");
    }
}
