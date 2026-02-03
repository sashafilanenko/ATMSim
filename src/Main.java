import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        Bank bank = new Bank();
        ConsoleUIService ui = new ConsoleUIService();

        initializeData(bank);

        ATM atm = new ATM(bank, ui);
        atm.start();

    }
    private static void initializeData(Bank bank) {
        try {

            bank.registerUser("alex_dev", "Alexey Petrov", "pass123");
            bank.registerUser("sam_boss", "Sam Smith", "secure555");

            bank.createAccount("alex_dev", BigDecimal.valueOf(0));
            bank.createAccount("alex_dev", BigDecimal.valueOf(500.00));

            bank.createAccount("sam_boss", BigDecimal.valueOf(10000.00));

            System.out.println("Система инициализирована. Тестовые пользователи загружены.\n");

        } catch (Exception e) {
            System.err.println("Ошибка инициализации данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

