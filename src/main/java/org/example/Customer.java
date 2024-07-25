package org.example;

public class Customer{
String fName;
String lName;
String type;
double balance;

public Customer(String fName, String lName,String type, double balance) {
    this.fName = fName;
    this.lName = lName;
    this.balance = balance;
    this.type = type;
}

public String toString(){
    return "Account details are: " + this.fName + " " + this.lName + " " + this.type + " " + this.balance;
}
}