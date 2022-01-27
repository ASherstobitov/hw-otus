package ru.otus;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    Deque<Customer> customerDeque = new ArrayDeque<>();

    public void add(Customer customer) {
        customerDeque.addFirst(customer);
    }

    public Customer take() {
        return customerDeque.poll();
    }
}
