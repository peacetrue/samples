package com.github.peacetrue.samples.ks3;

import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.Ks3ClientConfig;
import com.ksyun.ks3.service.request.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author : xiayx
 * @since : 2021-02-08 10:23
 **/
@Slf4j
class Ks3Test {

    private Ks3 buildClient() {
        return new Ks3Client(
                "AKLTOWcmtrPUQ_Gu6xxgQPV0Hg",
                "ODZjp+i2xDusmBVVaCO8wyjmmxXHtIyry4WdXR9HlJuyyFmdKYC7kCUjd2NXQvdWKA==",
                new Ks3ClientConfig().withEndpoint("ks3-cn-beijing.ksyun.com")
        );
    }


    /* 5、存储空间(bucket)相关 */

    @Test
    void listBuckets() {
        Ks3 client = this.buildClient();
        List<Bucket> buckets = client.listBuckets();
        log.debug("buckets:\n{}", buckets);
        Assertions.assertTrue(buckets.size() > 0);
    }

    @Test
    void init() {
        Ks3 client = this.buildClient();
        Assertions.assertNotNull(client);
    }

    @Test
    void upload() {
        File file = new File("/Users/xiayx/Documents/Projects/samples/ks3/src/test/java/com/github/peacetrue/samplecase/ks3/Ks3Test.java");
        Ks3 client = this.buildClient();
        PutObjectRequest request = new PutObjectRequest("peace", "/test", file);
        PutObjectResult putObjectResult = client.putObject(request);
        log.debug("putObjectResult: {}", putObjectResult);
        Assertions.assertNotNull(putObjectResult);
    }

    @Test
    void downloadPrivate() throws Exception {
        Ks3 client = this.buildClient();
        String url = client.generatePresignedUrl("peace", "/test", 1000);
        Path downloadFile = Paths.get("/Users/xiayx/Documents/Projects/samples/ks3/src/test/resources/application-download.yml");
        Files.copy(new URL(url).openStream(), downloadFile);
        Assertions.assertTrue(Files.exists(downloadFile));
    }

    @Test
    void downloadPublic() throws Exception {
        String peace = MessageFormat.format("http://{0}.{1}/{2}", "peace", "ks3-cn-beijing.ksyun.com", "/test");
        log.debug("url: {}", peace);
        URL url = new URL(peace);
        Files.copy(url.openStream(), Paths.get("/Users/xiayx/Documents/Projects/samples/ks3/src/test/resources/application-download.yml"));
        Assertions.fail("");
    }
}
