package view;

import controller.StudentController;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StudentsView extends JFrame {
    private JPanel contentPane;
    private JPanel PANEL_TABLE;
    private JPanel CONTROL_PANEL;

    public JTable studentsTable;
    public DefaultTableModel tableModel;
    public JScrollPane tableScrollPane;
    public JFormattedTextField entrySearch;
    public JButton entryAdd;
    public JButton entryEdit;
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

        Object[] tableHeaders = {"ID #", "Email", "Last Name", "First Name", "Middle Name", "Course", "Year Level", "Section", "Date Created"};
        tableModel = new DefaultTableModel(null, tableHeaders);
        studentsTable.setModel(tableModel);


        // init controllers
        new StudentController(this);

    }
}
