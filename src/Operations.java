import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Function;

public enum Operations {

    TRANSACTION(1, "Оплатить обслуживание карты"),
    DEPOSIT(2, "Пополнить карту"),
    WITHDRAW(3, "Снять деньги со счета"),
    CHECK_BALANCE(4, "Проверить баланс"),
    TRANSFER(5, "Перевести деньги другому пользователю"),
    EXIT(6, "Выйти");

    private final int id;
    private final String description;

    Operations(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    private static final Map <Integer, Operations> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Operations::getId, Function.identity()));

    static Optional<Operations> getByID(int id){
        return Optional.ofNullable(BY_ID.get(id));
    }


}
