import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class SearchStudentTest {
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

    // 19
    @Test
    void searchByPrn_lastStudent_returnsCorrectName() throws Exception {
        Student found = operations.searchByPrn("33333333333");

        assertEquals("Ahmet", found.getName());
    }

    // 20
    @Test
    void searchByPrn_validPrnNotInList_throwsStudentNotFoundException() {
        String missingPrn = "44444444444";

        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByPrn(missingPrn));

        assertEquals("Student not found: PRN: " + missingPrn, exception.getMessage());
    }

    // 21
    @Test
    void searchByPrn_invalidPrnFormat_throwsInvalidSearchCriteriaException() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPrn("123"));

        assertTrue(exception.getMessage().startsWith("Invalid search criteria: Invalid PRN format"));
    }

    // 22
    @Test
    void searchByPrn_nullPrn_throwsInvalidSearchCriteriaException() {
        assertThrows(StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPrn(null));
    }

    // 23
    @Test
    void searchByPrn_emptyRepository_throwsStudentNotFoundException() {
        StudentOperations emptyOperations = new StudentOperations();

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> emptyOperations.searchByPrn("11111111111"));
    }

    // 24
    @Test
    void searchByName_exactName_returnsSameObject() throws Exception {
        Student found = operations.searchByName("Ismail");

        assertSame(student1, found);
    }

    // 25
    @Test
    void searchByName_lowerCase_findsStudentAndChecksPrn() throws Exception {
        Student s4 = new Student("44444444444", "Emre", "01.01.2000", 77);
        operations.addStudent(s4);

        Student found = operations.searchByName("emre");

        assertEquals("44444444444", found.getPrn());
    }

    // 26
    @Test
    void searchByName_nonExistingName_throwsExceptionAndMessage() {
        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByName("Non existing name"));

        assertEquals("Student not found: Name: Non existing name", exception.getMessage());
    }

    // 27
    @Test
    void searchByName_nullName_throwsExceptionAndMessage() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByName(null));

        assertEquals("Invalid search criteria: Name cannot be empty", exception.getMessage());
    }

    // 28
    @Test
    void searchByName_partialName_throwsStudentNotFoundException() throws Exception {
        Student s4 = new Student("99999999999", "Ali Haydar", "01.01.2009", 21);
        operations.addStudent(s4);

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByName("Ali"));
    }

    // 29
    @Test
    void searchByPosition_zero_returnsFirstStudent() throws Exception {
        Student found = operations.searchByPosition(0);

        assertSame(student1, found);
    }

    // 30
    @Test
    void searchByPosition_negative_throwsExceptionAndMessage() {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPosition(-1));

        assertEquals("Invalid search criteria: Position cannot be negative", exception.getMessage());
    }

    // 31
    @Test
    void searchByPosition_equalsListSize_throwsExceptionAndMessage() {
        StudentExceptions.StudentNotFoundException exception = assertThrows(
                StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByPosition(3));

        assertEquals("Student not found: Position: 3", exception.getMessage());
    }

    // 32
    @Test
    void searchByPosition_emptyRepo_throwsException() {
        StudentOperations emptyOperations = new StudentOperations();

        assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> emptyOperations.searchByPosition(0));
    }
    
    // 55
    @Test
    @DisplayName("A name made up of spaces is rejected when adding, just as it is when searching")
    void searchByName_nameFullOfSpaces() {
        Student s = new Student("12345678910", "    ","01.01.2002",77.7);

        assertAll("blank name",
            () -> assertThrows(StudentExceptions.InvalidStudentDataException.class,
                () -> operations.addStudent(s)),
            () -> assertThrows(StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByName("    ")));
    }
    
    // 56
    @Test
    @DisplayName("Two students with the same name return the first")
    void searchByName_duplicate() throws Exception {
        Student s = new Student("12345678910", "Ismail", "01.01.2006", 80.0);
        operations.addStudent(s);
        
        assertSame(student1, operations.searchByName("Ismail"));
    }
    
    // 57
    @Test
    @DisplayName("Search functioning while student with null name is in a list")
    void searchByName_whileNullNameInList() throws Exception {
        Student s = new Student("12345678910", null, "01.01.2002", 80.5);
        Student sLast = new Student("99999999999", "Last", "01.01.2002", 9.5);
        
        operations.addStudent(s);
        operations.addStudent(sLast);
        
        assertAll(
            () -> assertSame(student1, operations.searchByName("Ismail")),
            () -> assertSame(sLast, operations.searchByName("Last")),
            () -> assertThrows(StudentExceptions.StudentNotFoundException.class, 
                () -> operations.searchByName("G"))
        );
    }
    
    // 58
    @Test
    @DisplayName("Case-insensitive name matching with non-English letters")
    void searchByName_nonEnglish() throws Exception {
        Student s = new Student("12345678910", "Ayşe", "01.01.2002", 80.5);
        operations.addStudent(s);
        Student s1 = new Student("12345678911", "İsmail", "01.01.2002", 80.5);
        operations.addStudent(s1);
        Student s2 = new Student("12345678912", "Hakkı", "01.01.2002", 80.5);
        operations.addStudent(s2);
        
        assertAll(
            () -> assertSame(s, operations.searchByName("ayşe")),
            () -> assertSame(s, operations.searchByName("AYŞE")),
            () -> assertThrows(StudentExceptions.StudentNotFoundException.class,
                () -> operations.searchByName("ayse")),
            () -> assertSame(student1, operations.searchByName("Ismail")),
            () -> assertSame(student1, operations.searchByName("ısmail")),
            () -> assertSame(s1, operations.searchByName("İsmail")),
            () -> assertSame(s1, operations.searchByName("ismail")),
            () -> assertSame(s2, operations.searchByName("HAKKI")),
            () -> assertThrows(StudentExceptions.StudentNotFoundException.class, 
                () -> operations.searchByName("hakki")),
            () -> assertThrows(StudentExceptions.StudentNotFoundException.class, 
                () -> operations.searchByName("HAKKİ"))
        );
    }
    
    // 59 (parameterized)
    @ParameterizedTest
    @DisplayName("Searching by PRN with invalid format of length 11 throws invalid search criteria exception")
    @ValueSource(strings = {"1234567890", "123456789012", "12345abc901", "12345 67890", "           ", "+1234567890", "١٢٣٤٥٦٧٨٩١٠"})
    void searchByPrn_invalidFormat_throwsImproperSearchCriteria(String invalidPrn) {
        StudentExceptions.InvalidSearchCriteriaException exception = assertThrows(
                StudentExceptions.InvalidSearchCriteriaException.class,
                () -> operations.searchByPrn(invalidPrn));
                
        assertTrue(exception.getMessage().startsWith("Invalid search criteria: Invalid PRN format"));
    }
    // FYI, "١٢٣٤٥٦٧٨٩١٠" is "12345678910" in Arabic numerals, and are in fact considered digits in 'Character.isDigit()', hence why it's here
}