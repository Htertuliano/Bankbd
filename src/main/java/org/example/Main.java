package org.example;
import java.sql.*;
import java.util.Scanner;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;


public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DbService dbs = new DbService();
        System.out.print("Are you an (1)-Employee or (2)-Customer or you wish to be a (3) customer or (4) employee. Enter a number: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter employee ID: ");
                int code = scanner.nextInt();
                dbs = new DbService();
                if( dbs.isEmployee(code)) {
                Scanner scans = new Scanner(System.in);
                    while (true) {
                        System.out.println("1: Get Account by ID");
                        System.out.println("2: Approve Application");
                        System.out.println("3: Reject Application out");
                        System.out.println("4: Logout");
                        System.out.print("Enter your choice: ");
                        int num = scans.nextInt();
                        scans.nextLine();
                        switch (num) {
                            case 1:
                                dbs = new DbService();
                                dbs.getAccount();
                                break;
                            case 2:
                                dbs = new DbService();
                                dbs.approveApp();
                                break;
                            case 3:
                                dbs = new DbService();
                                dbs.rejectApp();
                            case 4:
                                return;
                            default:
                                System.out.println("Invalid choice, try again.");
                        }
                    }
                } else {
                    System.out.println("You are not an employee at the Bank.");
                }
                break;
            case 2:
                Scanner scanner1 = new Scanner(System.in);
                while (true) {
                    System.out.println("1: Show account details");
                    System.out.println("2: Deposit Amount");
                    System.out.println("3: Withdraw Amount");
                    System.out.println("4: Send amount to id");
                    System.out.println("5: Receive amount from id");
                    System.out.println("6: log out");
                    System.out.print("Enter your choice: ");
                    int num = scanner1.nextInt();
                    scanner1.nextLine();

                    switch (num) {
                        case 1:
                            dbs = new DbService();
                        //    dbs.createAccount();
                            break;
                        case 2:
                            dbs = new DbService();
                            dbs.Deposit();
                            break;
                        case 3:
                            dbs = new DbService();
                            dbs.Withdrawal();
                            break;
                        case 4:
                        //    dbs = new DbService();
                        //    dbs.getAccount();
                            break;
                        case 5:
                        //    dbs = new DbService();
                        //    dbs.getEmployee();
                            break;
                        case 6:
                            return;
                        default:
                            System.out.println("Invalid choice, try again.");
                    }
                }
                case 3:
                    dbs = new DbService();
                    dbs.applyBank();
                    break;
            case 4:
                dbs = new DbService();
                dbs.addEmployee();
                break;

        }
        }
     //   String isEmployee = scanner.nextLine();
     /*   if (isEmployee.equalsIgnoreCase("yes" )) {
            System.out.print("Please enter your employee code: ");
            int employeeCode = scanner.nextInt();
            DbService dbs = new DbService();
            dbs.isEmployee(employeeCode);
            System.out.println("Employee code entered: " + employeeCode);
        } else {
            System.out.println("You are not an employee at the Bank.");
      */





    //    Scanner scanner1 = new Scanner(System.in);
  /*      while (true) {
            System.out.println("1: Add account details");
            System.out.println("2: Deposit Amount");
            System.out.println("3: Withdraw Amount");
            System.out.println("4: Get Account by id");
            System.out.println("5: Get Employee by id");
            System.out.println("6: Apply to be employee");
            System.out.print("Enter your choice: ");
            int choice = scanner1.nextInt();
            scanner1.nextLine();

            switch (choice) {
                case 1:
                    DbService dbs = new DbService();
                    dbs.createAccount();
                    break;
                case 2:
                    dbs = new DbService();
                    dbs.Deposit();
                    break;
                case 3:
                    dbs = new DbService();
                    dbs.Withdrawal();
                    break;
                case 4:
                     dbs = new DbService();
                     dbs.getAccount();
                    break;
                case 5:
                    dbs = new DbService();
                    dbs.getEmployee();
                    break;
                case 6:
                    dbs = new DbService();
                    dbs.addEmployee();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
           }
      */  }


