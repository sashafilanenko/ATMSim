import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATM {

    private final Bank bank;
    private String login;
    private String password;

    public List<BigDecimal> temp = new ArrayList<>();

    ATM(Bank bank){
        this.bank = bank;
    }

    public void start(){

        System.out.println("добро пожаловать, войдите в аккаунт...\nвведите логин");

        Scanner scan = new Scanner(System.in);
        login = scan.nextLine();

        System.out.println("введите пароль");
        password = scan.nextLine();

        User currentUser = bank.authenticate(login, password);

        if (currentUser != null) {
            System.out.println("Успешный вход");
        } else {
            System.out.println("Неверный логин или пароль");
        }

        System.out.println("выберите действия: \n" +
                "1) оплатить обслуживание карты\n" +
                "2) пополнить карту\n" +
                "3) снять деньги со счета\n" +
                "4) проверить баланс\n" +
                "5) перевести деньги другому пользователю");

        User targetUser = bank.getTargetUser("Sam");

        int action = scan.nextInt();
        switch (action){
            case 1 -> bank.transaction(currentUser, String.valueOf(1));
            case 2 -> bank.deposit(currentUser, String.valueOf(1), BigDecimal.valueOf(-100));
            case 3 -> bank.withdraw(currentUser, String.valueOf(1), BigDecimal.valueOf(10));
            case 4 -> temp = bank.checkBalance(currentUser);
            case 5 -> bank.transfer(currentUser, String.valueOf(1), targetUser, String.valueOf(1), BigDecimal.valueOf(15));
        }


        temp = bank.checkBalance(currentUser);

        System.out.println(temp);;

    }



}
