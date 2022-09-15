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

    // TODO: Thread status notifier
    @SuppressWarnings("FieldCanBeLocal")
    private Thread studentEntryThread;

    public StudentEntryController(StudentEntryView view) {
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

            try {
                addEntry(newStudent);
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
     *
     * @param newStudent instance of a StudentModel
     */
    public void addEntry(StudentModel newStudent) throws IOException {
        Runnable write = () -> {
            String delimiter = Constants.DELIMITER;

            try (FileWriter out = new FileWriter(Constants.DB_STUDENTS, true)) {
                out.write(newStudent.getId() + delimiter + newStudent.getEmail() + delimiter + newStudent.getLastName() + delimiter + newStudent.getFirstName() + delimiter + newStudent.getMiddleName() + delimiter + newStudent.getCourse() + delimiter + newStudent.getYearLevel() + delimiter + newStudent.getSection() + delimiter + newStudent.getDateCreated());
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while adding a new student entry."));
            }
        };

        studentEntryThread = new Thread(write);
        studentEntryThread.start();
    }
}
