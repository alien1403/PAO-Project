package com.company.comparator;

import com.company.user.Customer;

import java.util.Comparator;

public class CustomerNameComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer c1, Customer c2){
        return c1.getFirstName().compareTo(c2.getFirstName());
    }
}
