package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class Java_Database {
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static String userInput = null;
	private static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) {
		try {

			/*
			 * @param url: the url that connects to my local database MAKE SURE
			 * THAT MYSQLD HAS BEEN STARTED ON CMD otherwise you get a
			 * connection error
			 * 
			 * @param username: name of the user (I use root)
			 * 
			 * @param password: password for specific user, root has no password
			 */
			String url = "jdbc:mysql://localhost:3306/login";
			String username = "root";
			String password = "1234";

			System.out.println("Connecting database...");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
			statement = (Statement) connection.createStatement();
			
			// Drop Table If Exists
			String sql= "DROP TABLE IF EXISTS Member;";
			statement.executeUpdate(sql);
			
			// Create Table Member
			sql = "CREATE TABLE Member "
					+ "(id INT NOT NULL PRIMARY KEY UNIQUE AUTO_INCREMENT," 
					+ " email varchar(30) UNIQUE NOT NULL,"
					+ " password varchar(30) NOT NULL," 
					+ " description varchar(30) NULL);";
			statement.executeUpdate(sql);
			
			// Insert dummy values into Member table
			sql = "INSERT INTO Member VALUES "
					+ "(31, 'batman@gmail.com', '1234', 'Hello World!'),"
					+ "(32, 'kane@yahoo.com', '1234', 'Goodbye World!'),"
					+ "(33, 'bobbyhill@gmail.com', '1234', 'Good night!');";
			statement.executeUpdate(sql);
			
			// Get the whole table of Member
			sql = "SELECT * FROM Member;";
			resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String email = resultSet.getString("email");
				String pass = resultSet.getString("password");
				String desc = resultSet.getString("description");
			}
			
			// Get Member by email
			sql = "SELECT * FROM Member WHERE email = 'batman@gmail.com'";
			resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String email = resultSet.getString("email");
				String pass = resultSet.getString("password");
				String desc = resultSet.getString("description");
			}
			
			// Main loop
			boolean done = false;
			do {
				displayHeader();
				System.out.println("Enter Query (Enter \"exit\" to exit)");
				userInput = checkValid();
				String command = userInput;

				if (userInput.equalsIgnoreCase("exit")) {
					done = true;
				} else {
					try {
						resultSet = statement.executeQuery(command);
						printResults(resultSet);
					} catch (SQLException e) {
						System.out.println("Error: No Search Results");
						System.out.println("Press any Key to continue:");
						// should probably use an event listener for key
						// presses
						// I'll update this later
						String temp = keyboard.nextLine();
					}
				}
			} while (!done);

			System.out.println("Good Bye");

		} catch (Exception e) {
			e.printStackTrace(); // if sql throws any exception print the stack
									// trace
		} finally {
			close(); // close all possible resource leaks
		}
	}

	private static void printResults(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data sets
		ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
		int columns = metaData.getColumnCount();
		String row = "";
		for (int i = 1; i <= columns; i++) {
			row += metaData.getColumnLabel(i) + "\t";
		}
		System.out.print(row + "\n");
		row = "";
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			for (int i = 1; i <= columns; i++) {
				row += resultSet.getString(i) + "\t";
			}
			System.out.print(row + "\n");
			row = "";
		}
		System.out.println("Hit any key to continue:");
		String temp = keyboard.nextLine();// again i'll fix this later!
	}

	/*
	 * All Display functions are remove from main so that main is more readable
	 * without all the interface options getting in the way
	 */
	private static void displayHeader() {
		System.out.println();
		System.out.println("***Welcome to Database management***");
	}

	private static String checkValid() {
		String userInput = ""; // local scope of userInput although I can't
								// remember why
		boolean valid = false; // used to make sure user input is
								// acceptable

		userInput = keyboard.nextLine();

		while (!valid) {
			if (userInput.equals("")) { // never accept null char
				System.out.println("ERROR: Invalid Input!");
				userInput = keyboard.nextLine();
			} else
				valid = true;
		}
		return userInput;
	}

	private static void close() { // close all potential resource leaks
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}

			if (keyboard != null) {
				keyboard.close();
			}
		} catch (Exception e) {

		}
	}
}