import java.util.Scanner;

public class ATM {

    public String action;

    public void start(){
        System.out.println("добро пожаловать, войдите в аккаунт...\nвведите логин");
        Scanner scan = new Scanner(System.in);
        action = scan.nextLine();
    }

}
