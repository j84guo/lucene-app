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

  @Test
  public void test1() {
    System.out.println("Test1 is running!");
  }
}
