package Logic;

import UI.*;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        AppLogger.init();

        Logger mainLog = AppLogger.getLogger(Main.class.getName());
        mainLog.info("Application starting with 3 ATM windows...");

        BankService bank = new Bank();
        initializeData(bank);

        SwingUtilities.invokeLater(() -> {
            for (int i = 1; i <= 3; i++) {
                MainFrame frame = new MainFrame();
                frame.setTitle("My Swing Bank ATM #" + i);
                new SwingBankController(bank, frame);
                frame.setLocation(100 + i * 40, 100 + i * 40);
                frame.setVisible(true);
            }
        });
    }

    private static void initializeData(BankService bank) {
        try {
            bank.registerUser("alex", "Alexey", "123");
            bank.createAccount("alex", BigDecimal.valueOf(1000));

            bank.registerUser("alex2", "Alexey2", "123");
            bank.createAccount("alex2", BigDecimal.valueOf(2000));

            bank.registerUser("alex3", "Alexey3", "123");
            bank.createAccount("alex3", BigDecimal.valueOf(100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}