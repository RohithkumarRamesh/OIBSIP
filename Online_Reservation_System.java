
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Online_Reservation_System {

    private static final String INSERT_QUERY = "INSERT INTO RESERVATIONS (NAME, CONTACTNUMBER, TRAIN_NUMBER, TRAIN_NAME, CLASS_TYPE, TRAVELLING_DATE, DEPARTURE, DESTINATION, PNR_NUMBER) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM RESERVATIONS";
    private static final String DELETE_QUERY = "DELETE FROM RESERVATIONS WHERE PNR_NUMBER = ?";

    private static ArrayList<StringBuilder> list=new ArrayList<StringBuilder>();

    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RAILWAYSYSTEM", "rohith", "Internship");

        try {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("Welcome To Online Reservations");
                System.out.println("Please Enter Your LOGIN ID :");
                int id = sc.nextInt();
                System.out.println("Please Enter Your Password");
                String password = sc.next();
                String sql = "SELECT COUNT(*), USERNAME FROM USERS  WHERE LOGIN_ID = ? AND PASSWORD = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, password);
                    try (ResultSet set = pstmt.executeQuery()) {
                        while (set.next()) {
                            int count = set.getInt(1);
                            if (count > 0) {
                                String name = set.getString(2);
                                System.out.println("Welcome " + name + " You've Successfully Logged in");
                                System.out.println("To Continue Your Registration process, Please click 0");
                                int option = sc.nextInt();
                                if (option == 0) {
                                    reservation(con, sc);
                                }
                            } else {
                                System.out.println("No User Exists, Get signed Up.");
                                System.out.println("Click 1 to sign up");
                                int option = sc.nextInt();
                                if (option == 1) {
                                    newUser(con, sc);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.close();
    }

    private static void newUser(Connection con, Scanner sc) throws SQLException {
        try {
            System.out.println("Enter Your details:");
            System.out.println("Enter Your name :");
            String name = sc.next();
            System.out.println("Enter your Password :");
            String password = sc.next();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(LOGIN_ID) FROM USERS");
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }


            int loginId = count + 1;

            System.out.println("Generated Login ID: " + loginId);
            System.out.println("Name: " + name);
            System.out.println("Password: " + password);


            String query = "INSERT INTO USERS (LOGIN_ID, USERNAME, PASSWORD) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, loginId);
            pstmt.setString(2, name);
            pstmt.setString(3, password);

            int st = pstmt.executeUpdate();
            if (st == 1) {
                System.out.println("You are successfully signed in. Your login id is: " + loginId);
            } else {
                System.out.println("Failed to sign up!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void reservation(Connection con, Scanner sc) {
        try {
            while (true) {
                System.out.println("Select the option to continue :");
                System.out.println("1) Make A Registration");
                System.out.println("2) View Your Registration");
                System.out.println("3) Cancel Your Registration");
                System.out.println("4) Exit");
                int option = sc.nextInt();
                switch (option) {
                    case 1:
                        makeRegistration(con, sc);
                        break;
                    case 2:
                        viewRegistration(con, sc);
                        break;
                    case 3:
                        deleteRegistration(con, sc);
                        break;
                    case 4: // Exit option
                        exitProgram(con);
                        break;
                    default:
                        System.out.println("Enter the valid option!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void exitProgram(Connection con)
    {
        try {
            con.close();
            System.out.println("Exiting the program. Goodbye!");
            System.exit(0);
        } catch (SQLException e) {
            System.err.println("Error while closing the connection: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void makeRegistration(Connection con, Scanner sc) throws SQLException {
        System.out.println();
        System.out.println("ENTER THE DETAILS");
        System.out.println("Enter name:");
        String name = sc.next();
        System.out.println("Enter contact number:");
        int contactNumber = sc.nextInt();
        System.out.println("Enter train number:");
        int trainNumber = sc.nextInt();
        System.out.println("Enter train name:");
        String trainName = sc.next();
        System.out.println("Enter class type:");
        String classType= sc.next();
        System.out.println("Enter traveling date (YYYY-MM-DD):");
        String travelingDate = sc.next();
        System.out.println("Enter departure:");
        String departure = sc.next();
        System.out.println("Enter destination:");
        String destination = sc.next();


        String pnrNumber = generatePNR();

        try (PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setString(1, name);
            stmt.setInt(2, contactNumber);
            stmt.setInt(3, trainNumber);
            stmt.setString(4, trainName);
            stmt.setString(5, classType);
            stmt.setString(6, travelingDate);
            stmt.setString(7, departure);
            stmt.setString(8, destination);
            stmt.setString(9, pnrNumber);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Reservation successfully added with PNR number: " + pnrNumber);
            } else {
                System.out.println("Failed to add reservation.");
            }
        }
    }

    private static String generatePNR() {
        Random random = new Random();
        StringBuilder pnr = new StringBuilder();
        for (int i = 0; i < 8; i++) { // Set desired length of PNR number
            pnr.append(random.nextInt(10)); // Generate random digit (0-9)
        }
        if(list.contains(pnr))
        {
           return generatePNR();
        }
        else
        {
            list.add(pnr);
            return pnr.toString();
        }
    }

    private static void viewRegistration(Connection con, Scanner sc) throws SQLException {
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_QUERY)) {

            while (rs.next()) {
                String name = rs.getString("NAME");
                int contactNumber = rs.getInt("CONTACTNUMBER");
                int trainNumber = rs.getInt("TRAIN_NUMBER");
                String trainName = rs.getString("TRAIN_NAME");
                String classType = rs.getString("CLASS_TYPE");
                String travelingDate = rs.getString("TRAVELLING_DATE");
                String departure = rs.getString("DEPARTURE");
                String destination = rs.getString("DESTINATION");
                String pnrNumber = rs.getString("PNR_NUMBER");

                System.out.println("Name: " + name);
                System.out.println("Contact Number: " + contactNumber);
                System.out.println("Train Number: " + trainNumber);
                System.out.println("Train Name: " + trainName);
                System.out.println("Class Type: " + classType);
                System.out.println("Traveling Date: " + travelingDate);
                System.out.println("Departure: " + departure);
                System.out.println("Destination: " + destination);
                System.out.println("PNR Number: " + pnrNumber);
                System.out.println();
            }
        }
    }

    private static void deleteRegistration(Connection con, Scanner sc) throws SQLException {


        System.out.println("Enter PNR number of reservation to delete:");
        String pnrNumber = sc.next();

        try (PreparedStatement stmt = con.prepareStatement(DELETE_QUERY)) {
            stmt.setString(1, pnrNumber);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reservation with PNR number " + pnrNumber + " deleted successfully.");
            } else {
                System.out.println("No reservation found with PNR number " + pnrNumber + ".");
            }
        }
    }
}