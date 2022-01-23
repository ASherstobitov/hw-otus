package ru.otus;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    TreeMap<Customer, String> treeMap =
            new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {

        var tempEntry = treeMap.firstEntry();
        Map.Entry<Customer, String> pair = null;

        if (tempEntry != null) {
            var tempCustomer = new Customer(tempEntry.getKey());
            pair = Map.entry(tempCustomer, tempEntry.getValue());
        }
        return pair;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {

        var tempEntry = treeMap.higherEntry(customer);
        Map.Entry<Customer, String> pair = null;
        if (tempEntry != null) {
            var tempCustomer = new Customer(tempEntry.getKey());
            pair = Map.entry(tempCustomer, tempEntry.getValue());
        }
        return pair;
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
