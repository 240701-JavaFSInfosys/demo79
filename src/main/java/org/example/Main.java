package org.example;

import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:h2:./h2/db";
        String username = "sa";
        String password = "sa";

        Connection connection = null;

// this entire block of code will just be our database setup.
        try{
            connection = DriverManager.getConnection(url, username, password);
//            this will read the file from which I would like to write SQL setup (tables, data)
            FileReader sqlReader = new FileReader("src/main/resources/data.sql");
            System.out.println("SQL Connection Established");
            RunScript.execute(connection, sqlReader);
            System.out.println("Ran SQL setup.");
        }catch (SQLException e) {
            System.out.println("Some issue occurred: "+e.getMessage());
//            This block of code runs when some issue occurs in the previous try block.
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find the file data.sql.");
        }

        while(true){
            System.out.println("SELECT: would you like to 1) insert person or 2) get all people");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if(input.charAt(0) == '1'){
                String id = sc.nextLine();
                String first = sc.nextLine();
                String last = sc.nextLine();
                try {
//                    We use the preparedStatement's ? format to prevent SQL injection, which you can read about on the notes.
                    PreparedStatement ps = connection.prepareStatement("insert into Person (id, first_name, last_name) values (?, ?, ?)");
                    ps.setInt(1, Integer.parseInt(id));
                    ps.setString(2, first);
                    ps.setString(3, last);
                    ps.executeUpdate();
                }catch (SQLException e){
                    System.out.println("There was an issue: "+e.getMessage());
                }
            }else if(input.charAt(0) == '2'){
                try{
                    PreparedStatement ps = connection.prepareStatement("select * from Person");
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        System.out.println("Person: " +
                                rs.getInt("id") +
                                rs.getString("first_name") +
                                rs.getString("last_name"));
                    }
                }catch(SQLException e){
                    System.out.println("Some issue occurred: "+e.getMessage());
                }
            }
        }

    }
}