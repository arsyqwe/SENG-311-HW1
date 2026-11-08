import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
public class UpdateStudentBasisPathTest {
 
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
 
    // P1: 3-7-16 (prn parametresi geçersiz)
    @Test
    void path1_invalidPrnParameter_throwsInvalidUpdateData() {
        Student updated = new Student("11111111111", "Ismail", "01.01.2006", 90.0);
 
        assertThrows(StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("123", updated));
    }
 
    // P2: 3-4-7-16 (updatedStudent'in prn'i geçersiz)
    @Test
    void path2_invalidUpdatedStudentPrn_throwsInvalidUpdateData() {
        Student updated = new Student("123", "Ismail", "01.01.2006", 90.0);
 
        assertThrows(StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("11111111111", updated));
    }
 
    // P3: 3-4-5-7-16 (marks geçersiz)
    @Test
    void path3_invalidMarks_throwsInvalidUpdateData() throws Exception {
        Student updated = new Student("11111111111", "Ismail", "01.01.2006", 150.0);
 
        StudentExceptions.InvalidUpdateDataException ex = assertThrows(
                StudentExceptions.InvalidUpdateDataException.class,
                () -> operations.updateStudent("11111111111", updated));
 
        assertTrue(ex.getMessage().startsWith("Invalid update data:"));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }
 
    // P4: 3-4-5-9-15-16 (liste boş, döngüye girilmez, bulunamadı)
    @Test
    void path4_emptyList_throwsStudentUpdateException() {
        StudentOperations emptyOps = new StudentOperations();
        Student updated = new Student("11111111111", "Ismail", "01.01.2006", 90.0);
 
        StudentExceptions.StudentUpdateException ex = assertThrows(
                StudentExceptions.StudentUpdateException.class,
                () -> emptyOps.updateStudent("11111111111", updated));
 
        assertEquals("Failed to update student: Student with PRN 11111111111 not found",
                ex.getMessage());
    }
 
    // P5: 3-4-5-9-10-11-12-16 (ilk elemanda bulundu)
    @Test
    void path5_foundAtFirstElement_replacesStudent() throws Exception {
        Student updated = new Student("11111111111", "Ismail Updated", "01.01.2006", 95.0);
 
        operations.updateStudent("11111111111", updated);
 
        assertSame(updated, operations.searchByPrn("11111111111"));
        assertSame(student2, operations.searchByPrn("22222222222"));
    }
 
    // P6: 3-4-5-9-10-14-9-10-11-12-16 (ilkinde eşleşmedi, ikincide bulundu)
    @Test
    void path6_foundAtSecondElement_replacesStudent() throws Exception {
        Student updated = new Student("22222222222", "Eren Updated", "01.01.2008", 95.0);
 
        operations.updateStudent("22222222222", updated);
 
        assertSame(updated, operations.searchByPrn("22222222222"));
        assertSame(student1, operations.searchByPrn("11111111111"));
    }
 
    // Ekstra (basis path değil): 3-4-5-9-10-14-9-10-14-9-15-16
    // Dolu listede hiçbir eleman eşleşmez, döngü biter, 15'e gidilir
    @Test
    void extra_nonEmptyListNotFound_throwsStudentUpdateException() {
        Student updated = new Student("44444444444", "Nobody", "01.01.2000", 50.0);
 
        assertThrows(StudentExceptions.StudentUpdateException.class,
                () -> operations.updateStudent("44444444444", updated));
    }
}