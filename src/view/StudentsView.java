package view;

import controller.StudentController;
import model.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentsView extends JFrame {
    private JPanel contentPane;
    public JTable studentsTable;
    public DefaultTableModel tableModel;
    public JScrollPane tableScrollPane;

    public JFormattedTextField entrySearch;
    public JButton entryAdd;
    public JButton entryDelete;
    public JButton adminSignOut;
    public JButton refresh;

    public StudentsView() {
        setTitle("Student Management");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(1100, 550);
        setMinimumSize(getSize());
        pack();

        tableModel = new DefaultTableModel(null, Constants.TABLE_HEADERS);
        studentsTable.setModel(tableModel);

        // init controllers
        new StudentController(this);
    }

    public String getEntrySearch() {
        return entrySearch.getText();
    }
}
