package Logic;

import UI.*;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        AppLogger.init();

        Logger mainLog = AppLogger.getLogger(Main.class.getName());
        mainLog.info("Application starting...");

        Bank bank = new Bank();
        initializeData(bank);

        SwingUtilities.invokeLater(() -> {
            MainFrame view = new MainFrame();
            new SwingBankController(bank, view);
            view.setVisible(true);
        });
    }

    private static void initializeData(Bank bank) {
        try {
            bank.registerUser("alex", "Alexey", "123");
            bank.createAccount("alex", BigDecimal.valueOf(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}