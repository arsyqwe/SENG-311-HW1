import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class StudentListIntegrityTest {
    private StudentOperations operations;

    @BeforeEach
    void setUp() {
        operations = new StudentOperations();
    }

    // 45
    @Test
    @DisplayName("A stored student's PRN set to null doesn't break the list for everyone else")
    void setPrnNull_afterAdding_otherStudentsStillWork() throws Exception {
        Student s = new Student("11111111111", "Ismail", "01.01.2006", 80.0);
        Student other = new Student("22222222222", "Eren", "01.01.2008", 85.0);
        Student newcomer = new Student("33333333333", "Ahmet", "01.01.2005", 90.0);
        operations.addStudent(s);
        operations.addStudent(other);

        s.setPrn(null);

        assertAll("list after null PRN",
            () -> assertSame(other, operations.searchByPrn("22222222222")),
            () -> assertDoesNotThrow(() -> operations.addStudent(newcomer)));
    }

    // 54
    @Test
    @DisplayName("Using setters to get past duplicate guards shouldn't be allowed")
    void addValidStudent_thenModifyBySetter() throws Exception{
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        assertDoesNotThrow(() -> operations.addStudent(s));
        Student s1 = new Student("12345678910", "A", "01.01.2006", 81.5);
        assertDoesNotThrow(() -> operations.addStudent(s1));
        
        s1.setPrn("49126067566");
        operations.deleteStudent("49126067566");

        assertThrows(StudentExceptions.StudentNotFoundException.class,
            () -> operations.searchByPrn("49126067566"));
    }
    
    // 72
    @Test 
    @DisplayName("Changing a student returned by getStudentsAboveMarks doesn't change the stored one")
    void getStudentsAboveMarks_modifyingReturnedStudent_doesNotChangeStored() throws Exception {
        Student student1 = new Student("11111111111", "Ismail", "01.01.2006", 80.0);
        Student student2 = new Student("22222222222", "Eren", "01.01.2008", 85.0);
        Student student3 = new Student("33333333333", "Ahmet", "01.01.2005", 90.0);

        operations.addStudent(student1);
        operations.addStudent(student2);
        operations.addStudent(student3);
        
        ArrayList<Student> result = operations.getStudentsAboveMarks(80.0);
        
        Student returned = result.get(0);
        returned.setMarks(40.0);

        assertEquals(85.0, operations.searchByPrn(returned.getPrn()).getMarks(), 0.0001);
    }
}
