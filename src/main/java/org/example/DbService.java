package org.example;

import java.sql.*;
import java.util.PropertyPermission;
import java.util.Random;
import java.util.Scanner;

public class DbService {
    String url = "jdbc:mysql://localhost:3306/bank";
    String user = "root";
    String pass = "442211";
        public DbService () throws SQLException {}
    public Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, pass);
        return connection;
    }

    public void applyBank() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter  your first name please: ");
        String fName = scanner.nextLine();
        System.out.print("Enter your last name please: ");
        String lName = scanner.nextLine();
        System.out.print("Enter the type of account you have: ");
        String accountType = scanner.nextLine();
        System.out.print("What is the current balance of the account? ");
        double balance = scanner.nextDouble();
        Random random = new Random();
        int code = 100 + random.nextInt(900);

        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        try {
            String addAppSql = "insert into applications(fName, lName, type, balance, code) values(?,?,?,?,?)";
            PreparedStatement addUser = connection.prepareStatement(addAppSql, Statement.RETURN_GENERATED_KEYS);
            addUser.setString(1, fName);
            addUser.setString(2, lName);
            addUser.setString(3, accountType);
            addUser.setDouble(4, balance);
            addUser.setInt(5, code);
            addUser.executeUpdate();
            ResultSet addUserResults = addUser.getGeneratedKeys();
            if (addUserResults.next()) {
                System.out.println("This is your application code "+ code + ". Present to employee for approval/rejection");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int approveApp() throws SQLException {
        int userId = 1;
        int accountId = 1;

        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        System.out.println("Enter the application code");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();
        try {
            connection.setAutoCommit(false);


            String selectNameSql = "select fName, lNAme,type, balance, code from applications where code =?";
            PreparedStatement selectName = connection.prepareStatement(selectNameSql);
            selectName.setInt(1, code);
            ResultSet selectNameResult = selectName.executeQuery();
            if (selectNameResult.next()) {
                String fName = selectNameResult.getString("fName");
                String lName = selectNameResult.getString("lName");
                String type = selectNameResult.getString("type");
                Double balance = selectNameResult.getDouble("balance");
                int num = selectNameResult.getInt("code");
                String addUserSql = "insert into users(fName,lName, code) values(?,?,?)";
                PreparedStatement addUser = connection.prepareStatement(addUserSql, Statement.RETURN_GENERATED_KEYS);
                addUser.setString(1, fName);
                addUser.setString(2, lName);
                addUser.setInt(3, code);
                addUser.executeUpdate();
                ResultSet addUserResult = addUser.getGeneratedKeys();
                if (addUserResult.next()) {
                    userId = addUserResult.getInt(1);
                }
                String addBalanceSql = "insert into accounts(type, balance) values(?,?)";
                PreparedStatement addBalance = connection.prepareStatement(addBalanceSql, Statement.RETURN_GENERATED_KEYS);
                addBalance.setString(1, type);
                addBalance.setDouble(2, balance);
                addBalance.executeUpdate();
                ResultSet addBalanceResult = addBalance.getGeneratedKeys();
                if (addBalanceResult.next()) {
                    accountId = addBalanceResult.getInt(1);
                }
            }
            if (userId > 0 && accountId > 0) {
                String linkAccountSql = "insert into links(userid, accountid) values (?,?)";
                PreparedStatement linkAccount = connection.prepareStatement((linkAccountSql));
                linkAccount.setInt(1, userId);
                linkAccount.setInt(2, accountId);
                linkAccount.executeUpdate();
                connection.commit();
            } else {
                connection.rollback();
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }
        System.out.println(accountId + " has been created. This number represents your id and will be necessary in order to make transactions");
        return accountId;
    }

    public void rejectApp() throws SQLException {
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        System.out.println("Enter the application code");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();
        try {
            String deleteAppSql = "delete fName, lNAme,type, balance from applications where code =?";
            PreparedStatement selectName = connection.prepareStatement(deleteAppSql);
            selectName.setInt(1, code);
            selectName.executeUpdate();
            connection.commit();
    } catch (SQLException e) {
        System.err.println("An error has occurred: " + e.getMessage());}
    }

    public Employee addEmployee() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name");
        String name = scanner.nextLine();
        Employee employee = new Employee(name);
        int pass = employee.code;
        int employeeId;

        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        try {
            String addaddEmployeeSql = "insert into employee(name, pass) values(?,?)";
            PreparedStatement addEmployee = connection.prepareStatement(addaddEmployeeSql, Statement.RETURN_GENERATED_KEYS);
            addEmployee.setString(1, name);
            addEmployee.setInt(2, pass);
            addEmployee.executeUpdate();
           // ResultSet addEmployeeResults = addEmployee.getGeneratedKeys();
            String selectCodeSql = "select pass from employee where name = ?";
            PreparedStatement selectCode = connection.prepareStatement(selectCodeSql);
            selectCode.setString(1, name);
            ResultSet employeeCode = selectCode.executeQuery();
            if (employeeCode.next()) {
                System.out.println("Employee added successfully, they will have access to accounts and records. Please record your code below, it will act as your password to login");
                System.out.println(employeeCode.getInt("pass"));
                System.out.println(employee.code);
               // System.out.println(addEmployeeResults.getInt("code"));
            }
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }

        return new Employee(name);
    }

    public boolean hasAccount() throws SQLException {
        boolean hasAccount = false;
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        System.out.println("Enter your application code to see if you have been approved");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();

        try {
            String selectCodeSql = "select name from users where code = ?";
            PreparedStatement selectEmployee = connection.prepareStatement(selectCodeSql);
            selectEmployee.setInt(1, code);
            ResultSet rs = selectEmployee.executeQuery();
            if (rs.next()) {
                hasAccount = true;
            } else {
                System.out.println("Employee has not yet approved your account");
            }
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }
        return hasAccount;
    }

    public boolean isEmployee(int code) throws SQLException {
        boolean employed = false;
        DbService dbs = new DbService();
        Connection connection = dbs.connect();

        try {
            String selectCodeSql = "select name from employee where pass = ?";
            PreparedStatement selectEmployee = connection.prepareStatement(selectCodeSql);
            selectEmployee.setInt(1, code);
            ResultSet rs = selectEmployee.executeQuery();
            if (rs.next()) {
                employed = true;
            } else {
                System.out.println("Employee has not yet approved your account");
            }
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }
        return employed;
    }


    public Customer getAccount() throws SQLException {
        Customer customer = null;
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account id please: ");
        int accountId = scanner.nextInt();
        try {
            String findUserSql = "select fName, lName, type, balance " +
                    "from users a join links b on a.id = b.userid " +
                    "join accounts c on c.id = b.accountid where c.id = ?";
            PreparedStatement findUser = connection.prepareStatement((findUserSql));
            findUser.setInt(1, accountId);
            ResultSet findUserResults = findUser.executeQuery();
            if (findUserResults.next()) {
                String fName = findUserResults.getString("fName");
                String lName = findUserResults.getString("lName");
                String type = findUserResults.getString("type");
                double balance = findUserResults.getDouble("balance");
              //  if (type.equals("Checking") || type.equals("Savings")) {
                    customer = new Customer(fName, lName, type, balance);
                    System.out.println(customer.toString());
              //  }
            } //else {
             //   System.err.println("No such account");
          //  }
            ;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return customer;

    }


    public String getEmployee() throws SQLException {
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        String employee = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account id please: ");
        int accountId = scanner.nextInt();
        try {
            String findUserSql = "select name, pass from employee where id = ?";
            PreparedStatement findEmployee = connection.prepareStatement((findUserSql));
            findEmployee.setInt(1, accountId);
            ResultSet findEmployeeResults = findEmployee.executeQuery();
            if (findEmployeeResults.next()) {
                String name = findEmployeeResults.getString("name");
                int code = findEmployeeResults.getInt("pass");
                employee = "This is employee name: " + name + " and pass: " + code;
                System.out.println(employee);
                //  }
            } else {
               System.err.println("No such account");
             }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return employee;

    }


    public boolean Deposit() throws SQLException {
        boolean success = false;
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your account ID: ");
        int accountid = scanner.nextInt();
        System.out.print("Please enter the deposit amount: ");
        double amt = scanner.nextDouble();
        if (amt == 0) {
            System.out.println("You cannot deposit nothing, please input an amount");
            System.out.print("Please enter the deposit amount: ");
            amt = scanner.nextDouble();
        }
        try {
            String updateBalanceSql = "update accounts set balance = balance + ? where id = ?";
            String selectBalanceSql = "select balance from accounts where id = ?";
            PreparedStatement updateBalance = connection.prepareStatement(updateBalanceSql);
            PreparedStatement selectBalance = connection.prepareStatement(selectBalanceSql);
            selectBalance.setInt(1, accountid);
            ResultSet rs = selectBalance.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance + amt <= 0) {
                    System.out.println("This would be an invalid transaction. You have "
                            + balance + " but you are trying to deposit" + amt);
                } else {
                    updateBalance.setDouble(1, amt);
                    updateBalance.setInt(2, accountid);
                    updateBalance.executeUpdate();
                    success = true;
                }}
            else{
                    System.out.println("No such account found");
                }
            } catch(SQLException e){
                System.err.println("An error has occurred: " + e.getMessage());
            }
            return success;
        }


    public boolean Withdrawal() throws SQLException {
        boolean success = false;
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your account ID: ");
        int accountid = scanner.nextInt();
        System.out.print("Please enter the withdrawal amount: ");
        double amt = scanner.nextDouble();
        if (amt == 0) {
            System.out.println("You cannot withdraw nothing, please input an amount");
            System.out.print("Please enter the deposit amount: ");
            amt = scanner.nextDouble();
        }
        try {
            String updateBalanceSql = "update accounts set balance = balance - ? where id = ?";
            String selectBalanceSql = "select balance from accounts where id = ?";
            PreparedStatement updateBalance = connection.prepareStatement(updateBalanceSql);
            PreparedStatement selectBalance = connection.prepareStatement(selectBalanceSql);
            selectBalance.setInt(1, accountid);
            ResultSet rs = selectBalance.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if ( balance - amt <=0 ){
                    System.out.println("This would be an invalid transaction. You have "
                            + balance + " but you are trying to withdrawal" + amt);
                } else {
                System.out.println(balance);
                updateBalance.setDouble(1, amt);
                updateBalance.setInt(2, accountid);
                updateBalance.executeUpdate();
                success = true;
                }
            }
            else { System.out.println("No such account found"); }
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }
        return success;
    }

    public boolean deleteAccount() throws SQLException {
        boolean success = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account id please: ");
        int accountid = scanner.nextInt();
        DbService dbs = new DbService();
        Connection connection = dbs.connect();
        try {
            String deleteSql = "delete users,accounts " +
                    "from users a join links b on a.id = b.userid " +
                    "join accounts c on c.id = b.accounts where c.id = ?";
            PreparedStatement deleteAccount = connection.prepareStatement((deleteSql));
            deleteAccount.setInt(1, accountid);
            deleteAccount.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        }
        return success;
    }


}
