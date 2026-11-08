import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchStudentTest {
    private StudentOperations operations;
    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    void setUp() throws Exception {
        operations = new StudentOperations();

        student1 = new Student("11111111111", "Ismail", "01.01.2006", 80.0);
        student2 = new Student("22222222222", "Eren", "01.01.2008", 85.0);
        student3 = new Student("33333333333", "Ahmet", "01.01.2005", 90.0);

        operations.addStudent(student1);
        operations.addStudent(student2);
        operations.addStudent(student3);
    }

    // 19
    @Test
    void searchByPrn_lastStudent_returnsCorrectName() throws Exception {
        Student found = operations.searchByPrn("33333333333");

        assertEquals("Ahmet", found.getName());
    }

    // 20
    @Test
    void searchByPrn_validPrnNotInList_throwsStudentNotFoundException() {
        String missingPrn = "44444444444";

        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByPrn(missingPrn));

        assertEquals("Student not found: PRN: " + missingPrn, exception.getMessage());
    }

    // 21
    @Test
    void searchByPrn_invalidPrnFormat_throwsInvalidSearchCriteriaException() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPrn("123"));

        assertTrue(exception.getMessage().startsWith("Invalid search criteria: Invalid PRN format"));
    }

    // 22
    @Test
    void searchByPrn_nullPrn_throwsInvalidSearchCriteriaException() {
        assertThrows(StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPrn(null));
    }

    // 23
    @Test
    void searchByPrn_emptyRepository_throwsStudentNotFoundException() {
        StudentOperations emptyOperations = new StudentOperations();

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> emptyOperations.searchByPrn("11111111111"));
    }

    // 24
    @Test
    void searchByName_exactName_returnsSameObject() throws Exception {
        Student found = operations.searchByName("Ismail");

        assertSame(student1, found);//beforeEachde 3 tane öğrenci koyulduğu için patlamıyor.
    }

    // 25
    @Test
    void searchByName_lowerCase_findsStudentAndChecksPrn() throws Exception {
        Student s4 = new Student("44444444444", "Emre", "01.01.2000", 77);
        operations.addStudent(s4);

        Student found = operations.searchByName("emre");

        assertEquals("44444444444", found.getPrn());
    }

    // 26
    @Test
    void searchByName_nonExistingName_throwsExceptionAndMessage() {
        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByName("Mahmut"));

        assertEquals("Student not found: Name: Mahmut", exception.getMessage());
    }

    // 27
    @Test
    void searchByName_nullName_throwsExceptionAndMessage() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByName(null));

        assertEquals("Invalid search criteria: Name cannot be empty", exception.getMessage());
    }

    // 28
    @Test
    void searchByName_partialName_throwsStudentNotFoundException() throws Exception {
        Student s4 = new Student("99999999999", "Ali Haydar", "01.01.2009", 21);
        operations.addStudent(s4);

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByName("Ali"));
    }

    // 29
    @Test
    void searchByPosition_zero_returnsFirstStudent() throws Exception {
        Student found = operations.searchByPosition(0);

        assertSame(student1, found);
    }

    // 30
    @Test
    void searchByPosition_negative_throwsExceptionAndMessage() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPosition(-1));

        assertEquals("Invalid search criteria: Position cannot be negative", exception.getMessage());
    }

    // 31
    @Test
    void searchByPosition_equalsListSize_throwsExceptionAndMessage() {
        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByPosition(3));

        assertEquals("Student not found: Position: 3", exception.getMessage());
    }

    // 32
    @Test
    void searchByPosition_emptyRepo_throwsException() {
        StudentOperations emptyOperations = new StudentOperations();

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> emptyOperations.searchByPosition(0));
    }
}