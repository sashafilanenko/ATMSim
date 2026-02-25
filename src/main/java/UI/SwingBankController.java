package UI;

import Logic.BankService;
import Logic.Account;
import Logic.User;

import exceptions.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Logger;

public class SwingBankController {
    private final BankService bank;
    private final BankView view;
    private User currentUser;

    private static final Logger LOG = AppLogger.getLogger(SwingBankController.class.getName());
    private final String sessionId = UUID.randomUUID().toString();


    public SwingBankController(BankService bank, BankView view) {
        this.bank = bank;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getLoginPanel().addLoginListener(e -> handleLogin());

        DashboardPanel dashboard = view.getDashboardPanel();

        dashboard.addLogoutListener(e -> handleLogout());
        dashboard.addBalanceListener(e -> refreshBalance());
        dashboard.addTransactionListener(e -> handleTransaction());
        dashboard.addDepositListener(e -> handleDeposit());
        dashboard.addWithdrawListener(e -> handleWithdraw());
        dashboard.addTransferListener(e -> handleTransfer());
    }

    private void handleLogin() {
        String login = view.getLoginPanel().getLogin();
        LOG.info(String.format("session=%s action=LOGIN_attempt user=%s", sessionId, login));
        String password = view.getLoginPanel().getPassword();

        if (login.isEmpty() || password.isEmpty()) {
            view.showError("������� ����� � ������.");
            return;
        }

        try {
            currentUser = bank.authenticate(login, password);
            LOG.info(String.format("session=%s action=LOGIN_success user=%s", sessionId, currentUser.getLogin()));
            view.showDashboard(currentUser.getFullName());
            refreshBalance();
        } catch (AuthException e) {
            view.showError("������ �����: " + e.getMessage());
        } catch (Exception e) {
            view.showError("��������� ������: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        int confirm = view.showConfirm(
                "�� �������, ��� ������ �����?",
                "�����",
                JOptionPane.YES_NO_OPTION);
        LOG.info(String.format(
                "session=%s action=LOGOUT user=%s",
                sessionId,
                currentUser.getLogin()
        ));

        if (confirm == JOptionPane.YES_OPTION) {
            currentUser = null;
            view.showLogin();
        }
    }

    private void refreshBalance() {
        if (currentUser == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("--- ���� ����� ---\n");

        var accounts = currentUser.getAccounts();
        if (accounts.isEmpty()) {
            sb.append("������ ���.\n");
        } else {
            for (Account acc : accounts) {
                sb.append(String.format("ID: %-8s | ������: %,.2f\n",
                        acc.getAccountID(), acc.getBalance()));
            }
        }
        sb.append("------------------");

        view.getDashboardPanel().updateInfoArea(sb.toString());
    }

    private void handleDeposit() {
        String accountId = askForAccountId("����������");
        if (accountId == null) return;

        BigDecimal amount = askForAmount("������� ����� ����������:");
        if (amount == null) return;

        try {
            bank.deposit(currentUser, accountId, amount);
            view.showInfo("���� " + accountId + " ������� �������� �� " + amount);
            refreshBalance();
            LOG.info(String.format(
                    "session=%s user=%s action=DEPOSIT_SUCCESS account=%s amount=%s",
                    sessionId,
                    currentUser.getLogin(),
                    mask(accountId),
                    amount
            ));

        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("������ ��������: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private void handleWithdraw() {
        String accountId = askForAccountId("������");
        if (accountId == null) return;

        BigDecimal amount = askForAmount("������� ����� ������:");
        if (amount == null) return;

        try {
            bank.withdraw(currentUser, accountId, amount);
            view.showInfo("������ " + amount + " �� ����� " + accountId + " ���������.");
            refreshBalance();
            LOG.info(String.format(
                    "session=%s user=%s action=WITHDRAW_SUCCESS account=%s amount=%s",
                    sessionId,
                    currentUser.getLogin(),
                    mask(accountId),
                    amount
            ));
        } catch (InsufficientFundsException e) {
            view.showError("������������ �������: " + e.getMessage());
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("������: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    public void handleTransaction(){
        String accountId = askForAccountId("������ �����");
        if (accountId == null) return;

        try {
            bank.transaction(currentUser, accountId);
            view.showInfo("������ ������");
            refreshBalance();
            LOG.info(String.format(
                    "session=%s user=%s action=TRANSACTION_SUCCESS account=%s amount=%s",
                    sessionId,
                    currentUser.getLogin(),
                    mask(accountId),
                    bank.getMaintenanceFee()
            ));
        } catch (InsufficientFundsException e) {
            view.showError("������������ �������: " + e.getMessage());
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("������: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private void handleTransfer() {
        String fromId = askForAccountId("�������� (��� ����)");
        if (fromId == null) return;

        String targetLogin = view.promptInput("������� ����� ����������:");
        if (targetLogin == null || targetLogin.trim().isEmpty()) return;

        String toId = view.promptInput("������� ID ����� ����������:");
        if (toId == null || toId.trim().isEmpty()) return;

        BigDecimal amount = askForAmount("������� ����� ��������:");
        if (amount == null) return;

        int confirm = view.showConfirm(
                String.format("��������� %s ������������ %s?\n� �����: %s\n�� ����: %s",
                        amount, targetLogin, fromId, toId),
                "������������� ��������",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            bank.transfer(currentUser, fromId, targetLogin, toId, amount);
            view.showInfo("������� �������� �������!");
            refreshBalance();
            LOG.info(String.format(
                    "session=%s user=%s action=TRANSFER_SUCCESS from=%s to_user=%s to=%s amount=%s",
                    sessionId,
                    currentUser.getLogin(),
                    mask(fromId),
                    targetLogin,
                    mask(toId),
                    amount
            ));
        } catch (AccountNotFoundException e) {
            view.showError("������ ����������: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            view.showError("������������ �������: " + e.getMessage());
        } catch (TransactionException e) {
            view.showError("������ ����������: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private String askForAccountId(String operationName) {
        String input = view.promptInput("������� ID ����� ��� " + operationName + ":");
        if (input == null) return null;

        input = input.trim();
        if (input.isEmpty()) {
            view.showError("ID ����� �� ����� ���� ������");
            return null;
        }
        return input;
    }

    private BigDecimal askForAmount(String message) {
        while (true) {
            String input = view.promptInput(message);
            if (input == null) return null;

            input = input.trim().replace(",", ".");

            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    view.showError("����� ������ ���� �������������!");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                view.showError("������������ ������ �����. ������: 100.50");
            }
        }
    }

    private String mask(String accountId) {
        if (accountId == null) return "null";
        if (accountId.length() <= 4) return "****";
        return "****" + accountId.substring(accountId.length() - 4);
    }

    private void handleSystemError(Exception e) {
        view.showError("����������� ������: " + e.getMessage());
        e.printStackTrace();
    }
}