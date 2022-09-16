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

    public JPopupMenu popupMenu;
    public JMenuItem popupMenuEdit;
    public JMenuItem popupMenuDelete;

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

        tableModel = new DefaultTableModel(Constants.TABLE_HEADERS, 0);
        studentsTable.setModel(tableModel);

        // Popup menu (context menu)
        popupMenu = new JPopupMenu();
        studentsTable.setComponentPopupMenu(popupMenu);

        popupMenuEdit = new JMenuItem("Edit");
        popupMenuDelete = new JMenuItem("Delete");

        popupMenu.add(popupMenuEdit);
        popupMenu.add(popupMenuDelete);

        // init controllers
        new StudentController(this);
    }

    public String getEntrySearch() {
        return entrySearch.getText();
    }
}
