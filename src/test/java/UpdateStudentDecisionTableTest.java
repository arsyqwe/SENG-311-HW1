import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// One test per rule (R1-R6) of the updateStudent decision table in the report.
public class UpdateStudentDecisionTableTest {
    private StudentOperations operations;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() throws Exception {
        operations = new StudentOperations();

        student1 = new Student("11111111111", "Ismail", "01.01.2006", 80.0);
        student2 = new Student("22222222222", "Eren", "01.01.2008", 85.0);

        operations.addStudent(student1);
        operations.addStudent(student2);
    }

    @Test
    @DisplayName("R1: searched PRN is invalid -> InvalidUpdateDataException")
    void rule1_searchedPrnInvalid() throws Exception {
        Student updated = new Student("11111111111", "New", "01.01.2006", 90.0);

        assertThrows(StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("123", updated));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }

    @Test
    @DisplayName("R2: new student's PRN is invalid -> InvalidUpdateDataException")
    void rule2_newPrnInvalid() throws Exception {
        Student updated = new Student("456", "New", "01.01.2006", 90.0);

        assertThrows(StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("11111111111", updated));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }

    @Test
    @DisplayName("R3: new marks are outside 0-100 -> InvalidUpdateDataException")
    void rule3_marksInvalid() throws Exception {
        Student updated = new Student("11111111111", "New", "01.01.2006", 150.0);

        assertThrows(StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("11111111111", updated));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }

    @Test
    @DisplayName("R4: searched PRN is not in the list -> StudentUpdateException (not found)")
    void rule4_searchedPrnNotInList() {
        Student updated = new Student("44444444444", "New", "01.01.2006", 90.0);

        StudentExceptions.StudentUpdateException exception = assertThrows(
                StudentExceptions.StudentUpdateException.class,
                () -> operations.updateStudent("44444444444", updated));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("R5: all valid, new PRN free -> student replaced in place")
    void rule5_validUpdate_replacesInPlace() throws Exception {
        Student updated = new Student("11111111111", "New", "01.01.2006", 90.0);

        operations.updateStudent("11111111111", updated);

        assertSame(updated, operations.searchByPosition(0));
    }

    // The existing test failed on this part last homework
    @Test
    @DisplayName("R6: new PRN belongs to another student -> StudentUpdateException (already exists)")
    void rule6_newPrnBelongsToAnother() {
        Student updated = new Student("22222222222", "New", "01.01.2006", 90.0);

        StudentExceptions.StudentUpdateException exception = assertThrows(
                StudentExceptions.StudentUpdateException.class,
                () -> operations.updateStudent("11111111111", updated));

        assertTrue(exception.getMessage().contains("already exists"));
    }
}
