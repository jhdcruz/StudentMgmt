package controller;

import model.Constants;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentEntryView;
import view.StudentsView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StudentController {

    private final String delimiter = Constants.DELIMITER;

    public StudentController(StudentsView view) {
        // fetch students on load
        try {
            getEntries(view.tableModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // listeners
        view.entryAdd.addActionListener(actionEvent -> newEntry(view.tableModel));
        view.refresh.addActionListener(actionEvent -> refreshEntries(view.tableModel));
        view.adminSignOut.addActionListener(actionEvent -> signOut(view));
    }

    /**
     * Show modal dialog for adding new student entry
     *
     * @param tableModel table access for manual entry insertion
     */
    public void newEntry(DefaultTableModel tableModel) {
        StudentEntryView studentEntryView = new StudentEntryView(tableModel);
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

        new Thread(runnable).start();
    }

    /**
     * Update student data
     *
     * @param table table to get selected row and col to update
     */
    public void updateEntry(JTable table) {
        Runnable runnable = () -> {
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            FileReader in;
            BufferedReader reader;

            try {
                in = new FileReader(Constants.DB_STUDENTS);
                reader = new BufferedReader(in);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (col != 0 && row != -1) {
                try {
                    String line;
                    String[] studentEntry;

                    String id = table.getValueAt(row, 0).toString();
                    String value = table.getValueAt(row, col).toString();

                    while ((line = reader.readLine()) != null) {
                        studentEntry = line.split(delimiter);
                        if (studentEntry[0].equals(id)) {
                            studentEntry[col] = value;
                        }
                    }
                } catch (Exception e) {
                    new ErrorDialogView(new Exception("An error occurred while updating student data."));
                }
            }
        };

        new Thread(runnable).start();
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
