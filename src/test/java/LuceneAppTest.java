import java.util.List;
import org.apache.lucene.document.Document;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;


public class LuceneAppTest {
  @BeforeSuite
  public void setupSuite() {
    System.out.println("@BeforeSuite");
  }

  @AfterSuite
  public void cleanupSuite() {
    System.out.println("@AfterSuite");
  }

  @BeforeClass
  public void setupClass() {
    System.out.println("@BeforeClass");
  }

  @AfterClass
  public void cleanupClass() {
    System.out.println("@AfterClass");
  }

  @BeforeMethod
  public void setupMethod() {
    System.out.println("@BeforeMethod");
  }

  @AfterMethod
  public void cleanupMethod() {
    System.out.println("@AfterMethod");
  }

  // assume default docs contain foo:bar
  @Test
  public void testDefaultDocuments() throws Exception {
    LuceneApp app = new LuceneApp();
    List<Document> res = app.search("body", "bar");
    Assert.assertEquals(1, res.size());
    Assert.assertEquals("foo", res.get(0).get("title"));
  }
}
