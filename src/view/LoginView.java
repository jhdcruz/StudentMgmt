package view;

import controller.LoginController;
import model.Constants;
import model.LoginModel;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {

    LoginModel model = new LoginModel();
    LoginController controller = new LoginController();

    private JPanel panel;
    private JPasswordField password;
    private JTextField username;
    private JButton signin;
    private JCheckBox remember;

    public LoginView() {
        setTitle("Login | " + Constants.APP_NAME);
        setContentPane(panel);
        getRootPane().setDefaultButton(signin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(500, 300);
        pack();

        // View listeners
        signin.addActionListener(actionEvent -> {
            boolean valid = controller.login(getUsername(), getPassword());

            if (valid) {
                this.setVisible(false);
                this.dispose();
            }
        });
    }

    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    public String getUsername() {
        return username.getText();
    }

    public JCheckBox getRemember() {
        return remember;
    }
}
