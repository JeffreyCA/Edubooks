package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Controller for customer entrance login
 */
public class CustomerEntrance implements Initializable {

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

	/**
	 * Proceed to storefront when use logs in
	 */
	@FXML
	private void loginButtonAction() {
		Account customer = null;
		boolean success = false;

		String email_address = email.getText();
		String password = pw.getText();
		String error = "Invalid login information";

		// Error messages
		// Empty email
		if (email_address.length() == 0)
			error = "Please enter an email!";
		// Empty password
		else if (password.length() == 0)
			error = "Please enter a password!";
		// Improper email format
		else if (!isEmail(email_address))
			error = "Improper email format!";
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
		}
		// Display alert popup if user enters invalid information
		if (!success) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(error);
			alert.showAndWait();
		}
		// Otherwise proceed to storefront
		else {
			launchStore(customer);
		}
	}

	/**
	 * Use data files to create a list of customer Accounts
	 */
	private void importAccounts() {
		// One line for email, another line for password
		final int LINES_PER_ACCOUNT = 2;

		// Variables
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = Utilities.countLines(Utilities.CUSTOMER_FILE);
		int accounts = lines / LINES_PER_ACCOUNT;
		String email;
		String password;

		// File readers
		FileReader file_reader;
		BufferedReader reader;

		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(Utilities.CUSTOMER_FILE);
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
			System.out.println("Error reading file.");
		}
	}

	/**
	 * Display storefront after successful login
	 * @param a Customer's account
	 */
	private void launchStore(Account a) {

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("StoreFront.fxml"));
			Parent root = (Parent) loader.load();
			StoreFront controller = loader.getController();
			Stage stage = (Stage) login.getScene().getWindow();
			Scene scene = new Scene(root);
			Instance i = controller.getInstance();

			i.account = a;
			i.loadData();

			// Grab customer cart, wishlist, and order data
			controller.initializeObservableLists();
			controller.initializeCart();
			controller.initializeWishlist();
			controller.initializeOrders(a.getOrderStack());
			controller.setOrders(a.getOrderStack());

			stage.setTitle("Book Store");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(scene);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	/**
	 * Create new customer account
	 */
	@FXML
	private void registerButtonAction() {
		boolean success = true;
		String alert = "";
		String email_address = email.getText();
		String password = pw.getText();

		// Empty email
		if (email_address.length() == 0) {
			alert = "Please enter an email!";
			success = false;
		}
		// Empty password
		else if (password.length() == 0) {
			alert = "Please enter a password!";
			success = false;
		}
		// Improper email format
		else if (!isEmail(email_address)) {
			alert = "Improper email format!";
			success = false;
		}
		else {
			for (Account a : list) {
				// Email matches an account
				if (a.getEmail().toLowerCase()
						.equals(email_address.toLowerCase())) {
					alert = "You already have an account!";
					success = false;
					break;
				}
			}
		}
		// Display message
		if (success) {
			list.add(new Account(email_address, password));
			saveData(list);
			alert = "Account created! You may login now.";
		}
		Alert dialog = new Alert(Alert.AlertType.INFORMATION);
		dialog.setContentText(alert);
		dialog.showAndWait();
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

	/**
	 * Save account info to customer.dat
	 * @param customer_file
	 * @param data
	 */
	private void saveData(ArrayList<Account> data) {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(Utilities.CUSTOMER_FILE, false);
			writer = new BufferedWriter(file);

			// Write account email and encrypted password
			for (Account a : data) {
				writer.write(a.getEmail());
				writer.newLine();
				writer.write(a.getEncryptedPassword());
				writer.newLine();
			}

			// Save and close file
			writer.close();
			file.close();
		}
		catch (IOException e) {
			System.out.println("File write error");
		}
	}
}
