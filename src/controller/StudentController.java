package controller;

import model.Constants;
import model.StudentModel;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentEntryView;
import view.StudentsView;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentController {
    StudentModel studentModel = new StudentModel();

    public StudentController(StudentsView view) {
        // Table Listeners
        view.entryAdd.addActionListener(actionEvent -> newEntry());
        view.refresh.addActionListener(actionEvent -> refreshEntries(view.tableModel));
        view.adminSignOut.addActionListener(actionEvent -> signOut(view));
    }

    /**
     * Show modal dialog for adding new student entry
     */
    public void newEntry() {
        StudentEntryView studentEntryView = new StudentEntryView();
        studentEntryView.setVisible(true);
    }

    public void getEntries(DefaultTableModel tableModel) throws IOException {
        FileReader in = new FileReader(Constants.DB_STUDENTS);
        BufferedReader reader = new BufferedReader(in);

        // Run data fetching in a thread, presumably helps for large data
        Runnable runnable = () -> {
            try (in; reader) {
                String line;
                String delimiter = Constants.DELIMITER;
                String[] studentEntry;

                while ((line = reader.readLine()) != null) {
                    studentEntry = line.split(delimiter);
                    tableModel.addRow(studentEntry);
                }
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while getting student entries."));
            }
        };

        runnable.run();
    }

    /**
     * Refetch student data from the .txt file
     *
     * @param tableModel table model to be updated
     */
    public void refreshEntries(DefaultTableModel tableModel) {
        try {
            tableModel.setRowCount(0);
            getEntries(tableModel);
        } catch (IOException e) {
            new ErrorDialogView(new Exception("An error occurred while refreshing student entries."));
        }
    }

    /**
     * Dispose the main UI and show the login UI
     *
     * @param studentsView the main table UI (studentsView)
     */
    public void signOut(StudentsView studentsView) {
        studentsView.setVisible(false);
        studentsView.dispose();

        new LoginView().setVisible(true);

    }
}

