package banking;

import java.util.Random;
import java.util.Scanner;

public class Actions {
    private final Scanner scanner = new Scanner(System.in);
    private final Moon moon = new Moon();
    private final CoolJDBC jdbc;

    public Actions(CoolJDBC jdbc) {
        this.jdbc = jdbc;
    }

    public void startPage() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        System.out.print(">");
        int number = scanner.nextInt();
        System.out.println();
        switch (number) {
            case 1: {
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                create();
                break;
            }
            case 2: {
                log();
                break;
            }
            case 0: {
                System.out.println("Bye");
                break;
            }
        }
    }

    private void log() {
        Scanner scannerList = new Scanner(System.in);
        System.out.println("Enter your card number");
        System.out.print(">");
        String inputNumberCard = scannerList.nextLine();
        System.out.println("Enter your PIN:");
        System.out.print(">");
        String inputPin = scannerList.nextLine();
        System.out.println();
        if (moon.checkNum(inputNumberCard)) {
            try {
                if (jdbc.selectCheckCardAndPin(new Card(inputNumberCard, inputPin))) {
                    System.out.println("You have successfully logged in!");
                    System.out.println();
                    new WorkingAccount(this.jdbc).menuAccount(new Card(inputNumberCard, inputPin));
                } else {
                    System.out.println("Wrong card number or PIN!");
                    System.out.println();
                    startPage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong card number or PIN!");
            System.out.println();
            startPage();
        }
    }

    private void create() {
        Random random = new Random();
        long numberCard = 400000000000000L + random.nextInt(1000000000);
        String numCard1 = String.valueOf(numberCard);
        int numRes = moon.generator(numCard1);
        String resNum = numCard1 + numRes;

        int num = random.nextInt(10000);
        int razryd = 1;
        int i = num;
        while (true) {
            if (i / 10 > 0) {
                razryd++;
                i = i / 10;
            } else {
                break;
            }
        }
        String numPin = "";
        switch (razryd) {
            case 1: {
                numPin = "000" + num;
                break;
            }
            case 2: {
                numPin = "00" + num;
                break;
            }
            case 3: {
                numPin = "0" + num;
                break;
            }
            case 4: {
                numPin = "" + num;
                break;
            }
        }
        try {
            if (!jdbc.selectCheckCard(resNum)) {
                Card card = new Card(resNum, numPin);
                jdbc.saveCard(card);
                System.out.println(card.getNumberCard());
                System.out.println("Your card PIN:");
                System.out.println(card.getPin());
                System.out.println();
                startPage();
            } else {
                create();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
