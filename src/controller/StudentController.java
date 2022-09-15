package controller;

import model.Constants;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentEntryView;
import view.StudentsView;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StudentController {

    // TODO: Thread status notifier
    @SuppressWarnings("FieldCanBeLocal")
    private Thread studentsThread;

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

    /**
     * Get student entries from file and add to table
     * this runs on a thread, presumably helps for large lists
     *
     * @param tableModel table model for `addRow()`
     */
    public void getEntries(DefaultTableModel tableModel) throws IOException {
        Runnable runnable = () -> {
            FileReader in;
            BufferedReader reader;

            try {
                in = new FileReader(Constants.DB_STUDENTS);
                reader = new BufferedReader(in);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            try (in; reader) {
                String delimiter = Constants.DELIMITER;
                String line;
                String[] studentEntry;

                while ((line = reader.readLine()) != null) {
                    studentEntry = line.split(delimiter);
                    tableModel.addRow(studentEntry);
                }
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while getting student entries."));
            }
        };

        studentsThread = new Thread(runnable);
        studentsThread.start();
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
