package view;

import controller.LoginController;
import model.Constants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {

    LoginController loginController = new LoginController();

    private JPanel panel;
    private JPasswordField password;
    private JTextField username;
    private JButton signin;

    public LoginView() {
        setTitle("Login | " + Constants.APP_NAME);
        setContentPane(panel);
        getRootPane().setDefaultButton(signin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(350, 200);
        setMinimumSize(getSize());
        pack();

        // View listeners
        signin.addActionListener(actionEvent -> {
            boolean valid = loginController.login(getUsername(), getPassword());

            if (valid) {
                setVisible(false);
                dispose();
            }
        });
    }

    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    public String getUsername() {
        return username.getText();
    }
}
