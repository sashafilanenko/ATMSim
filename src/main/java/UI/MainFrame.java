package UI;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame implements BankView {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final LoginPanel loginPanel;
    private final DashboardPanel dashboardPanel;

    public MainFrame() {
        setTitle("My Swing Logic.Bank ATM");
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

    @Override
    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
        loginPanel.clearFields();
    }

    @Override
    public void showDashboard(String userName) {
        dashboardPanel.setWelcomeMessage("Привет, " + userName);
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    @Override
    public LoginPanel getLoginPanel() { return loginPanel; }

    @Override
    public DashboardPanel getDashboardPanel() { return dashboardPanel; }

    @Override
    public void showError(String msg) {
        showWindowModalMessage(msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void shawApplyButtons(String msg){
        showWindowModalMessage(msg, "перевести?", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInfo(String msg) {
        showWindowModalMessage(msg, "Инфо", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String promptInput(String msg) {
        JOptionPane optionPane = new JOptionPane(
                msg,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        optionPane.setWantsInput(true);

        JDialog dialog = optionPane.createDialog(this, "Ввод");
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        Object value = optionPane.getInputValue();
        if (value == JOptionPane.UNINITIALIZED_VALUE) {
            return null;
        }
        return (String) value;
    }

    @Override
    public int showConfirm(String msg, String title, int optionType) {
        JOptionPane optionPane = new JOptionPane(
                msg,
                JOptionPane.QUESTION_MESSAGE,
                optionType);

        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();
        if (selectedValue instanceof Integer) {
            return (Integer) selectedValue;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private void showWindowModalMessage(String msg, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane(
                msg,
                messageType,
                JOptionPane.DEFAULT_OPTION);

        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}