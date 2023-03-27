package com.babayan.exercise.model;
import org.javamoney.moneta.Money;

public class Person {
    private final String name;
    private final Money wallet;
    private Money appendFromBank;

    public Person(String name, Money wallet, Money appendFromBank) {
        this.name = name;
        this.wallet = wallet;
        this.appendFromBank = appendFromBank;
    }

    public String getName() {
        return name;
    }

    public Money getWallet() {
        return wallet;
    }

    public Money getAppendFromBank() {
        return appendFromBank;
    }

    public void setAppendFromBank(Money appendFromBank) {
        this.appendFromBank = appendFromBank;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", wallet=" + wallet +
                ", appendFromBank=" + appendFromBank +
                '}';
    }
}
