import view.ErrorDialogView;
import view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();

            try {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            } catch (Exception exception) {
                new ErrorDialogView(exception);
            }
        });
    }

    /**
     * Set the look and feel of the application
     */
    private static void setLookAndFeel() {
        try {
            // check system os
            String os = System.getProperty("os.name").toLowerCase();

            // Force native GTK look and feel on Linux instead of Metal
            if (os.contains("nix")) {
                UIManager.setLookAndFeel("javax.swing.plaf.gtk.GTKLookAndFeel");
            } else {
                // Set System L&F on other OS'
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException |
                 ClassNotFoundException exception) {
            new ErrorDialogView(exception);
            System.exit(1);
        }
    }
}