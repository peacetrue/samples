package com.github.peacetrue.samples.lucene;

import com.github.peacetrue.test.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author : xiayx
 * @since : 2021-02-27 12:30
 **/
@Slf4j
public class LuceneTest {

    @Test
    void createIndex() throws IOException {
        FileSystemUtils.deleteRecursively(buildPath());
        IndexWriter indexWriter = new IndexWriter(
                buildDirectory(),
                new IndexWriterConfig(new SmartChineseAnalyzer())
        );
        indexWriter.addDocument(buildArticle01());
        indexWriter.addDocument(buildArticle02());
        indexWriter.commit();
        Assertions.assertEquals(2, indexWriter.getDocStats().numDocs);
        indexWriter.close();
    }

    private Path buildPath() {
        return Paths.get(TestUtils.getSourceFolderAbsolutePath(LuceneTest.class)).resolve("article-index");
    }

    private FSDirectory buildDirectory() throws IOException {
        return FSDirectory.open(buildPath());
    }

    public static Document buildArticle01() {
        Document document = new Document();
        document.add(new NumericDocValuesField("id", 1L));
        document.add(new TextField("title", "华为手机详细教程", Field.Store.YES));
        document.add(new TextField("content", "华为手机是一种高端手机，价格昂贵", Field.Store.YES));
        return document;
    }

    public static Document buildArticle02() {
        Document document = new Document();
        document.add(new NumericDocValuesField("id", 2L));
        document.add(new TextField("title", "小米手机原理教程", Field.Store.YES));
        document.add(new TextField("content", "小米手机是一种低端手机，价格便宜", Field.Store.YES));
        return document;
    }

    @Test
    void searchIndex() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(buildDirectory());
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        Query query = new QueryParser("title", new SmartChineseAnalyzer()).parse("手机教程");
        TopDocs topDocs = indexSearcher.search(query, 10);
        Assertions.assertEquals(2, topDocs.scoreDocs.length);
        topDocs = indexSearcher.search(new TermQuery(new Term("content", "小米")), 10);
        Assertions.assertEquals(1, topDocs.scoreDocs.length);
        directoryReader.close();
    }


}
