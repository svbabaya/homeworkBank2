package com.babayan.exercise.model;
import org.javamoney.moneta.Money;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private static Bank instance;
    private Bank() {};
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    private Money wallet;
    private List<Person> people = new ArrayList<>();

    public void setWallet(Money wallet) {
        this.wallet = wallet;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Money getWallet() {

        return wallet;
    }

    public List<Person> getPeople() {

        return people;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "wallet=" + wallet +
                ", people=" + people +
                '}';
    }
}
