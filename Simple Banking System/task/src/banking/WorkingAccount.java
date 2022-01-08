package banking;

import java.util.Scanner;

public class WorkingAccount {
    private final Scanner scannerInt = new Scanner(System.in);
    private final Scanner scannerString = new Scanner(System.in);
    private final CoolJDBC jdbc;
    private final Moon moon = new Moon();

    public WorkingAccount(CoolJDBC jdbc) {
        this.jdbc = jdbc;
    }

    public void menuAccount(Card card) {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        System.out.print(">");
        int num = scannerInt.nextInt();
        System.out.println();
        switch (num) {
            case 1: {
                try {
                    System.out.println("Balance: " + jdbc.selectBalance(card));
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menuAccount(card);
                break;
            }
            case 2: {
                System.out.println("Enter income:");
                System.out.print(">");
                int sum = scannerInt.nextInt();
                try {
                    jdbc.enrollmentBalance(sum, card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Income was added!");
                System.out.println();
                menuAccount(card);
                break;
            }
            case 3: {
                boolean availabilityDatabase = false;
                int balance;
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                System.out.print(">");
                String strInput = scannerString.nextLine();

                if (strInput.equals(card.getNumberCard())) {
                    System.out.println("You can't transfer money to the same account!");
                    System.out.println();
                    menuAccount(card);
                    break;
                }

                if (!moon.checkNum(strInput)) {
                    System.out.println("Probably you made a mistake in" +
                            " the card number. Please try again!");
                    System.out.println();
                    menuAccount(card);
                    break;
                }
                try {
                    availabilityDatabase = jdbc.selectCheckCard(strInput);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!availabilityDatabase) {
                    System.out.println("Such a card does not exist.");
                    System.out.println();
                    menuAccount(card);
                    break;
                }
                System.out.println("Enter how much money you want to transfer:");
                System.out.print(">");
                int transSum = scannerInt.nextInt();
                try {
                    balance = jdbc.selectBalance(card);
                    if (balance - transSum < 0) {
                        System.out.println("Not enough money!");
                        System.out.println();
                        menuAccount(card);
                        break;
                    } else {
                        jdbc.transferMoney(strInput, card, transSum);
                        System.out.println("Success!");
                        System.out.println();
                        menuAccount(card);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4: {
                try {
                    jdbc.deleteAccount(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("The account has been closed!");
                System.out.println();
                new Actions(jdbc).startPage();
                break;
            }
            case 5: {
                System.out.println("You have successfully logged out!");
                System.out.println();
                new Actions(jdbc).startPage();
                break;
            }
            case 0: {
                System.out.println("Bye");
                break;
            }
        }
    }
}
