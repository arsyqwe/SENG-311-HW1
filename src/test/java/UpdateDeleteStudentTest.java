import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateDeleteStudentTest {
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

    // 33
    @Test
    void updateStudent_existingPrn_replacesStudent() throws Exception {
        Student updated = new Student("22222222222", "Eren Updated", "01.01.2008", 95.0);

        operations.updateStudent("22222222222", updated);

        Student found = operations.searchByPrn("22222222222");
        assertAll("updated student",
                () -> assertNotNull(found),
                () -> assertSame(updated, found),
                () -> assertNotSame(student2, found),
                () -> assertEquals("Eren Updated", found.getName()),
                () -> assertEquals(95.0, found.getMarks(), 0.001));
    }

    // 34
    @Test
    void updateStudent_nonExistingPrn_throwsStudentUpdateException() {
        Student updated = new Student("44444444444", "Nobody", "01.01.2000", 50.0);

        StudentExceptions.StudentUpdateException exception = assertThrows(
                StudentExceptions.StudentUpdateException.class,
                () -> operations.updateStudent("44444444444", updated));

        assertEquals("Failed to update student: Student with PRN 44444444444 not found",
                exception.getMessage());
    }

    // 35
    @Test
    void updateStudent_invalidMarks_throwsAndKeepsOriginal() throws Exception {
        Student invalid = new Student("22222222222", "Eren", "01.01.2008", 150.0);

        StudentExceptions.InvalidUpdateDataException exception = assertThrows(
                StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("22222222222", invalid));

        assertTrue(exception.getMessage().startsWith("Invalid update data: Invalid marks value"));
        assertSame(student2, operations.searchByPrn("22222222222"));
    }

    // 36
    @Test
    void deleteStudent_existingPrn_removesAndShiftsPositions() throws Exception {
        operations.deleteStudent("22222222222");

        assertAll("after delete",
                () -> assertThrows(StudentExceptions.StudentNotFoundException.class,
                        () -> operations.searchByPrn("22222222222")),
                () -> assertSame(student3, operations.searchByPosition(1)),
                () -> assertThrows(StudentExceptions.StudentNotFoundException.class,
                        () -> operations.searchByPosition(2)));
    }

    // 37
    @Test
    void deleteStudent_nonExistingPrn_throwsStudentDeletionException() {
        StudentExceptions.StudentDeletionException exception = assertThrows(
                StudentExceptions.StudentDeletionException.class,
                () -> operations.deleteStudent("44444444444"));

        assertEquals("Failed to delete student: Student with PRN 44444444444 not found",
                exception.getMessage());
    }
}