package controller;

import model.LoginModel;
import view.ErrorDialogView;
import view.TableView;

import java.io.IOException;

public class LoginController {

    public boolean login(String username, String password) {
        LoginModel loginModel = new LoginModel();
        TableView tableView = new TableView();

        try {
            boolean login = loginModel.login(username, password);

            if (login) {
                tableView.setVisible(true);
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
