package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Checkout implements Initializable {
	@FXML
	private javafx.scene.control.Button submit;
	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.TextField fullname;
	@FXML
	private javafx.scene.control.TextField address;
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

	Text subtotal;
	Text tax;
	Text total;
	Text order_qty;

	Instance i;
	ShoppingCart cart;
	ObservableList<Order> data;

	public void setSubtotalText(Text subtotal) {
		this.subtotal = subtotal;
	}

	public void setTaxText(Text tax) {
		this.tax = tax;
	}

	public void setTotalText(Text total) {
		this.total = total;
	}

	public void setOrderQty(Text order_qty) {
		this.order_qty = order_qty;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BooleanBinding filled = fullname.textProperty().isEqualTo("")
				.or(address.textProperty().isEqualTo(""))
				.or(city.textProperty().isEqualTo(""))
				.or(province.textProperty().isEqualTo(""))
				.or(postal.textProperty().isEqualTo(""))
				.or(country.textProperty().isEqualTo(""))
				.or(phone.textProperty().isEqualTo(""));

		// only enable the ok button when there has been some text entered.
		submit.disableProperty().bind(filled);
	}

	@FXML
	private void cancelButtonAction() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void submitOrder() {
		String error = "";
		String fullname_value = fullname.getText();
		String address_value = address.getText();
		String city_value = city.getText();
		String province_value = province.getText();
		String postal_value = postal.getText();
		String country_value = country.getText();
		String phone_value = phone.getText();

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
			error += "The phone number field should not have any letters.";
		}

		if (!error.equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(error);
			alert.showAndWait();
		}
		else {
			Order o = new Order(i.account.getCart(),
					i.account.getCart().getTaxPrice(), LocalDateTime.now(),
					fullname_value, address_value, city_value, province_value,
					postal_value, country_value, phone_value);
			saveOrder(i.account, o);

			Stage stage = (Stage) submit.getScene().getWindow();
			stage.close();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText(
					"Thank you for your purchase!\nYou will be notified about your order via phone or email.");
			alert.showAndWait();
		}
	}

	public void deductStock(ShoppingCart cart) {
		for (int i = 0; i < cart.size; i++) {
			Book b = cart.getBook(i);
			int quantity_ordered = cart.getNode(i).getQuantity();
			b.setQuantity(b.getQuantity() - quantity_ordered);
			deductBook(b, quantity_ordered);
		}
	}

	public void deductBook(Book b, int quantity) {

		final int LINES_PER_BOOK = 5;
		final int LINES_TO_SKIP = 3;
		int index = i.cart_list.indexOf(b);

		FileReader file_reader;
		FileWriter file_writer;
		BufferedWriter writer;
		BufferedReader reader;

		try {
			file_reader = new FileReader(Utilities.BOOK_FILE);
			reader = new BufferedReader(file_reader);
			String text = "";

			for (int i = 0; i < index + 1; i++) {
				for (int j = 0; j < LINES_PER_BOOK; j++)
					text += reader.readLine() + System.lineSeparator();
			}
			for (int i = 0; i < LINES_TO_SKIP; i++) {
				text += reader.readLine() + System.lineSeparator();
			}
			reader.readLine();
			text += String.valueOf(b.getQuantity()) + System.lineSeparator();
			text += reader.readLine() + System.lineSeparator();

			reader.close();
			file_reader.close();

			file_writer = new FileWriter(Utilities.BOOK_FILE, false);
			writer = new BufferedWriter(file_writer);

			file_writer.write(text);
			writer.close();
			file_writer.close();
		}
		catch (IOException e) {
			System.out.println("File write error");
		}
	}

	public void saveOrder(Account a, Order o) {
		a.addOrder(o);
		data.add(o);
		deductStock(i.account.getCart());
		a.clearCart();
		a.save(i);

		i.cart_list.clear();
		subtotal.setText(String.format("$%.2f", 0F));
		tax.setText(String.format("$%.2f", 0F));
		total.setText(String.format("$%.2f", 0F));
		order_qty.setText(data.size() + " Orders");

	}

	public void setObservableList(ObservableList<Order> data) {
		this.data = data;
	}

	public boolean hasNumbers(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > '0' && s.charAt(i) < '9')
				return true;
		}
		return false;
	}

	public boolean hasLetters(String s) {
		String lower = s.toLowerCase();
		for (int i = 0; i < s.length(); i++) {
			if ((lower.charAt(i) >= 'a' && lower.charAt(i) <= 'z'))
				return true;
		}
		return false;
	}

	public boolean isValidName(String s) {
		String[] array = s.split(" ");

		System.out.println((array.length > 1));
		return (array.length > 1);
	}

	public void setInstance(Instance i) {
		this.i = i;
	}

	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}
}
