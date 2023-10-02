package com.mycompany.app;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;

/**
 * Hello world!
 */
public class App {

    private static final String MESSAGE = "Hello World!";

    public App() {}

    public static void main(String[] args) {
        System.out.println(MESSAGE);

        // Vulnerable code starts here
        if (args.length > 0) {
            try {
                // Execute the command provided as an argument
                Process process = Runtime.getRuntime().exec(args[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Vulnerable code ends here
    }

    public String getMessage() {
        return MESSAGE;
    }

     // New method to perform the database query
    private static void performDatabaseQuery() {
        try {
            // Assuming you have initialized the 'connection' object properly
            Connection connection = DriverManager.getConnection("your_database_url", "your_username", "your_password");

            // Assuming you have a 'request' object available
            HttpServletRequest request = new HttpServletRequest(); // You need to initialize this properly

            // Get username from parameters
            String username = request.getParameter("username");

            // Create a statement from database connection
            Statement statement = connection.createStatement();

            // Create unsafe query by concatenating user-defined data with query string
            String query = "SELECT secret FROM Users WHERE (username = '" + username + "' AND NOT role = 'admin')";

            // ... OR ...

            // Insecurely format the query string using user-defined data
            String queryFormatted = String.format("SELECT secret FROM Users WHERE (username = '%s' AND NOT role = 'admin')", username);

            // Execute query and return the results
            ResultSet result = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
