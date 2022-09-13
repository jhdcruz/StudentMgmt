package view;

import model.Constants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;

public class ErrorDialogView {

    private static JDialog DIALOG;

    public ErrorDialogView(Exception exception) {

        JFrame frame = new JFrame();
        DIALOG = new JDialog(frame, Constants.APP_NAME, true);

        DIALOG.setLayout(new FlowLayout());
        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(e -> ErrorDialogView.DIALOG.setVisible(true));

        DIALOG.add(new JLabel(exception.getMessage()));
        DIALOG.add(closeButton);
        DIALOG.setSize(300, 300);
        DIALOG.setVisible(true);
    }
}