import javax.swing.SwingUtilities;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // 1. Инициализация Данных (Model)
        Bank bank = new Bank();
        initializeData(bank);

        // 2. Запуск GUI в потоке AWT
        SwingUtilities.invokeLater(() -> {
            // Создаем View
            MainFrame view = new MainFrame();

            // Создаем Controller (он сам свяжет View и Model)
            new SwingBankController(bank, view);

            // Показываем окно
            view.setVisible(true);
        });
    }

    private static void initializeData(Bank bank) {
        // Та же логика инициализации, что и у вас
        try {
            bank.registerUser("alex", "Alexey", "123");
            bank.createAccount("alex", BigDecimal.valueOf(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}