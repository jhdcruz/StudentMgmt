package controller;

import model.Constants;
import model.StudentModel;
import view.ErrorDialogView;
import view.StudentEntryView;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

public class StudentEntryController {
    StudentModel studentModel = new StudentModel();

    public StudentEntryController(StudentEntryView view) {
        // Listeners
        view.entrySubmit.addActionListener(actionEvent -> {
            try {
                addEntry(null);
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while submitting student entry.\n" + e.getMessage()));
            }
        });

        // close on ESCAPE key
        view.contentPane.registerKeyboardAction(actionEvent -> view.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        view.entryCancel.addActionListener(actionEvent -> view.dispose());
    }

    /**
     * Add a new student to the database (students.txt)
     *
     * @param newStudent instance of a StudentModel
     */
    public void addEntry(StudentModel newStudent) throws IOException {
        String delimiter = Constants.DELIMITER;
        FileWriter out = new FileWriter(Constants.DB_STUDENTS, true);


        try (out) {
            out.write(newStudent.getId() + delimiter + newStudent.getEmail() + delimiter + newStudent.getLastName() + delimiter + newStudent.getFirstName() + delimiter + newStudent.getMiddleName() + delimiter + newStudent.getCourse() + delimiter + newStudent.getYearLevel() + delimiter + newStudent.getSection() + delimiter + newStudent.getDateCreated());
        } catch (IOException e) {
            new ErrorDialogView(new Exception("An error occurred while adding a new student entry."));
        }
    }
}

