package view;

import model.Constants;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.KeyEvent;

public class AboutDialogView extends JDialog {
    private JPanel contentPane;
    private JLabel appName;
    private JLabel appVer;
    private JLabel appCopy;
    private JLabel appDesc;
    private JTextPane appAuthors;

    public AboutDialogView() {
        setTitle("About");
        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 400);
        setMinimumSize(getSize());

        setLocationRelativeTo(null);
        setResizable(false);

        pack();

        appName.setText(Constants.APP_NAME);
        // make app name bold
        appName.setFont(appName.getFont().deriveFont(java.awt.Font.BOLD));

        appVer.setText("v" + Constants.APP_VERSION);
        appDesc.setText(Constants.APP_DESCRIPTION);
        appCopy.setText(Constants.APP_COPYRIGHT);

        // center authors list
        StyledDocument doc = appAuthors.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // add authors in each lines in JTextArea
        StringBuilder authors = new StringBuilder();
        for (String author : Constants.APP_AUTHORS) {
            authors.append(author).append("\n");
            appAuthors.setText(String.valueOf(authors));
        }

        // ESC to close dialog
        contentPane.registerKeyboardAction(actionEvent -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}