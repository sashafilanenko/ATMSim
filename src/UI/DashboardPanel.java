package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;



public class DashboardPanel extends JPanel {
    private JLabel welcomeLabel = new JLabel("Привет!");
    private JTextArea infoArea = new JTextArea(10, 30);
    private JButton depositBtn = new JButton("Пополнить");
    private JButton withdrawBtn = new JButton("Снять");
    private JButton transactionBtn = new JButton("Оплата обслуживания");
    private JButton transferBtn = new JButton("Перевод");
    private JButton refreshBtn = new JButton("Обновить баланс");
    private JButton logoutBtn = new JButton("Выход");

    public DashboardPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(welcomeLabel);
        topPanel.add(logoutBtn);
        add(topPanel, BorderLayout.NORTH);

        infoArea.setEditable(false);
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transactionBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setWelcomeMessage(String msg) { welcomeLabel.setText(msg); }
    public void updateInfoArea(String info) { infoArea.setText(info); }

    public void addLogoutListener(ActionListener l) { logoutBtn.addActionListener(l); }
    public void addDepositListener(ActionListener l) { depositBtn.addActionListener(l); }
    public void addWithdrawListener(ActionListener l) { withdrawBtn.addActionListener(l); }
    public void addTransactionListener(ActionListener l) { transactionBtn.addActionListener(l); }
    public void addTransferListener(ActionListener l) { transferBtn.addActionListener(l); }
    public void addBalanceListener(ActionListener l) { refreshBtn.addActionListener(l); }
}