package org.example;
import java.util.Random;
import java.util.Scanner;

public class Employee{
    String name;
    int code;

    public Employee(String name) {
        Random random = new Random();
        this.name = name;
        this.code = 1000 + random.nextInt(9000);
    }


    public String toString(){
        return "Account details are: " + this.name + " " + this.code;
    }
}