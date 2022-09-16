package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class ErrorDialogView extends JDialog {
    private JPanel contentPane;
    private JButton close;
    private JLabel errorMessage;

    public ErrorDialogView(Exception exception) {
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(close);

        setTitle("Something went wrong...");
        setLocationRelativeTo(null);
        setSize(350, 150);

        setResizable(false);
        setVisible(true);

        errorMessage.setText(exception.getMessage());

        close.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        contentPane.registerKeyboardAction(actionEvent -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

    }
}
