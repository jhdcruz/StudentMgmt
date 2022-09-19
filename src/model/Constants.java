package model;

public class Constants {

    // Application
    public static final String APP_NAME = "Student Management";
    public static final String APP_DESCRIPTION = "Basic student management application";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_COPYRIGHT = "Copyright © 2022 Student Management";
    public static final String[] APP_AUTHORS = {"Dela Cruz, Joshua Hero", "Lim, Bruce Wayne", "Candoy, John Matthew", "Logdat, Randel", "Soriano, Raphael Kim"};

    // Database
    public static final String DELIMITER = ",";
    public static final String DB_ADMINS = "src/model/admins.txt";
    public static final String DB_STUDENTS = "src/model/students.txt";
    public static final String DB_STUDENTS_TMP = "src/model/students.tmp";

    // Table
    public static final Object[] TABLE_HEADERS = {"ID #", "Email", "Last Name", "First Name", "Middle Name", "Course", "Year Level", "Section", "Date Created"};

    // Entries
    public static final String[] ENTRY_COURSES = {"BSIT", "BSCS", "BSIS", "BSDSA", "BSEMC"};

    // String for JComboBox in table cells instead of JSpinner
    public static final String[] ENTRY_YEAR_LEVELS = {"1", "2", "3", "4"};
}
