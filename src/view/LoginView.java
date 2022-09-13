package view;

import model.Constants;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {

    private JPanel PANEL_LOGIN;
    private JPasswordField LOGIN_PASSWORD;
    private JTextField LOGIN_USERNAME;
    private JButton LOGIN_SIGNIN;
    private JCheckBox LOGIN_REMEMBER;

    public LoginView() {
        setTitle("Login | " + Constants.APP_NAME);
        setContentPane(PANEL_LOGIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 300);
        setVisible(true);
        pack();
    }
}
