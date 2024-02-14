import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        int number = rand.nextInt(101);
        int attempts = 5;

        System.out.println("WELCOME TO THE GAME ");
        System.out.println("GOOD LUCK!");
        System.out.println("A NUMBER IS SELECTED IN THE RANGE 0-100.");
        System.out.println("ATTEMPTS REMAINING: 5");

        for (int i = 0; i < 5; i++) {
            int guess = sc.nextInt();
            sc.nextLine();
            if (guess == number) {
                System.out.println("CONGRATULATIONS! YOU'VE WON!");
                System.out.println("YOUR SCORE IS: "+attempts*20);
                break;
            }
            if (guess > number) {
                System.out.println();
                System.out.println("OOPS! YOUR GUESS IS TOO HIGH!");
            } else {
                System.out.println();
                System.out.println("OOPS! YOUR GUESS IS TOO LOW!");
            }
            attempts--;
            System.out.println("ATTEMPTS REMAINING: " +attempts);
        }

        System.out.println("\nGAME OVER!");
        System.out.println("YOUR SCORE IS: "+attempts*20);
        System.out.println("THE NUMBER WAS: " + number);
        sc.close();
    }
}
