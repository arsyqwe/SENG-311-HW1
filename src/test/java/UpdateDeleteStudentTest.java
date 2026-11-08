import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    @Tag("pit")
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
    
    // 60
    @Test
    @DisplayName("An update checks for conflicts with existing PRNs")
    void updateStudent_existingPrnAndNonEqualPrn() throws Exception {
        Student s = new Student("22222222222", "Ismail", "01.01.2006", 80.0);
        
        StudentExceptions.StudentUpdateException exception = assertThrows(
            StudentExceptions.StudentUpdateException.class,
            () -> operations.updateStudent("11111111111", s));
            
        assertTrue(exception.getMessage().contains("Student with PRN 22222222222 already exists."));
    }
    
    // 61
    @Test
    @DisplayName("Updating a student does not shift its position")
    void updateStudent_existingPrn_doesNotShiftPosition() throws Exception {
        Student updated = new Student("22222222222", "Eren Updated", "01.01.2008", 95.0);
        operations.updateStudent("22222222222", updated);
        assertSame(updated, operations.searchByPosition(1));
    }
    
    //62
    @Test
    @DisplayName("An updated student with an invalid PRN is rejected and the original is kept")
    void updateStudent_updatedStudentInvalidPrn_throwsAndKeepsOriginal() throws Exception {
        Student updated = new Student("123", "Ismail Updated", "01.01.2006", 80.0);

        StudentExceptions.InvalidUpdateDataException exception = assertThrows(
                StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("11111111111", updated));

        // The message names "123", so it was the new student's PRN that failed, not the one we searched with
        assertTrue(exception.getMessage().contains("Invalid PRN format: 123"));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }
    
    //63
    @Test
    @DisplayName("Passing null as student throws update exception")
    void updateStudent_nullStudent_throwsUpdateException() {
        assertThrows(
                StudentExceptions.InvalidUpdateDataException.class, 
                () -> operations.updateStudent("11111111111", null));
    }
    
    // 64
    @Test
    @DisplayName("Invalid PRN while updating student throws update exception")
    void updateStudent_invalidPrn_throwsUpdateException() {
        Student updated = new Student("11111111111", "Ismail Updated", "01.01.2006", 80.0);

        StudentExceptions.InvalidUpdateDataException exception = assertThrows(
                StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("1", updated));
        
        assertTrue(exception.getMessage().contains("Invalid PRN format"));
    }
    
    // 65 (parameterized)
    @ParameterizedTest
    @DisplayName("Marks in the boundary do not throw exceptions for updating student")
    @ValueSource(doubles = {0, 100})
    void updateStudent_boundaryMarks_doesNotThrow(double boundaryMarks) throws Exception {
        Student updated = new Student("22222222222", "Eren", "01.01.2008", boundaryMarks);

        assertDoesNotThrow(() -> operations.updateStudent("22222222222", updated));
        assertEquals(boundaryMarks, operations.searchByPrn("22222222222").getMarks(), 0.001);
    }
    
    // 74
    @Test
    @DisplayName("First error to be thrown when a student is invalid and unknown is invalidity")
    void updateInvalidAndNonExistingStudent_throwsInvalidUpdateDataException(){
        Student student1 = new Student("44444444444", "A", "01.01.2002", 150.0);
        
        StudentExceptions.InvalidUpdateDataException exception = assertThrows(
                StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("44444444444", student1));

        assertTrue(exception.getMessage().contains("Invalid marks value"));
    }

    // 80
    @Test
    @DisplayName("Updating a student to an unused PRN moves them to that PRN in place")
    void updateStudent_toUnusedPrn_oldPrnGoneNewPrnFindable() throws Exception {
        Student updated = new Student("44444444444", "Eren", "01.01.2008", 85.0);

        operations.updateStudent("22222222222", updated);

        assertAll("PRN change",
                () -> assertSame(updated, operations.searchByPrn("44444444444")),
                () -> assertThrows(StudentExceptions.StudentNotFoundException.class,
                        () -> operations.searchByPrn("22222222222")),
                () -> assertSame(updated, operations.searchByPosition(1)));
    }
    
    // --

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
    
    // 66
    @Test
    @DisplayName("Having an invalid PRN sought for deletion also throws exception for invalidity")
    void deleteStudent_invalidPrn_throwsInvalidPrnException() {
        StudentExceptions.InvalidPRNException exception = assertThrows(
                StudentExceptions.InvalidPRNException.class, 
                () -> operations.deleteStudent("123")
        );
        
        assertTrue(exception.getMessage().contains("Invalid PRN format"));
    }

    // 78
    @Test
    @DisplayName("A deleted PRN can be reused by a new student")
    void deleteStudent_thenReAddSamePrn_newStudentIsFindable() throws Exception {
        operations.deleteStudent("22222222222");
        Student newcomer = new Student("22222222222", "Zehra", "01.01.2002", 70.0);

        assertDoesNotThrow(() -> operations.addStudent(newcomer));

        Student found = operations.searchByPrn("22222222222");
        assertAll("re-added student",
                () -> assertSame(newcomer, found),
                () -> assertNotSame(student2, found));
    }
}