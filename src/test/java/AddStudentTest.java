import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class AddStudentTest {
    private StudentOperations operations;

    @BeforeEach
    void setUp() {
        operations = new StudentOperations();
    }

    // 6
    @Test
    void addValidStudent_thenSearchByPrn() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);

        assertDoesNotThrow(() -> operations.addStudent(s));
        assertDoesNotThrow(() -> assertSame(s, operations.searchByPrn("49126067566")));
    }

    // 7
    @Test
    void addTwoStudents_verifyByPosition() throws Exception {
        Student s = new Student("12345678901", "Ismail", "01.01.2006", 80.5);
        Student s1 = new Student("23456789012", "Eren", "01.01.2008", 81.5);

        operations.addStudent(s);
        operations.addStudent(s1);

        assertAll("student positions",
                () -> assertSame(s, operations.searchByPosition(0)),
                () -> assertSame(s1, operations.searchByPosition(1)));
    }

    // 8 fail on purpose
    @Test
    void addDuplicatePrn_throwsDuplicatePRNException() throws Exception {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        Student s1 = new Student("49126067566", "Eren", "01.01.2008", 80.5);

        operations.addStudent(s);

        StudentExceptions.DuplicatePRNException exception = assertThrows(
                StudentExceptions.DuplicatePRNException.class,
                () -> operations.addStudent(s1));

        assertEquals("Student with PRN " + s.getPrn() + " already exists.", exception.getMessage());
    }

    // 9 fail on purpose
    @Test
    void addDuplicatePrn_listSizeAndMarksUnchanged() throws Exception {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        Student s1 = new Student("49126067566", "Ismail", "01.01.2006", 20.5);

        operations.addStudent(s);

        assertThrows(StudentExceptions.DuplicatePRNException.class, () -> operations.addStudent(s1));
        assertThrows(StudentExceptions.StudentNotFoundException.class, () -> operations.searchByPosition(1));
        assertEquals(80.5, operations.searchByPosition(0).getMarks(), 0.001);
    }

    // 10 (parameterized)
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123", "1234567890", "123456789012", "12345abc901", "12345 67890", "           "})
    void addStudent_withInvalidPrn_throwsInvalidStudentDataException(String invalidPrn) {
        Student student = new Student(invalidPrn, "Ismail", "01.01.2006", 85.0);

        assertThrows(StudentExceptions.InvalidStudentDataException.class,
                () -> operations.addStudent(student));
    }

    // 11
    @Test
    void addNegativeMarks_messageContainsExpected() {
        Student student = new Student("12345678901", "Ismail", "01.01.2006", -0.1);

        StudentExceptions.InvalidStudentDataException exception = assertThrows(
                StudentExceptions.InvalidStudentDataException.class,
                () -> operations.addStudent(student));

        assertTrue(exception.getMessage().contains("Invalid marks value"));
    }

    // 12
    @Test
    void addOver100Marks_messageEqualsExpected() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 102);

        StudentExceptions.InvalidStudentDataException exception = assertThrows(
                StudentExceptions.InvalidStudentDataException.class,
                () -> operations.addStudent(s));

        assertEquals("Invalid student data: Invalid marks value: " + s.getMarks(), exception.getMessage());
    }

    // 13
    @Test
    void addBoundaryMarks_doesNotThrow() {
        Student sMin = new Student("49126067566", "Ismail", "01.01.2006", 0);
        Student sMax = new Student("12345678901", "Eren", "01.01.2008", 100);

        assertAll("boundary marks",
                () -> assertDoesNotThrow(() -> operations.addStudent(sMin)),
                () -> assertDoesNotThrow(() -> operations.addStudent(sMax)));
    }

    // 14
    @Test
    void addNullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> operations.addStudent(null));
    }

    // 15
    @Test
    void addStudent_printsSuccessMessage() throws Exception {
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));

        try {
            Student student = new Student("12345678901", "Ismail", "01.01.2006", 85.0);
            operations.addStudent(student);

            assertTrue(outContent.toString().contains("Student added successfully"));
        } finally {
            System.setOut(originalOut);
        }
    }
}