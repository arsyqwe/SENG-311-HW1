import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite 2 - Search, update, delete and statistics")
@SelectClasses({
        SearchStudentTest.class,
        UpdateDeleteStudentTest.class,
        StatisticsStudentTest.class
})
public class SearchUpdateDeleteStatsSuite {
}