package com.github.peacetrue.samples.https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author : xiayx
 * @since : 2021-08-21 07:32
 **/
public class SkipHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        return true;
    }

}
