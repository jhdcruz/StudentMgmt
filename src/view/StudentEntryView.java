package view;

import controller.StudentEntryController;
import model.Constants;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class StudentEntryView extends JDialog {
    public JPanel contentPane;
    public JButton entrySubmit;
    public JButton entryCancel;
    public JTextField id;
    public JTextField nameFirst;
    public JTextField email;
    public JSpinner yearLevel;
    public JComboBox<String> course;
    public JTextField nameMiddle;
    public JTextField nameLast;

    public StudentEntryView() {
        setTitle("Add Student | " + Constants.APP_NAME);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);

        getRootPane().setDefaultButton(entrySubmit);
        pack();

        // Limit year level values
        SpinnerModel value = new SpinnerNumberModel(1, 1, 4, 1);
        yearLevel.setModel(value);

        // available courses
        String[] courses = {"BSIT", "BSCS", "BSIS", "BSDSA", "BSEMC"};
        course.setModel(new DefaultComboBoxModel<>(courses));

        // Listeners
        new StudentEntryController(this);
    }
}
