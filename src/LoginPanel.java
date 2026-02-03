import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JButton loginButton = new JButton("Войти");

    public LoginPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Логин:"), gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Пароль:"), gbc);
        gbc.gridx = 1;
        add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        add(loginButton, gbc);
    }

    public String getLogin() { return userField.getText(); }
    public String getPassword() { return new String(passField.getPassword()); }

    public void addLoginListener(ActionListener l) {
        loginButton.addActionListener(l);
    }

    public void clearFields() {
        userField.setText("");
        passField.setText("");
    }
}