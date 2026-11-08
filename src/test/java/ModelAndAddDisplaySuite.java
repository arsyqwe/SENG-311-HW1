import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite 1 - Student model, add and display")
@SelectClasses({
        StudentModelTest.class,
        AddStudentTest.class,
        DisplayStudentsTest.class,
        StudentListIntegrityTest.class
})
public class ModelAndAddDisplaySuite {
}