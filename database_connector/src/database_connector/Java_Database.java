package database_connector;

import java.sql.*;
import java.util.Scanner;

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
			String url = "jdbc:mysql://localhost:3306/company";
			String username = "root";
			String password = "";

			System.out.println("Connecting database...");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
			statement = connection.createStatement();

			// Main loop
			boolean done = false;
			do {
				displayHeader();
				System.out.println("Enter Query (Enter \"exit\" to exit)");
				userInput = checkValid();
				String command = userInput;

				if (userInput.equalsIgnoreCase("exit")) {
					done = true;
				}
				else{
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
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columns = metaData.getColumnCount();
		String row = "";
		for(int i = 1; i <= columns; i++){
			row += metaData.getColumnLabel(i) + "\t";
		}
		System.out.print(row + "\n");
		row = "";
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			for (int i=1; i <= columns; i++){
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
