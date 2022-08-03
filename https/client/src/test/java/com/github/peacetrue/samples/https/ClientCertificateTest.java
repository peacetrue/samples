package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@ActiveProfiles({"test", "server"})
class ClientCertificateTest extends CertificateTest {

    /**
     * @see org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption#SSL
     */
    @Test
    void trustSelfSignedStrategy() {
        assertSuccess("trustSelfSignedStrategy");
    }

}
