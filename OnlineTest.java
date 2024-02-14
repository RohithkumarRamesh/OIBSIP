import java.sql.*;
import java.util.Scanner;

public class OnlineTest
{

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/STUDENTS_DATA", "rohith", "Internship"))
        {

            System.out.println("WELCOME TO THE ONLINE TEST.");
            System.out.println("PLEASE READ THE INSTRUCTIONS BELOW BEFORE STARTING THE TEST.");
            System.out.println("------------------------INSTRUCTIONS------------------------");
            System.out.println("1. Don't Copy:");
            System.out.println("2. If something goes wrong during the exam, ask your teacher for help right away.");
            System.out.println("3. Be fair and honest while you're taking it.");
            System.out.println("CLICK 0 TO START THE TEST.");
            sc.nextLine();

            boolean loggedIn = false;

            while (!loggedIn)
            {
                System.out.println("PLEASE LOGIN TO START THE TEST");
                System.out.println("ENTER YOUR ROLL NUMBER :");
                String rollno = sc.nextLine();
                System.out.println("PLEASE ENTER THE PASSCODE :");
                int code;
                try
                {
                    code = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Invalid input! Please enter a valid number for the passcode.");
                    continue;
                }
                String sql = "SELECT NAME FROM STUDENTS WHERE ROLL_NUMBER = ? AND PASSCODE = ?";
                try (PreparedStatement smt = con.prepareStatement(sql))
                {
                    smt.setString(1, rollno);
                    smt.setInt(2, code);
                    try (ResultSet set = smt.executeQuery())
                    {
                        if (set.next())
                        {
                            loggedIn = true;
                            String name = set.getString(1);
                            System.out.println("WELCOME " + name + ", ALL THE BEST!");

                            System.out.println("Press ENTER to start the exam...");
                            sc.nextLine(); // Consume newline character left after previous nextInt
                            sc.nextLine(); // Wait for Enter key press

                            try
                            {
                                int score = examstart(sc);
                                System.out.println("YOUR PERCENTAGE IS: " + ((double) score / 25) * 100);

                                String insertSql = "INSERT INTO TESTRESULTS (ROLL_NUMBER, NAME, PASSCODE, PERCENTAGE) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                                    insertStmt.setString(1, rollno);
                                    insertStmt.setString(2, name);
                                    insertStmt.setInt(3, code);
                                    insertStmt.setDouble(4, ((double) score / 25) * 100);
                                    insertStmt.executeUpdate();
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("An error occurred during the exam: " + e.getMessage());
                            }
                        }
                        else
                        {
                            System.out.println("INVALID CREDENTIALS! Please try again.");
                        }
                    }
                }
                catch (SQLException e)
                {
                    System.out.println("SQL Error: " + e.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    private static int examstart(Scanner scanner)
    {
        int totalMarks = 0;

        // Question 1
        System.out.println("\nQuestion 1:");
        System.out.println("What is the output of the following code?");
        System.out.println("int x = 5;\nSystem.out.println(x++);");
        System.out.println("a) 5");
        System.out.println("b) 6");
        System.out.println("c) Compilation Error");
        System.out.println("d) Runtime Error");
        System.out.print("Your answer: ");
        if (scanner.next().trim().equalsIgnoreCase("a"))
        {
            totalMarks += 5;
        }

        // Question 2
        System.out.println("\nQuestion 2:");
        System.out.println("What is the correct way to declare a constant in Java?");
        System.out.println("a) final int MAX_VALUE = 100;");
        System.out.println("b) const int MAX_VALUE = 100;");
        System.out.println("c) static final int MAX_VALUE = 100;");
        System.out.println("d) final static int MAX_VALUE = 100;");
        System.out.print("Your answer: ");
        if (scanner.next().trim().equalsIgnoreCase("c"))
        {
            totalMarks += 5;
        }

        // Question 3
        System.out.println("\nQuestion 3:");
        System.out.println("What is the result of the expression 10 % 3?");
        System.out.println("a) 3");
        System.out.println("b) 1");
        System.out.println("c) 0");
        System.out.println("d) 10");
        System.out.print("Your answer: ");
        if (scanner.next().trim().equalsIgnoreCase("b"))
        {
            totalMarks += 5;
        }

        // Question 4
        System.out.println("\nQuestion 4:");
        System.out.println("What is the superclass of all classes in Java?");
        System.out.println("a) Object");
        System.out.println("b) Class");
        System.out.println("c) Super");
        System.out.println("d) Main");
        System.out.print("Your answer: ");
        if (scanner.next().trim().equalsIgnoreCase("a"))
        {
            totalMarks += 5;
        }

        // Question 5
        System.out.println("\nQuestion 5:");
        System.out.println("Which keyword is used to define a subclass in Java?");
        System.out.println("a) subclass");
        System.out.println("b) extends");
        System.out.println("c) inherits");
        System.out.println("d) implements");
        System.out.print("Your answer: ");
        if (scanner.next().trim().equalsIgnoreCase("b"))
        {
            totalMarks += 5;
        }

        return totalMarks;
    }
}
