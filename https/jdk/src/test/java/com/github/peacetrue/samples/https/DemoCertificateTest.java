package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@ActiveProfiles({"test", "demo"})
class DemoCertificateTest extends CertificateTest {

    /**
     * 异常链：
     * ResourceAccessException
     * javax.net.ssl.SSLHandshakeException
     * java.security.cert.CertificateException: No name matching localhost found
     * demo 证书 CN=UNKNOWN ，所以提示不匹配 localhost
     */
    @Test
    void nameMismatch() {
        assertThrows("No name matching localhost found");
    }

    /** 设置 trustStore 也无法解决 host 检查问题 */
    @Test
    void nameMismatchWhenSetKeyStore() throws Exception {
        buildStore("demo");
        resetSSL();
        assertThrows("No name matching localhost found");
    }

}
