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

        // disable cell editing on date created (col 8)
        studentsTable.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(java.util.EventObject event) {
                return false;
            }
        });

        // make table cell 5 a combo box of available courses
        studentsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox<>(Constants.ENTRY_COURSES)) {
            @Override
            public boolean isCellEditable(java.util.EventObject event) {
                return true;
            }
        });

        // make table cell 6 a spinner for year level
        studentsTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JComboBox<>(Constants.ENTRY_YEAR_LEVELS)) {
            @Override
            public boolean isCellEditable(java.util.EventObject event) {
                return true;
            }
        });

        // init controllers
        new StudentController(this);
    }

    public String getEntrySearch() {
        return entrySearch.getText();
    }
}
