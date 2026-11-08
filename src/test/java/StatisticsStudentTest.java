import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsStudentTest {
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

    // 38
    @Test
    void getAverageMarks_threeStudents_returnsAverage() throws Exception {
        assertEquals(85.0, operations.getAverageMarks(), 0.001);
    }

    // 39
    @Test
    void getTopStudent_returnsStudentWithHighestMarks() throws Exception {
        assertSame(student3, operations.getTopStudent());
    }

    // 40
    @Test
    void getStudentsAboveMarks_threshold80_returnsOnlyHigherStudents() throws Exception {
        ArrayList<Student> result = operations.getStudentsAboveMarks(80.0);

        assertAll("above 80",
                () -> assertIterableEquals(List.of(student2, student3), result),
                () -> assertFalse(result.contains(student1)));
    }
}