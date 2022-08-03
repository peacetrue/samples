/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.peacetrue.samples.https;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * {@link SimpleClientHttpRequestFactory} that skips SSL certificate verification.
 *
 * @author Madhura Bhave
 */
@Slf4j
public class SkipSslVerificationHttpRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if (connection instanceof HttpsURLConnection) {
            prepareHttpsConnection((HttpsURLConnection) connection);
        }
        super.prepareConnection(connection, httpMethod);
    }

    private void prepareHttpsConnection(HttpsURLConnection connection) {
        connection.setHostnameVerifier(new SkipHostnameVerifier());
        try {
            connection.setSSLSocketFactory(SSLContextUtils.createSslSocketFactory());
        } catch (Exception ex) {
            log.error("create ssl socket factory error", ex);
        }
    }

}
