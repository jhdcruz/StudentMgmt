package view;

import controller.StudentEntryController;
import model.Constants;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

public class StudentEntryView extends JDialog {
    public JPanel contentPane;
    public JButton entrySubmit;
    public JButton entryCancel;

    public JTextField nameFirst;
    public JTextField nameMiddle;
    public JTextField nameLast;
    public JTextField email;
    public JTextField section;

    public JComboBox<String> yearLevel;
    public JComboBox<String> course;

    public JLabel emptySection;
    public JLabel emptyNameLast;
    public JLabel emptyNameMiddle;
    public JLabel emptyNameFirst;
    public JLabel emptyEmail;

    public StudentEntryView(DefaultTableModel tableModel) {
        setTitle("Add Student | " + Constants.APP_NAME);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);

        setSize(400, 520);
        setMinimumSize(getSize());
        pack();

        getRootPane().setDefaultButton(entrySubmit);

        // Limit year level values
        yearLevel.setModel(new DefaultComboBoxModel<>(Constants.ENTRY_YEAR_LEVELS));

        // available courses
        course.setModel(new DefaultComboBoxModel<>(Constants.ENTRY_COURSES));

        // Listeners
        new StudentEntryController(this, tableModel);
    }

    public String getNameFirst() {
        return nameFirst.getText();
    }

    public String getEmail() {
        return email.getText();
    }

    public int getYearLevel() {
        return Integer.parseInt((String) Objects.requireNonNull(yearLevel.getSelectedItem()));
    }

    public String getCourse() {
        return (String) course.getSelectedItem();
    }

    public String getNameMiddle() {
        return nameMiddle.getText();
    }

    public String getNameLast() {
        return nameLast.getText();
    }

    public String getSection() {
        return section.getText();
    }
}
