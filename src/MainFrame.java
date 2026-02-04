import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame() {
        setTitle("My Swing Bank ATM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel();
        dashboardPanel = new DashboardPanel();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        add(mainPanel);
        showLogin();
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
        loginPanel.clearFields();
    }

    public void showDashboard(String userName) {
        dashboardPanel.setWelcomeMessage("Привет, " + userName);
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    // Геттеры для контроллера
    public LoginPanel getLoginPanel() { return loginPanel; }
    public DashboardPanel getDashboardPanel() { return dashboardPanel; }

    // Утилитарные методы для диалогов
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void shawApplyButtons(String msg){
        JOptionPane.showMessageDialog(this, msg, "перевести?", JOptionPane.ERROR_MESSAGE);
    }
    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Инфо", JOptionPane.INFORMATION_MESSAGE);
    }
    public String promptInput(String msg) {
        return JOptionPane.showInputDialog(this, msg);
    }
}