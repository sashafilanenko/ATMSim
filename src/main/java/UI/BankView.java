package UI;

public interface BankView {

    LoginPanel getLoginPanel();

    DashboardPanel getDashboardPanel();

    void showLogin();

    void showDashboard(String userName);

    void showError(String msg);

    void showInfo(String msg);

    String promptInput(String msg);

    int showConfirm(String msg, String title, int optionType);

    void setVisible(boolean visible);
}

