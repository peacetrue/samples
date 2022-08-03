package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@ActiveProfiles({"test", "server", "client-auth"})
class ClientCertificateNeedClientAuthTest extends CertificateTest {


    /**
     * @see org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption#SSL
     */
    @Test
    void basic() {
        //javax.net.ssl.SSLHandshakeException: Received fatal alert: certificate_unknown
        assertSuccess("basic");
    }

}
