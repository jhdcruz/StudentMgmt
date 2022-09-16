package model;

public class Constants {

    // Application
    public static final String APP_NAME = "Student Management";

    // Database
    public static final String DELIMITER = ",";
    public static final String DB_ADMINS = "src/db/admins.txt";
    public static final String DB_STUDENTS = "src/db/students.txt";
    public static final String DB_STUDENTS_TMP = "src/db/students.tmp";

    // Table
    public static final Object[] TABLE_HEADERS = {"ID #", "Email", "Last Name", "First Name", "Middle Name", "Course", "Year Level", "Section", "Date Created"};

    // Entries
    public static final String[] ENTRY_COURSES = {"BSIT", "BSCS", "BSIS", "BSDSA", "BSEMC"};
    public static final String[] ENTRY_YEAR_LEVELS = {"1", "2", "3", "4"};
}
