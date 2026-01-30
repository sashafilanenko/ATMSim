import java.util.Scanner;

public class ATM {

    private final Bank bank;
    public String login;
    public String password;

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


    }



}
