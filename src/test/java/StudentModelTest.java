import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;

public class StudentModelTest {

    // 1
    @Test
    void constructorAndGetters_storeAllFields() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        assertAll("student fields",
                () -> assertEquals("49126067566", s.getPrn()),
                () -> assertEquals("Ismail", s.getName()),
                () -> assertEquals("01.01.2006", s.getDob()),
                () -> assertEquals(80.5, s.getMarks(), 0.001));
    }

    // 2
    @Test
    void setters_updateAllFields() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        s.setPrn("11111111111");
        s.setName("Eren");
        s.setDob("02.02.2007");
        s.setMarks(90.0);

        assertAll("updated fields",
                () -> assertEquals("11111111111", s.getPrn()),
                () -> assertEquals("Eren", s.getName()),
                () -> assertEquals("02.02.2007", s.getDob()),
                () -> assertEquals(90.0, s.getMarks(), 0.001));
    }

    // 3
    @Test
    void toString_hasExpectedOutput() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);

        assertEquals("PRN: 49126067566, Name: Ismail, DoB: 01.01.2006, Marks: 80.5",
                s.toString());
    }

    // 4
    @Test
    void twoStudentsWithSameData_areDifferentObjects() {
        Student s = new Student("49126067566", "Ismail", "01.01.2006", 80.5);
        Student s1 = new Student("49126067566", "Ismail", "01.01.2006", 80.5);

        assertNotEquals(s, s1);
        assertNotSame(s, s1);
    }

    // 5
    @Test
    void constructor_withNullName_storesNull() {
        Student s = new Student("49126067566", null, "01.01.2006", 80.5);

        assertNull(s.getName());
    }
    
    // 41
    @Test
    @DisplayName("Setters reject invalid values with IllegalArgumentException and keep the old value")
    void setters_rejectsInvalidValues() {
        Student s = new Student("12345678910", null, "01.01.2004", 100);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> s.setPrn("-1")),
            () -> assertThrows(IllegalArgumentException.class, () -> s.setDob("00.00.0000")),
            () -> assertThrows(IllegalArgumentException.class, () -> s.setMarks(-50)),
            () -> assertEquals("12345678910", s.getPrn()),
            () -> assertEquals("01.01.2004", s.getDob()),
            () -> assertEquals(100.0, s.getMarks(), 0.001)
        );
    }
    
    // 42
    @Test
    @DisplayName("ToString when null")
    void toString_null() {
        Student s = new Student(null, null, null, 0.0);
        
        assertEquals("PRN: null, Name: null, DoB: null, Marks: 0.0", s.toString());
    }
    
    // 43
    @Test
    @DisplayName("ToString when marks is int")
    void toString_intMarks() {
        Student s = new Student("12345678910", "A", "01.01.2004", 80);
        
        assertEquals("PRN: 12345678910, Name: A, DoB: 01.01.2004, Marks: 80.0", s.toString());
    }
    
    // 44
    @Test
    @DisplayName("A student in a HashSet is still found after its fields are changed")
    void studentInHashSet_changedFields_stillFound() {
        Student s = new Student("12345678910", "A", "01.01.2004", 80);

        HashSet<Student> hs = new HashSet<Student>();
        hs.add(s);
        s.setPrn("10987654321");
        s.setMarks(50);

        assertAll(
            () -> assertTrue(hs.contains(s)),
            () -> assertFalse(hs.add(s))
        );
    }
}