package com.github.peacetrue.samples.https;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author : xiayx
 * @since : 2021-08-22 08:49
 **/
public abstract class SSLContextUtils {

    protected SSLContextUtils() {
    }

    public static SSLSocketFactory createSslSocketFactory() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new SkipX509TrustManager()}, new SecureRandom());
        return context.getSocketFactory();
    }

    public static String findClasspathAbsolute() {
        String[] classpathList = System.getProperty("java.class.path").split(File.pathSeparator);
        return Stream.of(classpathList)
                .filter(item -> item.endsWith("resources/main"))
                .findFirst().orElseThrow(() -> new IllegalStateException("classpath not found"));
    }

    public static Properties buildKeyStore(String keyStoreName) {
        Properties properties = new Properties();
        String classpath = findClasspathAbsolute();
        String keyStorePath = String.format("%s/%s.jks", classpath, keyStoreName);
        properties.setProperty("javax.net.ssl.keyStore", keyStorePath);
        properties.setProperty("javax.net.ssl.keyStorePassword", "peacetrue");
        properties.setProperty("javax.net.ssl.trustStore", keyStorePath);
        properties.setProperty("javax.net.ssl.trustStorePassword", "peacetrue");
        properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
        return properties;
    }
}
