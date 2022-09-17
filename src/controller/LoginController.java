package controller;

import model.Constants;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentsView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    public LoginController(LoginView view) {

        view.signIn.addActionListener(actionEvent -> {
            boolean valid;

            valid = login(view.getUsername(), view.getPassword());

            if (valid) {
                view.setVisible(false);
                view.dispose();
            }
        });
    }

    /**
     * Check for login credentials
     *
     * @param username username to be validated
     * @param password password to be validated
     * @return `true` if credentials are correct, else `false`
     */
    public boolean login(String username, String password) {
        StudentsView studentsView = new StudentsView();

        try (FileReader in = new FileReader(Constants.DB_ADMINS); BufferedReader reader = new BufferedReader(in)) {

            String line;
            boolean valid = false;

            // check if credentials are correct
            while ((line = reader.readLine()) != null) {
                String delimiter = Constants.DELIMITER;
                String[] credentials = line.split(delimiter);

                valid = (credentials[0].equals(username) && credentials[1].equals(password));
            }

            if (valid) {
                studentsView.setVisible(true);
                return true;
            } else {
                new ErrorDialogView(new Exception("Invalid credentials"));
            }

        } catch (IOException exception) {
            new ErrorDialogView(exception);
        }

        return false;
    }
}
