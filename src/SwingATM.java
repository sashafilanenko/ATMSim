import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import exceptions.*;

public class SwingATM extends JFrame {
    private final Bank bank;
    private User currentUser;

    private JPanel cards;
    private CardLayout cardLayout;

    public SwingATM(Bank bank) {
        this.bank = bank;
        initUI();
    }

    private void initUI() {
        setTitle("Банкомат Swing Edition");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createLoginPanel(), "LOGIN");
        cards.add(createMenuPanel(), "MENU");

        add(cards);

        cardLayout.show(cards, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField loginField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Войти");

        panel.add(new JLabel("Логин:"));
        panel.add(loginField);
        panel.add(new JLabel("Пароль:"));
        panel.add(passField);
        panel.add(new JLabel(""));
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String pass = new String(passField.getPassword());
            authenticate(login, pass);
        });

        return panel;
    }

    // --- Экран Меню ---
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5)); // Сетка для кнопок
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton btnBalance = new JButton("Проверить баланс");
        JButton btnDeposit = new JButton("Пополнить");
        JButton btnWithdraw = new JButton("Снять");
        JButton btnTransfer = new JButton("Перевод");
        JButton btnExit = new JButton("Выход");

        // Привязываем действия
        btnBalance.addActionListener(e -> showBalances());
        btnDeposit.addActionListener(e -> handleDeposit());
        btnWithdraw.addActionListener(e -> handleWithdraw());
        btnTransfer.addActionListener(e -> handleTransfer());

        btnExit.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cards, "LOGIN");
        });

        panel.add(new JLabel("Выберите операцию:", SwingConstants.CENTER));
        panel.add(btnBalance);
        panel.add(btnDeposit);
        panel.add(btnWithdraw);
        panel.add(btnTransfer);
        panel.add(btnExit);

        return panel;
    }


    private void authenticate(String login, String password) {
        try {
            this.currentUser = bank.authenticate(login, password);
            JOptionPane.showMessageDialog(this, "Привет, " + currentUser.getLogin());
            cardLayout.show(cards, "MENU");
        } catch (AuthException e) {
            handleException(e);
        }
    }

    private void showBalances() {
        StringBuilder sb = new StringBuilder("Ваши счета:\n");
        var accounts = currentUser.getAccounts();
        if (accounts.isEmpty()) sb.append("Счетов нет.");

        for (Account acc : accounts) {
            sb.append(String.format("ID: %s | Баланс: %,.2f\n", acc.getAccountID(), acc.getBalance()));
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void handleDeposit() {
        try {
            String id = JOptionPane.showInputDialog(this, "ID счета:");
            if (id == null) return; // Нажали отмену

            String amountStr = JOptionPane.showInputDialog(this, "Сумма:");
            if (amountStr == null) return;

            BigDecimal amount = new BigDecimal(amountStr);

            bank.deposit(currentUser, id, amount);
            JOptionPane.showMessageDialog(this, "Успешно пополнено!");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleWithdraw() {
        try {
            showBalances();

            String id = JOptionPane.showInputDialog(this, "ID счета для снятия:");
            if (id == null) return;

            String amountStr = JOptionPane.showInputDialog(this, "Сумма:");
            if (amountStr == null) return;

            BigDecimal amount = new BigDecimal(amountStr);

            bank.withdraw(currentUser, id, amount);
            JOptionPane.showMessageDialog(this, "Средства выданы.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleTransfer() {
        try {
            String fromId = JOptionPane.showInputDialog(this, "С какого счета:");
            String toUser = JOptionPane.showInputDialog(this, "Логин получателя:");
            String toId = JOptionPane.showInputDialog(this, "ID счета получателя:");
            String amountStr = JOptionPane.showInputDialog(this, "Сумма:");

            if (fromId == null || toUser == null || toId == null || amountStr == null) return;

            BigDecimal amount = new BigDecimal(amountStr);
            bank.transfer(currentUser, fromId, toUser, toId, amount);

            JOptionPane.showMessageDialog(this, "Перевод выполнен.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        String title = "Ошибка";
        String message = e.getMessage();

        if (e instanceof AccountNotFoundException || e instanceof InsufficientFundsException) {
            message = "Операция невозможна: " + e.getMessage();
        } else if (e instanceof AuthException) {
            message = "Ошибка входа: " + e.getMessage();
        } else if (e instanceof NumberFormatException) {
            message = "Некорректный формат числа!";
        }

        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);

    }
}