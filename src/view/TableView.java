package view;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableView extends JFrame {
    private JPanel PANEL_MAIN;
    private JTable TABLE_STUDENTS;
    private JButton ENTRY_ADD;
    private JScrollPane TABLE_SCROLLPANE;
    private JPanel PANEL_TABLE;
    private JPanel CONTROL_PANEL;
    private JFormattedTextField ENTRY_SEARCH;
    private JButton ENTRY_EDIT;
    private JButton ENTRY_DELETE;

    public TableView() {
        setTitle("Student Management");
        setContentPane(PANEL_MAIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1000, 600);
        setVisible(true);
        pack();
    }
}
