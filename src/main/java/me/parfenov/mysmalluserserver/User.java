package me.parfenov.mysmalluserserver;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Name must not be null.")
    @Size(min = 1, max = 30, message = "Name length must be between {min} and {max}.")
    @Pattern(regexp = "^[A-ZА-ЯЁ]([A-Za-zА-Яа-яёЁ0-9_\\-\\.]*[A-Za-zА-Яа-яёЁ0-9]{1})?$",
            message = "Name contains invalid characters or (and) does not begin with a capital letter.")
    private String name;

    @NotNull(message = "Age must not be null.")
    @Min(value = 18, message = "Age should be equal or more than {value}.")
    @Max(value = 140, message = "Age must be equal to or less than {value}.")
    private Integer age;

    @Email(message = "E-mail must be valid.")
    private String email;

    private Date registrationTime = new Date();

    @NotNull(message = "\"Has a car\" must not be null.")
    private boolean hasCar;

    @NotNull
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "USD")})
    private Money moneyAmount;

    public String getMoneyAmount() { return moneyAmount.toString(); }

    public void setMoneyAmount(String moneyAmountStr) { moneyAmount = Money.parse(moneyAmountStr); }

}

