package controller;

import model.Constants;
import view.AboutDialogView;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentEntryView;
import view.StudentsView;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.IntStream;

public class StudentController {

    private final StudentsView view;
    private final TableModelListener tableListener = this::updateEntry;

    private final String delimiter = Constants.DELIMITER;


    public StudentController(StudentsView view) {
        this.view = view;

        // fetch students on load
        getEntries(view.tableModel);

        // LISTENERS -----------------------------------

        // add confirmation when quitting the form
        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Any unsaved data will be lost.", "Exit Program?", JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    view.dispose();
                } else {
                    view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });


        // entry addition & modifications
        view.entryAdd.addActionListener(actionEvent -> newEntry());
        view.entryDelete.addActionListener(actionEvent -> deleteEntries());

        // listen for changes in the search field and filter the table
        view.entrySearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                searchEntries();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                searchEntries();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                searchEntries();
            }
        });

        // listen to table cell updates
        view.tableModel.addTableModelListener(tableListener);

        // Table popup menu (context menu)
        view.popupMenu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Automatically select row and col where popup menu is called
                // Skip when multiple rows are already selected
                if (view.studentsTable.getSelectedRowCount() == 1) {
                    Point p = view.studentsTable.getMousePosition();

                    if (p != null) {
                        int rowNumber = view.studentsTable.rowAtPoint(p);
                        int colNumber = view.studentsTable.columnAtPoint(p);
                        view.studentsTable.setRowSelectionInterval(rowNumber, rowNumber);
                        view.studentsTable.setColumnSelectionInterval(colNumber, colNumber);
                    }
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Ignore, auto-generated required method stub
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // Ignore, auto-generated required method stub
            }
        });

        // delete entries from popup menu
        view.popupMenuDelete.addActionListener(actionEvent -> deleteEntries());
        // edit/update entry from popup menu
        view.popupMenuEdit.addActionListener(actionEvent -> {
            // get row and col from right-clicked cell
            int row = view.studentsTable.getSelectedRow();
            int col = view.studentsTable.getSelectedColumn();

            view.studentsTable.editCellAt(row, col);
        });

        // About menu
        view.aboutMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {
                AboutDialogView about = new AboutDialogView();
                about.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent menuEvent) {
                // Ignore, auto-generated required method stub
            }

            @Override
            public void menuCanceled(MenuEvent menuEvent) {
                // Ignore, auto-generated required method stub
            }
        });

        // refresh the table
        view.refresh.addActionListener(actionEvent -> refreshEntries());
        // go back to login
        view.adminSignOut.addActionListener(actionEvent -> signOut());
    }

    /**
     * Filter entries by search query
     */
    public void searchEntries() {
        Runnable runnable = () -> {
            // reset table to give way for matched entries
            view.tableModel.setRowCount(0);

            try (FileReader in = new FileReader(Constants.DB_STUDENTS); BufferedReader reader = new BufferedReader(in)) {
                view.tableModel.removeTableModelListener(tableListener);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(delimiter);

                    // Look for matches in all columns except the date
                    if (IntStream.of(0, 1, 2, 3, 4, 5, 6, 7).anyMatch(i -> data[i].contains(view.getEntrySearch()))) {
                        view.tableModel.addRow(data);
                    }
                }

                view.tableModel.addTableModelListener(tableListener);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(runnable).start();
    }


    /**
     * Get student entries from file and add to table
     * this runs on a thread, presumably helps for large lists
     *
     * @param tableModel table model for `addRow()`
     */
    public void getEntries(DefaultTableModel tableModel) {
        Runnable runnable = () -> {
            try (FileReader in = new FileReader(Constants.DB_STUDENTS); BufferedReader reader = new BufferedReader(in)) {
                String line;
                String[] studentEntry;

                view.tableModel.removeTableModelListener(tableListener);

                while ((line = reader.readLine()) != null) {
                    studentEntry = line.split(delimiter);
                    tableModel.addRow(studentEntry);
                }

                view.tableModel.addTableModelListener(tableListener);
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while getting student entries."));
            }
        };

        new Thread(runnable).start();
    }

    /**
     * Update student data
     * <p>
     * Suppress duplicate code warning because it doesn't work with try-with-resources
     */
    @SuppressWarnings("DuplicatedCode")
    public void updateEntry(TableModelEvent tableEvent) {
        Runnable runnable = () -> {
            JTable table = view.studentsTable;

            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            if (col != -1 && row != -1) {
                try (FileReader in = new FileReader(Constants.DB_STUDENTS); FileWriter out = new FileWriter(Constants.DB_STUDENTS_TMP, true); BufferedReader reader = new BufferedReader(in)) {
                    String line;
                    String[] studentEntry;

                    String id = table.getValueAt(row, 0).toString();
                    String value = table.getValueAt(row, col).toString();

                    while ((line = reader.readLine()) != null) {
                        // copy all lines with the updated value in tmp file
                        if (line.contains(id)) {
                            studentEntry = line.split(delimiter);
                            studentEntry[col] = value;
                            out.write(String.join(delimiter, studentEntry) + System.lineSeparator());
                        }
                    }

                    // delete old file and rename tmp file
                    //noinspection ResultOfMethodCallIgnored
                    new File(Constants.DB_STUDENTS_TMP).renameTo(new File(Constants.DB_STUDENTS));
                } catch (Exception e) {
                    new ErrorDialogView(new Exception("An error occurred while updating student data."));
                }
            }
        };

        new Thread(runnable).start();
    }

    /**
     * Delete student entries
     * <p>
     * It is not possible to dynamically delete lines from a text file
     * So we have to copy all lines except the ones we want to delete
     * to a temporary file and then rename it to the original file.
     * <p>
     * Suppress duplicate code warning because it doesn't work with try-with-resources
     *
     * @see <a href="https://stackoverflow.com/a/5800618/16171990">Deleting lines from a file</a>
     */
    @SuppressWarnings("DuplicatedCode")
    public void deleteEntries() {
        if (view.studentsTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(view, "No row selected.", "Invalid Operation", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int proceed = JOptionPane.showConfirmDialog(view, "This action permanently deletes records, these actions are irreversible.", "Delete student records?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (proceed == JOptionPane.YES_OPTION) {
                Runnable runnable = () -> {
                    JTable table = view.studentsTable;
                    int[] selectedRows = table.getSelectedRows();

                    if (selectedRows != null) {
                        try (FileReader in = new FileReader(Constants.DB_STUDENTS); FileWriter out = new FileWriter(Constants.DB_STUDENTS_TMP, true); BufferedReader reader = new BufferedReader(in)) {
                            String line;

                            while ((line = reader.readLine()) != null) {
                                // copy all lines except the ones that matches the id of the selected rows
                                String finalLine = line;
                                if (IntStream.of(selectedRows).noneMatch(i -> finalLine.contains(table.getValueAt(i, 0).toString()))) {
                                    out.write(line + System.lineSeparator());
                                }
                            }

                            // delete old file and rename tmp file
                            if (new File(Constants.DB_STUDENTS_TMP).renameTo(new File(Constants.DB_STUDENTS))) {
                                // temporarily remove table listener for rows removal
                                view.tableModel.removeTableModelListener(tableListener);

                                // remove selected rows from the table
                                for (int i = selectedRows.length - 1; i >= 0; i--) {
                                    view.tableModel.removeRow(selectedRows[i]);
                                }

                                // re-add table listener
                                view.tableModel.addTableModelListener(tableListener);
                            }
                        } catch (Exception e) {
                            new ErrorDialogView(new Exception("An error occurred while deleting student entries."));
                        }
                    } // if selectedRows != null
                }; // runnable

                new Thread(runnable).start();
            }
        }

    }

    /**
     * Refetch student data from the .txt file
     */
    public void refreshEntries() {
        DefaultTableModel tableModel = view.tableModel;

        // temporarily remove table listener for rows removal
        view.tableModel.removeTableModelListener(tableListener);

        tableModel.setRowCount(0);
        getEntries(tableModel);

        // re-add table listener
        view.tableModel.addTableModelListener(tableListener);
    }

    /**
     * Dispose the main UI and show the login UI
     */
    public void signOut() {
        int proceed = JOptionPane.showConfirmDialog(view, "Are you sure you want to sign out?", "Sign out?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (proceed == JOptionPane.YES_OPTION) {
            view.dispose();

            new LoginView().setVisible(true);
        }

    }

    /**
     * Show modal dialog for adding new student entry
     */
    public void newEntry() {
        StudentEntryView studentEntryView = new StudentEntryView(view.tableModel);
        studentEntryView.setVisible(true);
    }
}
