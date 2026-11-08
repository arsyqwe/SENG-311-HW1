public class StudentExceptions {
    // Add Student Exceptions
    public static class DuplicatePRNException extends Exception {
        public DuplicatePRNException(String prn) {
            super("Student with PRN " + prn + " already exists.");
        }
    }

    public static class InvalidStudentDataException extends Exception {
        public InvalidStudentDataException(String message) {
            super("Invalid student data: " + message);
        }
    }

    // Display Students Exceptions
    public static class EmptyStudentListException extends Exception {
        public EmptyStudentListException() {
            super("No students available in the system.");
        }
    }

    // Search Exceptions
    public static class StudentNotFoundException extends Exception {
        public StudentNotFoundException(String message) {
            super("Student not found: " + message);
        }
    }

    public static class InvalidSearchCriteriaException extends Exception {
        public InvalidSearchCriteriaException(String message) {
            super("Invalid search criteria: " + message);
        }
    }

    // Update Exceptions
    public static class StudentUpdateException extends Exception {
        public StudentUpdateException(String message) {
            super("Failed to update student: " + message);
        }
    }

    public static class InvalidUpdateDataException extends Exception {
        public InvalidUpdateDataException(String message) {
            super("Invalid update data: " + message);
        }
    }

    // Delete Exceptions
    public static class StudentDeletionException extends Exception {
        public StudentDeletionException(String message) {
            super("Failed to delete student: " + message);
        }
    }

    // General Validation Exceptions
    public static class InvalidPRNException extends Exception {
        public InvalidPRNException(String prn) {
            super("Invalid PRN format: " + prn);
        }
    }

    public static class InvalidMarksException extends Exception {
        public InvalidMarksException(double marks) {
            super("Invalid marks value: " + marks);
        }
    }
}