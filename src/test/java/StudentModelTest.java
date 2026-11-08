import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotSame(s, s1);
    }

    // 5
    @Test
    void constructor_withNullName_storesNull() {
        Student s = new Student("49126067566", null, "01.01.2006", 80.5);

        assertNull(s.getName());
    }
}