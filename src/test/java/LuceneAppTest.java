import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.BytesRef;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LuceneAppTest {

  private LuceneApp app;

  @BeforeClass
  public void setupClass() throws Exception {
    app = new LuceneApp();
  }

  @AfterClass
  public void cleanupClass() {
    app.closeQuietly();
  }

  // assume default docs contain foo:bar
  @Test
  public void testDefaultDocuments() throws Exception {
    LuceneApp app = new LuceneApp();
    List<Document> res = app.search("body", "bar");
    Assert.assertEquals(1, res.size());
    Assert.assertEquals(res.get(0).get("title"), "foo");
  }

  // term query - field and value
  @Test
  public void testTermQuery() throws Exception {
    Term term = new Term("title", "go");
    TermQuery query = new TermQuery(term);
    List<Document> res = app.search(query);
    Assert.assertEquals(1, res.size());
    Assert.assertEquals(res.get(0).getValues("body")[0], "lang");
  }

  // prefix query - search for documents by prefix
  @Test
  public void testPrefixQuery() throws Exception {
    Term term = new Term("title", "fir");
    PrefixQuery query = new PrefixQuery(term);
    List<Document> res = app.search(query);
    Assert.assertEquals(res.get(0).getValues("body")[0], "truck");
  }

  // wildcard query - * could be an arbitrary string
  @Test
  public void testWildcardQuery() throws Exception {
    Term term = new Term("title", "fir*");
    WildcardQuery query = new WildcardQuery(term);
    List<Document> res = app.search(query);
    Assert.assertEquals(res.get(0).getValues("body")[0], "truck");
  }

  // phrase query - search by a sequence of strings
  // the first int argument is the distance in words the texts can be separated by
  @Test
  public void testPhraseQuery() throws Exception {
    PhraseQuery query = new PhraseQuery(
        10,
        "body",
        new BytesRef("wonder"),
        new BytesRef("land")
    );
    List<Document> res = app.search(query);
    Assert.assertEquals(res.size(), 1);
  }

  // fuzzy query - search for a term which is similar, but not identical
  @Test
  public void testFuzzyQuery() throws Exception {
    Term term = new Term("body", "WOnder");
    FuzzyQuery query = new FuzzyQuery(term);
    List<Document> res = app.search(query);
    Assert.assertEquals(res.size(), 1);
  }

  @Test
  public void testBooleanQuery() throws Exception  {
    Term term1 = new Term("body", "wonder");
    Term term2 = new Term("body", "land");

    TermQuery query1 = new TermQuery(term1);
    TermQuery query2 = new TermQuery(term2);

    BooleanQuery booleanQuery = new BooleanQuery.Builder()
        .add(query1, BooleanClause.Occur.MUST)
        .add(query2, BooleanClause.Occur.MUST)
        .build();

    List<Document> res = app.search(booleanQuery);
    System.out.println(res);
  }
}
