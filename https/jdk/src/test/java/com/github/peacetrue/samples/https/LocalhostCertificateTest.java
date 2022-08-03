package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@ActiveProfiles({"test", "localhost"})
class LocalhostCertificateTest extends CertificateTest {

    /**
     * 异常链：
     * ResourceAccessException
     * javax.net.ssl.SSLHandshakeException
     * sun.security.validator.ValidatorException: PKIX path building failed:
     * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
     * <p>
     * 在 JDK 证书库中找不到 localhost 条目
     */
    @Test
    void unableToFindValidCertificationPath() {
        assertThrows("unable to find valid certification path to requested target");
    }

    @Test
    void avoidUnableToFindValidCertificationPath() throws Exception {
        buildStore("localhost");
        resetSSL();
        assertSuccess("avoidUnableToFindValidCertificationPath");
    }

}
