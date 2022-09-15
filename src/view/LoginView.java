package view;

import controller.LoginController;
import model.Constants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {

    private JPanel panel;
    public JPasswordField password;
    public JTextField username;
    public JButton signIn;

    public LoginView() {
        setTitle("Login | " + Constants.APP_NAME);
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getRootPane().setDefaultButton(signIn);
        setSize(350, 200);
        setMinimumSize(getSize());
        pack();

        // init controller
        new LoginController(this);
    }

    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    public String getUsername() {
        return username.getText();
    }
}
