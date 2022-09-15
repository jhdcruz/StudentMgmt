package controller;

import model.Constants;
import view.ErrorDialogView;
import view.LoginView;
import view.StudentsView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    LoginView view;

    public LoginController(LoginView view) {
        this.view = view;

        view.signIn.addActionListener(actionEvent -> {
            boolean valid = false;

            try {
                valid = login(view.getUsername(), view.getPassword());
            } catch (IOException e) {
                new ErrorDialogView(new Exception("An error occurred while logging in."));
            }

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
    public boolean login(String username, String password) throws IOException {
        StudentsView studentsView = new StudentsView();

        FileReader in = new FileReader(Constants.DB_ADMINS);
        BufferedReader reader = new BufferedReader(in);

        try (in; reader) {
            String line;
            boolean valid = false;

            // check if credentials are correct
            while ((line = reader.readLine()) != null) {
                String delimiter = ",";
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
