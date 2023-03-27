package com.babayan.exercise;

import com.babayan.exercise.model.Bank;
import com.babayan.exercise.model.Person;
import org.javamoney.moneta.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CurrencyUnit rouble = Monetary.getCurrency("RUB");
        Bank bank = Bank.getInstance();

        parsingXml(bank, rouble);

        calculation(bank, rouble);

        makeResult(bank, rouble);

    }

    static void parsingXml(Bank bank, CurrencyUnit rouble) {

        List<Person> people = new ArrayList<>();

        File file = new File("src/main/resources/data.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document dataXml = null;
        try {
            dataXml = dbf.newDocumentBuilder().parse(file);
        } catch (Exception e) {
            System.out.println("Open parsing error");
        }

        Node rootNode = dataXml.getFirstChild();

        Money walletBank = Money.of(Double.parseDouble(rootNode.getAttributes().item(0).getNodeValue()), rouble);

        NodeList rootChildren = rootNode.getChildNodes();

        for (int i = 0; i < rootChildren.getLength(); i++) {
            if (rootChildren.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Person person = new Person(rootChildren.item(i).getAttributes().getNamedItem("name").getNodeValue(),
                    Money.of(Double.parseDouble(rootChildren.item(i).getAttributes().getNamedItem("wallet").getNodeValue()), rouble),
                    Money.of(0, rouble));
            people.add(person);
        }

        bank.setWallet(walletBank);
        bank.setPeople(people);

    }

    static void calculation(Bank bank, CurrencyUnit rouble) {
        // Определить максимальную сумму в кошельках
        Money maxWalletAmount = Money.of(0, rouble);
        for (Person p : bank.getPeople()) {
            if (p.getWallet().isGreaterThan(maxWalletAmount)) {
                maxWalletAmount = p.getWallet();
            }
        }
//        System.out.println(maxWalletAmount.getNumber());

        // Добавлять по 1 копейке во все кошельки, кроме кошельков c maxWalletAmount,
        // пока не закончатся деньги, либо все кошельки не станут равны maxWalletAmount

        int equalAmounts = 0;
        while (bank.getWallet().isGreaterThan(Money.of(0, rouble)) &&
                equalAmounts != bank.getPeople().size()) {
            for (Person p : bank.getPeople()) {
                if (p.getWallet().add(p.getAppendFromBank()).isLessThan(maxWalletAmount) &&
                bank.getWallet().isGreaterThan(Money.of(0, rouble))) {
                    bank.setWallet(bank.getWallet().subtract(Money.of(0.01, rouble)));
                    p.setAppendFromBank(p.getAppendFromBank().add(Money.of(0.01, rouble)));
                }
            }

            equalAmounts = 0;

            for (Person p: bank.getPeople()) {
                if (p.getWallet().add(p.getAppendFromBank()).equals(maxWalletAmount)) {
                    equalAmounts++;
                }
            }
        }

//        for (Person p : bank.getPeople()) {
//            System.out.println(p.getName() + " " + p.getWallet().getNumber()
//            + " " + p.getAppendFromBank().getNumber()
//            + " " + p.getWallet().add(p.getAppendFromBank()).getNumber());
//        }
//        System.out.println();
//        System.out.println(bank.getWallet().getNumber());

        // Распределить оставшиеся деньги по всем кошелькам
        while (bank.getWallet().isGreaterThan(Money.of(0, rouble))) {
            for (Person p : bank.getPeople()) {
                if (bank.getWallet().isGreaterThan(Money.of(0, rouble))) {
                    bank.setWallet(bank.getWallet().subtract(Money.of(0.01, rouble)));
                    p.setAppendFromBank(p.getAppendFromBank().add(Money.of(0.01, rouble)));
                }
            }
        }

//        for (Person p : bank.getPeople()) {
//            System.out.println(p.getName() + " " + p.getWallet().getNumber()
//            + " " + p.getAppendFromBank().getNumber()
//            + " " + p.getWallet().add(p.getAppendFromBank()).getNumber());
//        }
//        System.out.println();
//        System.out.println(bank.getWallet().getNumber());

    }

    static void makeResult(Bank bank, CurrencyUnit rouble) {
        System.out.printf("<total>\n");
        System.out.printf("<result>\n");
        for (Person p : bank.getPeople()) {
            System.out.printf("<Person name=\"%s\" wallet=\"%s\" appendFromBank=\"%s\"/>\n",
                    p.getName(), p.getWallet().getNumber(), p.getAppendFromBank().getNumber());
        }
        System.out.printf("</result>\n");
        System.out.printf("<minimum>\n");

        Money minAppendFromBank = bank.getPeople().get(0).getAppendFromBank();
        for (Person p : bank.getPeople()) {
            if (p.getAppendFromBank().isLessThan(minAppendFromBank)) {
                minAppendFromBank = p.getAppendFromBank();
            }
        }

        for (Person p : bank.getPeople()) {
            if (p.getAppendFromBank().equals(minAppendFromBank)) {
                System.out.printf("<Person name=\"%s\"/>\n", p.getName());
            }
        }

        System.out.printf("</minimum>\n");
        System.out.printf("</total>\n");
    }
}