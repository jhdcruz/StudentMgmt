package controller;

import model.Constants;
import model.StudentModel;
import view.ErrorDialogView;
import view.StudentEntryView;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentEntryController {

    private final StudentEntryView view;
    private final DefaultTableModel tableModel;

    private Object[] newStudentRow;
    private StudentModel newStudent;

    public StudentEntryController(StudentEntryView view, DefaultTableModel tableModel) {
        this.view = view;
        this.tableModel = tableModel;


        // Listeners
        view.entrySubmit.addActionListener(actionEvent -> {
            newStudent = new StudentModel();

            // use fixed locale to mitigate locale inconsistencies
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);

            // grab input data from view
            newStudent.setId(generateStudentId());
            newStudent.setFirstName(view.getNameFirst());
            newStudent.setMiddleName(view.getNameMiddle());
            newStudent.setLastName(view.getNameLast());
            newStudent.setCourse(view.getCourse());
            newStudent.setYearLevel(view.getYearLevel());
            newStudent.setSection(view.getSection());
            newStudent.setEmail(view.getEmail());
            // set current time & date with format
            newStudent.setDateCreated(dateFormat.format(new Date()));

            // add entry after validation checks
            if (validateEntry()) {
                addEntry(newStudent);

                view.dispose();
            }
        });

        view.entryCancel.addActionListener(actionEvent -> view.dispose());
        // close on ESCAPE key
        view.contentPane.registerKeyboardAction(actionEvent -> view.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // submit on ENTER key
        view.contentPane.registerKeyboardAction(actionEvent -> view.entrySubmit.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Validate form inputs
     * <p>
     * suppress unused warning for name variable
     * since were using it for validation for all
     * name fields instead of individual variables
     * <p>
     * separate ifs to show all invalid inputs at once
     * instead of one at a time
     */
    @SuppressWarnings("UnusedAssignment")
    public boolean validateEntry() {
        boolean email = false;
        boolean section = false;
        boolean name = false;
        boolean duplicates = false;

        if (view.getEmail().isEmpty()) {
            view.emptyEmail.setText("Email is required.");
        } else if (!view.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            view.emptyEmail.setText("Invalid email address.");
        } else {
            email = true;
            view.emptyEmail.setText("");
        }

        if (view.getNameFirst().isEmpty()) {
            //noinspection ConstantConditions
            name = false;
            view.emptyNameFirst.setText("First name is required.");
        } else if (!view.getNameFirst().matches("^[A-Za-z ]+$")) {
            //noinspection ConstantConditions
            name = false;
            view.emptyNameFirst.setText("Invalid first name.");
        } else {
            name = true;
            view.emptyNameFirst.setText("");
        }

        if (view.getNameMiddle().isEmpty()) {
            name = false;
            view.emptyNameMiddle.setText("Middle name is required.");
        } else if (!view.getNameMiddle().matches("^[A-Za-z ]+$")) {
            name = false;
            view.emptyNameMiddle.setText("Invalid middle name.");
        } else {
            name = true;
            view.emptyNameMiddle.setText("");
        }

        if (view.getNameLast().isEmpty()) {
            name = false;
            view.emptyNameLast.setText("Last name is required.");
        } else if (!view.getNameLast().matches("^[A-Za-z ]+$")) {
            name = false;
            view.emptyNameLast.setText("Invalid last name.");
        } else {
            name = true;
            view.emptyNameLast.setText("");
        }

        if (view.getSection().isEmpty()) {
            view.emptySection.setText("Section is required.");
        } else if (!view.getSection().matches("^[A-Za-z0-9]+$")) {
            view.emptySection.setText("Invalid section.");
        } else {
            section = true;
            view.emptySection.setText("");
        }

        if (duplicateChecker()) {
            duplicates = true;
        } else {
            view.emptyEmail.setText("Email already exists.");
        }

        // return true if all fields are valid
        return email && name && section && duplicates;
    }

    /**
     * Generate student ID
     */
    public String generateStudentId() {
        // get current year
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        // append 4 random numbers to year
        return year + String.format("%04d", (int) (Math.random() * 10000));
    }

    /**
     * Check if student record already exists
     *
     * @return returns true if all fields are unique
     */
    public boolean duplicateChecker() {
        try (FileReader in = new FileReader(Constants.DB_STUDENTS); BufferedReader reader = new BufferedReader(in)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(Constants.DELIMITER);

                // check if email already exists
                if (row[1].equals(newStudent.getEmail())) {
                    return false;
                }

                // check for duplicate student ID in column 0
                if (row[0].equals(newStudent.getId())) {
                    // regenerate student ID if it already exists
                    newStudentRow[0] = generateStudentId();
                }
            }
        } catch (IOException e) {
            new ErrorDialogView(new Exception("Couldn't check for duplicate entries, try again later."));
        }

        return true;
    }

    /**
     * Add a new student to the database (students.txt)
     * This runs in a thread to avoid blocking the UI
     *
     * @param newStudent instance of a StudentModel
     */
    public void addEntry(StudentModel newStudent) {
        Runnable write = () -> {
            // This is used for adding a new row to the table manually
            // instead of reading the file again
            newStudentRow = new Object[]{newStudent.getId(), newStudent.getEmail(), newStudent.getLastName(), newStudent.getFirstName(), newStudent.getMiddleName(), newStudent.getCourse(), newStudent.getYearLevel(), newStudent.getSection(), newStudent.getDateCreated()};

            try (FileWriter out = new FileWriter(Constants.DB_STUDENTS, true)) {
                String delimiter = Constants.DELIMITER;
                // IMPORTANT: Make sure this is in the same order as the table columns
                //            located in Constants.TABLE_HEADERS which is used for
                //            manually adding data rows to the table.
                //            Although refreshing the table will automatically fix
                //            the order, it's better to keep it consistent.
                out.write(newStudent.getId() + delimiter + newStudent.getEmail() + delimiter + newStudent.getLastName() + delimiter + newStudent.getFirstName() + delimiter + newStudent.getMiddleName() + delimiter + newStudent.getCourse() + delimiter + newStudent.getYearLevel() + delimiter + newStudent.getSection() + delimiter + newStudent.getDateCreated() + "\n");
                tableModel.addRow(Arrays.stream(newStudentRow).toArray());
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while adding a new student entry."));
            }
        };

        new Thread(write).start();
    }
}
