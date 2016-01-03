package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utilities {
	final static String ADMIN_LOGIN = "admin.dat";
	final static String BOOK_FILE = "books.txt";
	final static String CUSTOMER_FILE = "customer.dat";
	final static double TAX = 0.13;

	public static int countLines(String filename) {
		// Declaration
		FileReader file;
		BufferedReader reader;
		int counter = 0;
		String line;

		try {
			file = new FileReader(filename);
			reader = new BufferedReader(file);

			while ((line = reader.readLine()) != null) {
				// Increment line count
				counter++;
			}
		}
		catch (IOException e) {
			System.out.println("File read error");
		}
		return counter;
	}

	/**
	 * Checks if the given string is a number
	 *
	 * @param value A string containing a value
	 * @return true, if it is able to be parsed into a double, otherwise false
	 */
	public static boolean isNumber(String value) {
		try {
			double test = Double.parseDouble(value);
		}
		// If there is an exception caught, then the value is not numeric
		catch (NumberFormatException e) {
			return false;
		}
		// It is numeric if no exception is thrown
		return true;
	}
}
