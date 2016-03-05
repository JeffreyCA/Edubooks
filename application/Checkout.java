package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Checkout class for submitting purchases
 */
public class Checkout implements Initializable {
	// FXML elements
	@FXML
	private javafx.scene.control.Button submit;
	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.TextField fullname;
	@FXML
	private javafx.scene.control.TextField street;
	@FXML
	private javafx.scene.control.TextField city;
	@FXML
	private javafx.scene.control.TextField province;
	@FXML
	private javafx.scene.control.TextField postal;
	@FXML
	private javafx.scene.control.TextField country;
	@FXML
	private javafx.scene.control.TextField phone;

	// Textboxes containing shopping cart numbers
	Text subtotal;
	Text tax;
	Text total;
	Text order_qty;

	Instance i;
	ShoppingCart cart;
	ObservableList<Book> observable_books;
	ObservableList<Order> observable_orders;
	ListView<Book> list_view;

	/**
	 * Initialize submit button
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BooleanBinding filled = fullname.textProperty().isEqualTo("")
				.or(street.textProperty().isEqualTo(""))
				.or(city.textProperty().isEqualTo(""))
				.or(province.textProperty().isEqualTo(""))
				.or(postal.textProperty().isEqualTo(""))
				.or(country.textProperty().isEqualTo(""))
				.or(phone.textProperty().isEqualTo(""));

		// Only enable the ok button when there has been some text entered.
		submit.disableProperty().bind(filled);
	}

	/**
	 * Close checkout window if Cancel button is pressed
	 */
	@FXML
	private void cancelButtonAction() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * Order submission
	 */
	@FXML
	private void submitOrder() {
		// Customer order information fields
		String fullname_value = fullname.getText();
		String street_value = street.getText();
		String city_value = city.getText();
		String province_value = province.getText();
		String postal_value = postal.getText();
		String country_value = country.getText();
		String phone_value = phone.getText();

		// Append error messages to be displayed in an alert popup
		String error = "";

		if (!isValidName(fullname_value)) {
			error += "Please enter your full name.\n";
		}
		if (hasNumbers(fullname_value)) {
			error += "The name field should not have any numbers.\n";
		}
		if (hasNumbers(city_value)) {
			error += "The city field should not have any numbers.\n";
		}
		if (hasNumbers(province_value)) {
			error += "The province field should not have any numbers.\n";
		}
		if (hasLetters(phone_value)) {
			error += "The phone number field should not have any letters.\n";
		}
		if (!isValidPhone(phone_value)) {
			error += "The phone number does not have the appropriate amount of digits.";
		}

		// Show alert if user enters invalid information
		if (!error.equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(error);
			alert.showAndWait();
		}
		else {
			// Correctly format phone number like: (416) 123-1234
			phone_value = formatPhone(phone_value);

			// Order contains an Address object
			Address address = new Address(fullname_value, street_value,
					city_value, province_value, postal_value, country_value,
					phone_value);
			Order order = new Order(i.account.getCart(),
					i.account.getCart().getTaxPrice(), LocalDateTime.now(),
					address, i.account.getEmail());

			// Save order information to data file
			saveOrder(i.account, order);

			// Close window and display success message
			Stage stage = (Stage) submit.getScene().getWindow();
			stage.close();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(
					"Thank you for your purchase!\nYou will be notified about "
							+ "your order via phone or email.");
			alert.showAndWait();
		}
	}

	// Data validation

	/**
	 * Format phone number to be displayed in the customer's order history
	 * Example: (416) 123-1234
	 * @param s Phone number as string
	 * @return Formatted phone number
	 */
	public String formatPhone(String s) {
		// Constants
		final char DASH = '-';
		final int FIRST_DASH_POSITION = 3;
		final int SECOND_DASH_POSITION = 6;

		// Remove all characters such as spaces, dashes, and brackets
		s = removeChars(s);
		s = s.substring(0, FIRST_DASH_POSITION) + DASH
				+ s.substring(FIRST_DASH_POSITION, SECOND_DASH_POSITION) + DASH
				+ s.substring(SECOND_DASH_POSITION);
		return s;
	}

	/**
	 * Checks if the string contains any numbers
	 * @param s String to be checked
	 * @return true if the string contains numbers, otherwise false
	 */
	public boolean hasNumbers(String s) {
		// Check each character if it is between the ranges of 0-9
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > '0' && s.charAt(i) < '9')
				return true;
		}
		return false;
	}

	/**
	 * Checks if the string contains any letters
	 * @param s String to be checked
	 * @return true if the string contains letters, otherwise false
	 */
	public boolean hasLetters(String s) {
		// Convert all uppercases to lowercases for easier comparisons
		String lower = s.toLowerCase();

		// Check each character if it is between the ranges of a-z
		for (int i = 0; i < s.length(); i++) {
			if ((lower.charAt(i) >= 'a' && lower.charAt(i) <= 'z'))
				return true;
		}
		return false;
	}

	/**
	 * Check if the full name has both a first and last name
	 * @param s Full name
	 * @return true if the name contains a space, otherwise false
	 */
	public boolean isValidName(String s) {
		String[] array = s.split(" ");

		System.out.println((array.length > 1));
		return (array.length > 1);
	}

	/**
	 * Check if the phone number contains 10 digits, excluding all
	 * other characters
	 *
	 * @param s Phone number
	 * @return true, if the number contains 10 digits and does not start with
	 * 0, otherwise false
	 */
	public boolean isValidPhone(String s) {
		final int PHONE_DIGITS = 10;
		// Eliminate all characters in the phone number string
		s = removeChars(s);

		if (s.length() == PHONE_DIGITS && s.charAt(0) != '0')
			return true;
		return false;
	}

	/**
	 * Remove all unnecessary characters from a given phone number
	 * @param s Phone number
	 * @return Bare phone number with all formatting removed
	 */
	public String removeChars(String s) {
		s = s.replace("-", "");
		s = s.replace(" ", "");
		s = s.replace("(", "");
		s = s.replace(")", "");
		s = s.replace(".", "");
		return s;
	}

	/**
	 * Deduct book quantity in stock after making a purchase
	 * @param cart Customer's ShoppingCart
	 */
	public void deductStock(ShoppingCart cart) {
		for (int i = 0; i < cart.size; i++) {
			Book book = cart.getBook(i);
			int quantity_ordered = cart.getNode(i).getQuantity();
			// Deduct from the book quantity ordered
			book.setQuantity(book.getQuantity() - quantity_ordered);
			deductBook();
		}
	}

	/**
	 * Write book data to the data file
	 */
	public void deductBook() {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(Utilities.BOOK_FILE, false);
			writer = new BufferedWriter(file);

			// Write all book info to the file
			for (Book book : observable_books) {
				writer.write(book.getTitle());
				writer.newLine();
				writer.write(book.getAuthor());
				writer.newLine();
				writer.write(book.getCategory());
				writer.newLine();
				// BufferedWriter can write only Strings
				writer.write(String.valueOf(book.getQuantity()));
				writer.newLine();
				writer.write(String.valueOf(book.getPrice()));
				writer.newLine();
			}

			// Save and close file
			writer.close();
			file.close();
		}
		catch (IOException e) {
			System.out.println("File write error!");
		}
	}

	/**
	 * Save customer order in his/her history
	 * @param a Account of customer
	 * @param order Order
	 */
	public void saveOrder(Account account, Order order) {
		account.addOrder(order);
		observable_orders.add(0, order);
		deductStock(i.account.getCart());
		// Clear shopping cart and refresh storefront
		list_view.refresh();
		account.clearCart();
		account.save(i);

		i.observable_cart.clear();
		subtotal.setText(String.format("$%.2f", 0F));
		tax.setText(String.format("$%.2f", 0F));
		total.setText(String.format("$%.2f", 0F));
		order_qty.setText(observable_orders.size() + " Orders");
	}

	// Information is passed from StoreFront class to Checkout class through
	// these setter methods
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

	public void setInstance(Instance i) {
		this.i = i;
	}

	public void setList(ListView<Book> list_view) {
		this.list_view = list_view;
	}

	public void setObservableBooks(ObservableList<Book> observable_books) {
		this.observable_books = observable_books;
	}

	public void setObservableOrders(ObservableList<Order> observable_orders) {
		this.observable_orders = observable_orders;
	}

	public void setOrderQty(Text order_qty) {
		this.order_qty = order_qty;
	}

	public void setSubtotalText(Text subtotal) {
		this.subtotal = subtotal;
	}

	public void setTaxText(Text tax) {
		this.tax = tax;
	}

	public void setTotalText(Text total) {
		this.total = total;
	}
}
