package view;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class StudentsView extends JFrame {
    private JPanel PANEL_MAIN;
    private JTable TABLE_STUDENTS;
    private JScrollPane TABLE_SCROLLPANE;
    private JPanel PANEL_TABLE;
    private JPanel CONTROL_PANEL;

    private JFormattedTextField entrySearch;
    private JButton entryAdd;
    private JButton entryEdit;
    private JButton entryDelete;
    private JButton adminSignOut;
    private JButton adminButton;

    public StudentsView() {
        setTitle("Student Management");
        setContentPane(PANEL_MAIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(900, 400);
        setMinimumSize(getSize());
        pack();
    }
}
