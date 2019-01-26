import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneApp {

    private Directory index;
    private Analyzer analyzer;
    private IndexWriterConfig indexWriterConfig;
    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    private boolean shouldResetReader = false;

    private String[][] defaultDocuments = {
        {"foo", "bar"},
        {"go", "lang"},
        {"alice", "bob"},
        {"fire", "truck"}
    };

    public LuceneApp() throws IOException {
        initIndex();
        initWriter();
        addDefaultDocuments();
    }

    private void initIndex() throws IOException {
        index = new RAMDirectory();
        analyzer = new StandardAnalyzer();
    }

    private void initWriter() throws IOException {
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(index, indexWriterConfig);
    }

    private void initReader() throws IOException {
        indexReader = DirectoryReader.open(index);
        indexSearcher = new IndexSearcher(indexReader);
    }

    private void resetReader() throws IOException {
        closeReaderIfOpen();
        initReader();
    }

    private void addDefaultDocuments() throws IOException {
        for (String document[] : defaultDocuments) {
            addDocument(document[0], document[1]);
        }
        commitWriter();
    }

    // assume all documents have title and body
    public void addDocument(String title, String body) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("body", body, Field.Store.YES));
        indexWriter.addDocument(doc);
    }

    public void commitWriter() throws IOException {
        indexWriter.commit();
        closeReaderIfOpen();
        shouldResetReader = true;
    }

    private void closeReaderIfOpen() throws IOException {
        if (indexReader != null) {
            indexReader.close();
        }
    }

    // we provide a way to close both the reader and writer quietly
    public void closeQuietly() {
        try {
            indexReader.close();
        } catch (IOException e) {}

        try {
            closeReaderIfOpen();
        } catch (IOException e) {}
    }

    public List<Document> search(String field, String queryText) throws ParseException, IOException {
        if (shouldResetReader) {
            resetReader();
            shouldResetReader = false;
        }

        Query query = new QueryParser(field, analyzer)
            .parse(queryText);
        TopDocs topDocs = indexSearcher.search(query, 10);

        List<Document> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            results.add(indexSearcher.doc(scoreDoc.doc));
        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        LuceneApp app = new LuceneApp();
        System.out.println(app.search("body", "bar"));
    }
}
