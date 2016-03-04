package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Utilities class for technical settings and configurations to be accessible
 * by other classes (Static methods)
 */
public class Utilities {

	// Date/time formats for LocalDateTime
	final static String DATE_FORMAT = "MMMM d, yyyy";
	final static String TIME_FORMAT = "hh:mm a";

	// Data filenames
	final static String ADMIN_LOGIN = "admin.dat";
	final static String BOOK_FILE = "books.txt";
	final static String CUSTOMER_FILE = "customer.dat";

	// Tax rate
	final static double TAX = 0.13;

	// Application icon
	final static Image ICON = new Image("file:icon.png");

	/**
	 * Count number of lines in a file to validate the file
	 * @param filename Name of the file
	 * @return number of lines in the file
	 */
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
	 * Check if the given string is a number
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
