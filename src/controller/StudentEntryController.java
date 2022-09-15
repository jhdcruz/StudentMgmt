package controller;

import model.Constants;
import model.StudentModel;
import view.ErrorDialogView;
import view.StudentEntryView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class StudentEntryController {

    private Thread studentEntryThread;
    private Object[] newStudentRow;

    public StudentEntryController(StudentEntryView view, DefaultTableModel tableModel) {
        // Listeners
        view.entrySubmit.addActionListener(actionEvent -> {
            StudentModel newStudent = new StudentModel();

            // grab input data from view
            newStudent.setId(view.id.getText());
            newStudent.setFirstName(view.getNameFirst());
            newStudent.setMiddleName(view.getNameMiddle());
            newStudent.setLastName(view.getNameLast());
            newStudent.setCourse(view.getCourse());
            newStudent.setYearLevel(view.getYearLevel());
            newStudent.setSection(view.getSection());
            newStudent.setEmail(view.getEmail());

            // set current time & date
            newStudent.setDateCreated(new Date());

            try {
                addEntry(newStudent);

                // this assumes entry was successful, errors in adding
                // entry is handled inside `addEntry()` itself.
                view.dispose();

                try {
                    studentEntryThread.join();
                    tableModel.addRow(Arrays.stream(newStudentRow).toArray());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while submitting student entry.\n" + e.getMessage()));
            }
        });

        view.entryCancel.addActionListener(actionEvent -> view.dispose());
        // close on ESCAPE key
        view.contentPane.registerKeyboardAction(actionEvent -> view.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Add a new student to the database (students.txt)
     * This runs in a thread to avoid blocking the UI
     *
     * @param newStudent instance of a StudentModel
     */
    public void addEntry(StudentModel newStudent) throws IOException {
        Runnable write = () -> {
            // This is used for adding a new row to the table manually
            // we run this in a thread to avoid blocking the UI.
            newStudentRow = new Object[]{newStudent.getId(), newStudent.getEmail(), newStudent.getLastName(), newStudent.getFirstName(), newStudent.getMiddleName(), newStudent.getCourse(), newStudent.getYearLevel(), newStudent.getSection(), newStudent.getDateCreated()};

            try (FileWriter out = new FileWriter(Constants.DB_STUDENTS, true)) {
                String delimiter = Constants.DELIMITER;
                // IMPORTANT: this assumes that the data is already validated
                //            this also assumes that the order is correct and
                //            aligned with the table headers in the `StudentsView`
                out.write(newStudent.getId() + delimiter + newStudent.getEmail() + delimiter + newStudent.getLastName() + delimiter + newStudent.getFirstName() + delimiter + newStudent.getMiddleName() + delimiter + newStudent.getCourse() + delimiter + newStudent.getYearLevel() + delimiter + newStudent.getSection() + delimiter + newStudent.getDateCreated() + "\n");
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while adding a new student entry."));
            }
        };

        studentEntryThread = new Thread(write);
        studentEntryThread.start();
    }
}
