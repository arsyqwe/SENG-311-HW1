import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

public class SearchByNameTest {
    private StudentOperations operations;
    private Student student1;

    @BeforeEach
    void setUp() throws Exception {
        operations = new StudentOperations();

        student1 = new Student("11111111111", "Ismail", "01.01.2006", 80.0);

        operations.addStudent(student1);
    }

    @Test
    @Tag("pit")
    @DisplayName("Test for path 1")
    void searchByName_path1() {
        assertThrows(StudentExceptions.InvalidSearchCriteriaException.class,
            () -> operations.searchByName(null)
        );
    }
    
    @Test
    @Tag("pit")
    @DisplayName("Test for path 2")
    void searchByName_path2() {
        assertThrows(StudentExceptions.InvalidSearchCriteriaException.class,
            () -> operations.searchByName("")
        );
    }
    
    @Test
    @Tag("pit")
    @DisplayName("Test for path 3")
    void searchByName_path3() {
        operations = new StudentOperations();
        
        assertThrows(StudentExceptions.StudentNotFoundException.class,
            () -> operations.searchByName("Ismail")
        );
    }
    
    @Test
    @Tag("pit")
    @DisplayName("Test for path 4")
    void searchByName_path4() throws Exception {
        assertSame(student1, operations.searchByName("Ismail"));
    }
    
    @Test
    @Tag("pit")
    @DisplayName("Test for path 5")
    void searchByName_path5() throws Exception {
        Student student2 = new Student("22222222222", "Eren", "01.01.2008", 85.0);
        operations.addStudent(student2);

        assertSame(student2, operations.searchByName("Eren"));
    }
}