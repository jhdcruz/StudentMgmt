package controller;

import model.AdminModel;
import view.ErrorDialogView;
import view.StudentsView;

import java.io.IOException;

public class LoginController {

    public boolean login(String username, String password) {
        AdminModel adminModel = new AdminModel();
        StudentsView studentsView = new StudentsView();

        try {
            boolean login = adminModel.login(username, password);

            if (login) {
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
