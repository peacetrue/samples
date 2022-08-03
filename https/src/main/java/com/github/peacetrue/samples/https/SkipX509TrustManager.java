package com.github.peacetrue.samples.https;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @author : xiayx
 * @since : 2021-08-21 07:33
 **/
public class SkipX509TrustManager implements X509TrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

}
