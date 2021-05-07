package com.github.peacetrue.samples.hadoop;

import com.github.peacetrue.test.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : xiayx
 * @since : 2021-01-24 20:58
 **/
@Slf4j
public class HadoopTest {

    static {
//        System.setProperty("hadoop.home.dir", "/Users/xiayx/Documents/Projects/samples/docs/antora/modules/ROOT/attachment/hadoop-3.1.4");
    }

    public static final String LOCATION = "hdfs://hadoop-node01:9000";

    @Test
    void url() throws Exception {
        log.info("通过 URL 方式下载 HDFS 文件");

        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        String path = "/hadoop-root-namenode-hadoop-node01.log";
        InputStream inputStream = new URL(LOCATION + path).openStream();
        String pathname = TestUtils.getSourceFolderAbsolutePath(this.getClass()) + path;
        FileOutputStream outputStream = new FileOutputStream(pathname);
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);

        Assertions.assertTrue(Files.deleteIfExists(Paths.get(pathname)));
    }

    private FileSystem getFileSystem() throws IOException, URISyntaxException {
        FileSystem fileSystem = FileSystem.get(new URI(LOCATION), new Configuration());
        Assertions.assertNotNull(fileSystem);
        return fileSystem;
    }

    @Test
    void listFiles() throws URISyntaxException, IOException {
        log.info("获取文件列表");

        FileSystem fileSystem = getFileSystem();

        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            log.info("fileStatus: {}", fileStatus);
            Assertions.assertNotNull(fileStatus);
        }
    }

    @Test
    void mkdir() throws Exception {
        FileSystem fileSystem = getFileSystem();
        fileSystem.mkdirs(new Path("/peacetrue/hadoop"));
    }

    @Test
    void create() throws Exception {
        log.info("上传文件");

        FileSystem fileSystem = getFileSystem();
        String fileName = getClass().getSimpleName() + ".java";
        String uploadSourcePath = TestUtils.getSourceFolderAbsolutePath(getClass()) + "/" + fileName;
        fileSystem.copyFromLocalFile(new Path(uploadSourcePath), new Path("/"));

        Path downloadSourcePath = new Path("/" + fileName);
        String downloadDistPath = TestUtils.getSourceFolderAbsolutePath(getClass()) + "/Download.java";
        fileSystem.copyToLocalFile(downloadSourcePath, new Path(downloadDistPath));
    }

    @Test
    void createBigFile() throws Exception {
        log.info("上传文件");

        FileSystem fileSystem = getFileSystem();
        String fileName = "Adobe_Acrobat_DC_20.013.20064__macwk.com.dmg";
        fileSystem.copyFromLocalFile(new Path("/Users/xiayx/Downloads/" + fileName), new Path("/"));

        Path downloadSourcePath = new Path("/" + fileName);
        String downloadDistPath = TestUtils.getSourceFolderAbsolutePath(getClass()) + "/" + fileName;
        fileSystem.copyToLocalFile(downloadSourcePath, new Path(downloadDistPath));

        Assertions.assertTrue(Files.deleteIfExists(Paths.get(downloadDistPath)));
    }


}
