package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CustomerEntrance implements Initializable {

	final private String CUSTOMER_FILE = "customer.dat";

	@FXML
	private javafx.scene.control.Button back;
	@FXML
	private javafx.scene.control.Button login;
	@FXML
	private javafx.scene.control.Button register;
	@FXML
	private javafx.scene.control.TextField email;
	@FXML
	private javafx.scene.control.PasswordField pw;

	ArrayList<Account> list = new ArrayList<Account>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		importAccounts();
	}

	/**
	 * Close window when back button is pressed
	 */
	@FXML
	private void backButtonAction() {
		Stage stage = (Stage) back.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void loginButtonAction() {
		String email_address = email.getText();
		String password = pw.getText();
		boolean success = false;
		Account customer;

		// Improper email format
		if (!isEmail(email_address)) {
			// Display alert
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Improper email format!");
			alert.showAndWait();
		}
		else {
			for (Account a : list) {
				if (a.getEmail().toLowerCase()
						.equals(email_address.toLowerCase())
						&& a.getPassword().equals(password)) {
					customer = a;
					success = true;
					break;
				}
			}
			if (!success) {
				// Display alert
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Invalid login info.");
				alert.showAndWait();
			}
		}
	}

	@FXML
	private void registerButtonAction() {
		String email_address = email.getText();
		String password = pw.getText();

		// Improper email format
		if (!isEmail(email_address)) {
			// Display alert
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Improper email format!");
			alert.showAndWait();
		}
		else if (password.length() == 0) {
			// Display alert
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Please enter a password!");
			alert.showAndWait();
		}
		else {
			for (Account a : list) {
				if (a.getEmail().toLowerCase()
						.equals(email_address.toLowerCase())) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("You already have an account!");
					alert.showAndWait();
					break;
				}
				// Create account
				else {
					list.add(new Account(email_address, password));

				}
			}
		}
	}

	/**
	 * Use regex to determine if the string is an email address
	 * Criteria:
	 * - Has a @ symbol
	 * - Has a period after the @ symbol
	 * - Last character is not a period
	 * - Part after the period is a letter from a-z
	 * @param s String to be tested
	 * @return true if the string is in an email format, otherwise false
	 */
	private boolean isEmail(String s) {
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(s.toLowerCase());
		boolean is_email = m.matches();
		return is_email;
	}

	public int countLines(String filename) {
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

	private void importAccounts() {
		// Constant Declaration
		final int LINES_PER_ACCOUNT = 2;
		final String ERROR = "Error reading file.";

		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = countLines(CUSTOMER_FILE);
		int accounts = lines / LINES_PER_ACCOUNT;
		String email;
		String password;
		// File readers
		FileReader file_reader;
		BufferedReader reader;

		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(CUSTOMER_FILE);
			reader = new BufferedReader(file_reader);

			// Loop through all customers
			for (int i = 0; i < accounts; i++) {
				valid = true;
				// Read and process input
				email = reader.readLine();
				if (!isEmail(email))
					valid = false;
				password = Account.rot13(reader.readLine());

				if (valid)
					list.add(new Account(email, password));

			}
			// Close readers
			reader.close();
			file_reader.close();
		}
		// Handle exception
		catch (IOException e) {
			System.out.println(ERROR);
		}
	}

	private void updateAccounts() {

	}
}
