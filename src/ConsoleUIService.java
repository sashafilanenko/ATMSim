import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleUIService {

    private final Scanner scanner;

    ConsoleUIService(){
        this.scanner = new Scanner(System.in);
    }

    public void print(String message){
        System.out.println(message);
    }

    String prompt(String message){
        System.out.println(message);
        return scanner.nextLine().trim();
    }

    public BigDecimal promptBigDecimal(String message) {
        System.out.println(message);
        String input = scanner.nextLine().trim().replace(",", ".");
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный формат суммы. Попробуйте снова.");
            return promptBigDecimal(message);
        }
    }

    public int promptInt(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showMenu() {
        System.out.println("\nВыберите действие:");
        for (Operations op : Operations.values()) {
            System.out.println(op.getId() + ") " + op.getDescription());
        }
    }

}
