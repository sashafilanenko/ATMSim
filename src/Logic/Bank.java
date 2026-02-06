package Logic;

import exceptions.*;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class Bank {

    private final Random random = new Random();

    private Map<String, User> users = new HashMap<>();
    private static final BigDecimal MAINTENANCE_FEE = BigDecimal.valueOf(20);

    public BigDecimal getMaintenanceFee(){
        return MAINTENANCE_FEE;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка алгоритма хеширования", e);
        }
    }

    public void registerUser(String login, String fullName, String rawPassword) {
        if (users.containsKey(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        String passwordHash = hashPassword(rawPassword);
        User newUser = new User(login, fullName, passwordHash);
        users.put(login, newUser);
    }

    public String createAccount(String userLogin, BigDecimal initialBalance) throws AccountNotFoundException {
        User user = users.get(userLogin);
        if (user == null) {
            throw new AccountNotFoundException("Пользователь не найден");
        }

        String newAccountId = generateAccountId();

        Account newAccount = new Account(newAccountId, initialBalance);
        user.addAccount(newAccount);

        return newAccountId;
    }
    private String generateAccountId() {
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    public User authenticate(String login, String password) throws AuthException {
        User user = users.get(login);
        if (user != null && user.verifyPassword(hashPassword(password))) {
            return user;
        }
        throw new AuthException("Неверный логин или пароль");
    }

    public User getTargetUser(String login) throws AccountNotFoundException{

        User user = users.get(login);
        if (user == null) {
            throw new AccountNotFoundException("Пользователь не найден: " + login);
        }
        if (user.getNumOfAccounts() == 0) {
            throw new AccountNotFoundException("У пользователя нет аккаунтов: " + login);
        }
        return user;
    }

    public void transaction(User user, String id) throws AccountNotFoundException, InsufficientFundsException{
        Account account = user.getAccountById(id);
        account.withdraw(MAINTENANCE_FEE);
    }
    public void deposit(User user, String id, BigDecimal amount) throws AccountNotFoundException{
        Account account = user.getAccountById(id);
        account.deposit(amount);
    }

    public void withdraw(User user, String id, BigDecimal amount) throws AccountNotFoundException, InsufficientFundsException{
        Account account = user.getAccountById(id);
        account.withdraw(amount);
    }

    public List<BigDecimal> checkBalance(User user){
        if (user == null) throw new IllegalArgumentException("пользователь нулевой");
        List<BigDecimal> balances = new ArrayList<>();
        for (Account acc : user.getAccounts()) {
            balances.add(acc.getBalance());
        }
        return balances;
    }

    public void transfer(User currentUser, String fromId, String targetLogin, String toId, BigDecimal amount)
            throws AccountNotFoundException, InsufficientFundsException, TransactionException {

        User targetUser = users.get(targetLogin);
        if (targetUser == null) {
            throw new AccountNotFoundException("Получатель с логином " + targetLogin + " не найден");
        }

        Account sourceAcc = currentUser.getAccountById(fromId);
        Account targetAcc = targetUser.getAccountById(toId);

        if (sourceAcc.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств для перевода");
        }

        if (sourceAcc.equals(targetAcc)) {
            throw new TransactionException("Нельзя перевести деньги на тот же самый счет");
        }

        sourceAcc.withdraw(amount);
        targetAcc.deposit(amount);
    }

}
