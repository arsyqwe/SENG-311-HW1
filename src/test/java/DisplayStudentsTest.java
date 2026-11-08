import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayStudentsTest {
    private StudentOperations operations;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        operations = new StudentOperations();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    // 16
    @Test
    void displayStudents_emptyList_throwsException() {
        StudentExceptions.EmptyStudentListException exception = assertThrows(
                StudentExceptions.EmptyStudentListException.class,
                () -> operations.displayStudents());

        assertEquals("No students available in the system.", exception.getMessage());
    }

    // 17
    @Test
    void displayStudents_singleStudent_printsCorrectFormat() throws Exception {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80);
        operations.addStudent(s);
        outContent.reset();

        operations.displayStudents();

        String expectedOutput = "Position 0: " + s + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    // 18
    @Test
    void displayStudents_multipleStudents_printsAllPositions() throws Exception {
        Student s0 = new Student("12345678901", "Ismail", "01.01.2006", 85.0);
        Student s1 = new Student("12345678902", "Eren", "01.01.2008", 90.0);
        Student s2 = new Student("12345678903", "Ahmet", "02.02.2005", 75.0);

        operations.addStudent(s0);
        operations.addStudent(s1);
        operations.addStudent(s2);
        outContent.reset();

        operations.displayStudents();
        String output = outContent.toString();

        assertAll("multiple student output",
                () -> assertTrue(output.contains("Position 0: " + s0)),
                () -> assertTrue(output.contains("Position 1: " + s1)),
                () -> assertTrue(output.contains("Position 2: " + s2)));
    }

    // 79
    @Test
    @DisplayName("A failed add prints no success message")
    void addStudent_failed_printsNothing() throws Exception {
        operations.addStudent(new Student("11111111111", "Ismail", "01.01.2006", 80.0));
        outContent.reset();

        assertAll("failed adds",
                () -> assertThrows(StudentExceptions.DuplicatePRNException.class,
                        () -> operations.addStudent(new Student("11111111111", "Eren", "01.01.2008", 85.0))),
                () -> assertThrows(StudentExceptions.InvalidStudentDataException.class,
                        () -> operations.addStudent(new Student("123", "Ahmet", "01.01.2005", 90.0))),
                () -> assertEquals("", outContent.toString()));
    }
}