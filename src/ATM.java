import java.util.Scanner;

public class ATM {

    public String login;
    public String password;


    public void start(){

        System.out.println("добро пожаловать, войдите в аккаунт...\nвведите логин");

        Scanner scan = new Scanner(System.in);
        login = scan.nextLine();

        System.out.println("введите пароль");
        password = scan.nextLine();






    }



}
