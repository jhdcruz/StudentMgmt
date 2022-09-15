package controller;

import model.Constants;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentEntryView;
import view.StudentsView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.stream.IntStream;

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
        view.entryDelete.addActionListener(actionEvent -> deleteEntries(view.tableModel, view.studentsTable));
        view.entrySearch.addActionListener(actionEvent -> searchEntries(view.tableModel, view.getEntrySearch()));
        // view.entryEdit.addActionListener(actionEvent -> updateEntry(view.studentsTable));

        view.refresh.addActionListener(actionEvent -> refreshEntries(view.tableModel));
        view.adminSignOut.addActionListener(actionEvent -> signOut(view));
    }

    /**
     * Filter entries by search query
     *
     * @param tableModel table model
     * @param query      search query
     */
    private void searchEntries(DefaultTableModel tableModel, String query) {
        Runnable runnable = () -> {
            FileReader in;
            BufferedReader reader;

            try {
                in = new FileReader(Constants.DB_STUDENTS);
                reader = new BufferedReader(in);

                tableModel.setRowCount(0);
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(delimiter);

                    // Look for matches in all columns except the date
                    if (IntStream.of(0, 1, 2, 3, 4, 5, 6, 7).anyMatch(i -> data[i].contains(query))) {
                        tableModel.addRow(data);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(runnable).start();
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

            if (col != 0 && row != -1) {
                try {
                    in = new FileReader(Constants.DB_STUDENTS);
                    reader = new BufferedReader(in);

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

    public void deleteEntries(DefaultTableModel tableModel, JTable table) {
        Runnable runnable = () -> {
            File tmp;
            FileReader in;
            FileWriter out;
            BufferedReader reader;

            try {
                tmp = new File(Constants.DB_STUDENTS_TMP);
                in = new FileReader(Constants.DB_STUDENTS);
                out = new FileWriter(tmp, true);
                reader = new BufferedReader(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int[] selectedRows = table.getSelectedRows();
            if (selectedRows != null) {
                try (in; out; reader) {
                    String line;

                    // WORKAROUND:
                    // It is not possible to remove a line from a text file
                    // So we're going to copy all the lines except the ones we want to delete
                    // Source: https://stackoverflow.com/a/5800618/16171990 among many others...
                    while ((line = reader.readLine()) != null) {
                        // copy all lines except the ones that matches the id of the selected rows
                        String finalLine = line;
                        if (IntStream.of(selectedRows).noneMatch(i -> finalLine.split(delimiter)[0].equals(table.getValueAt(i, 0).toString()))) {
                            out.write(line + System.lineSeparator());
                        }
                    }

                    // remove selected rows from the table
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        tableModel.removeRow(selectedRows[i]);
                    }

                    // delete old file and rename tmp file
                    if (!tmp.renameTo(new File(Constants.DB_STUDENTS))) {
                        throw new RuntimeException("An error occurred while deleting student entries.");
                    }
                } catch (Exception e) {
                    new ErrorDialogView(new Exception("An error occurred while deleting student entries."));
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
