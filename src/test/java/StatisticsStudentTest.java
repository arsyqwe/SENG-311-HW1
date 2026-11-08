import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    @Tag("pit")
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
    
    // 67
    @Test
    @DisplayName("Average Marks on empty list throws empty student list exception")
    void getAverageMarks_emptyList() throws Exception {
        StudentOperations op = new StudentOperations();
        assertThrows(StudentExceptions.EmptyStudentListException.class, 
            () -> op.getAverageMarks());
    }
    
    // 68
    @Test
    @DisplayName("Average Marks that isn't a whole number is not truncated or rounded")
    void getAverageMarks_nonWholeAverage() throws Exception {
        StudentOperations op = new StudentOperations();
        Student s = new Student("12345678910", "A", "01.01.2002", 80.0);
        Student s1 = new Student("12345678911", "B", "01.01.2002", 85.0);
        op.addStudent(s);
        op.addStudent(s1);

        assertEquals(82.5, op.getAverageMarks(), 0.0001);
    }
   
    // 69
    @Test
    @DisplayName("Top student, when tied, is the one with the lowest PRN")
    void getTopStudent_whenTwoMaxes_returnLowestPrn() throws Exception{
        // Tied at 90 with student3 (33333333333). The lowest PRN is neither the first added,
        // the last added, nor the first alphabetically.
        Student student4 = new Student("30000000000", "Zehra", "01.01.2002", 90.0);
        Student student5 = new Student("99999999999", "Aahmet", "01.01.2005", 90.0);
        operations.addStudent(student4);
        operations.addStudent(student5);

        assertSame(student4, operations.getTopStudent());
    }
    
    // 70 (parameterized)
    @ParameterizedTest
    @DisplayName("Above Marks tested against threshold values expected to be invalid")
    @ValueSource(doubles = {-50, 150, Double.NaN, Double.POSITIVE_INFINITY, -0.01, -0.0001, 100.01, 100.0001})
    void getStudentsAboveMarks_invalidThreshold(double invalidThreshold) throws Exception {
        assertThrows(StudentExceptions.InvalidMarksException.class,
            () -> operations.getStudentsAboveMarks(invalidThreshold)
        );
    }
    
    // 71
    @Test
    @DisplayName("Above Marks on empty list throws empty student list exception")
    void getStudentsAboveMarks_emptyList_throwsEmptyListException() throws Exception {
        StudentOperations op = new StudentOperations();
        assertThrows(StudentExceptions.EmptyStudentListException.class, 
            () -> op.getStudentsAboveMarks(50));
    }
    
    // 73
    @Test 
    @DisplayName("getStudentsAboveMarks returned list modification")
    void getStudentsAboveMarks_clearingReturnedList_doesNotChangeStored() throws Exception {
        ArrayList<Student> result = operations.getStudentsAboveMarks(0.0);
        
        result.clear();

        assertIterableEquals(
            List.of(student1, student2, student3),
            operations.getStudentsAboveMarks(0.0)
        );
    }

    // 75
    @Test
    @DisplayName("Sorting by marks puts the highest marks first, and display shows the new order")
    void sortByMarks_sortsDescending_displayFollows() throws Exception {
        // Added last so the stored order is not simply ascending; reversing the list would not sort it
        Student student4 = new Student("44444444444", "Zehra", "01.01.2002", 87.5);
        operations.addStudent(student4);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            operations.sortByMarks();
            operations.displayStudents();
        } finally {
            System.setOut(originalOut);
        }

        String n = System.lineSeparator();
        assertAll("descending order",
                () -> assertSame(student3, operations.searchByPosition(0)),
                () -> assertSame(student4, operations.searchByPosition(1)),
                () -> assertSame(student2, operations.searchByPosition(2)),
                () -> assertSame(student1, operations.searchByPosition(3)),
                () -> assertEquals("Position 0: " + student3 + n
                        + "Position 1: " + student4 + n
                        + "Position 2: " + student2 + n
                        + "Position 3: " + student1 + n, outContent.toString()));
    }

    // 76
    @Test
    @DisplayName("Sorting by marks puts tied students in PRN order")
    void sortByMarks_tiedMarks_ordersByPrn() throws Exception {
        // Tied at 85 with student2 (22222222222) but added later with a lower PRN
        Student student4 = new Student("12222222222", "Zehra", "01.01.2002", 85.0);
        operations.addStudent(student4);

        operations.sortByMarks();

        assertAll("tied marks in PRN order",
                () -> assertSame(student3, operations.searchByPosition(0)),
                () -> assertSame(student4, operations.searchByPosition(1)),
                () -> assertSame(student2, operations.searchByPosition(2)),
                () -> assertSame(student1, operations.searchByPosition(3)));
    }

    // 77
    @Test
    @DisplayName("Sorting an empty list does nothing and leaves it empty")
    void sortByMarks_emptyList_doesNotThrow() {
        StudentOperations op = new StudentOperations();

        assertDoesNotThrow(() -> op.sortByMarks());
        assertThrows(StudentExceptions.StudentNotFoundException.class,
            () -> op.searchByPosition(0));
    }
}