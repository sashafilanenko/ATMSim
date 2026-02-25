package Logic;

import exceptions.*;

import java.math.BigDecimal;
import java.util.List;


public interface BankService {

    BigDecimal getMaintenanceFee();

    void registerUser(String login, String fullName, String rawPassword);

    String createAccount(String userLogin, BigDecimal initialBalance) throws AccountNotFoundException;

    User authenticate(String login, String password) throws AuthException;

    User getTargetUser(String login) throws AccountNotFoundException;

    void transaction(User user, String id) throws AccountNotFoundException, InsufficientFundsException;

    void deposit(User user, String id, BigDecimal amount) throws AccountNotFoundException;

    void withdraw(User user, String id, BigDecimal amount) throws AccountNotFoundException, InsufficientFundsException;

    List<BigDecimal> checkBalance(User user);

    void transfer(User currentUser,
                  String fromId,
                  String targetLogin,
                  String toId,
                  BigDecimal amount)
            throws AccountNotFoundException, InsufficientFundsException, TransactionException;
}

