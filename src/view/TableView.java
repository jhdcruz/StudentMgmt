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
    private JButton entryAdd;
    private JScrollPane TABLE_SCROLLPANE;
    private JPanel PANEL_TABLE;
    private JPanel CONTROL_PANEL;
    private JFormattedTextField ENTRY_SEARCH;
    private JButton entryEdit;
    private JButton entryDelete;
    private JButton adminSignOut;
    private JButton adminButton;

    public TableView() {
        setTitle("Student Management");
        setContentPane(PANEL_MAIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(1000, 600);
        pack();
    }
}
