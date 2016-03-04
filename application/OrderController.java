package application;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;

/**
 * This class manages how order information is displayed
 */
public class OrderController {
	// FXML fields
	@FXML
	private javafx.scene.control.TextArea address;
	@FXML
	private javafx.scene.control.TextArea details;
	@FXML
	private javafx.scene.text.Text text;

	/**
	 * Display order information in text area
	 * @param order Customer order
	 */
	public void initializeOrder(Order order) {
		details.setText(order.toString());
		formatOrder(order);
	}

	/**
	 * Format order nicely with appropriate headings
	 * @param order Customer order
	 */
	private void formatOrder(Order order) {
		// Constant declaration
		final int SEPARATOR_LENGTH = 30;

		String detail_text = "";

		// Apply date and time formats
		DateTimeFormatter date_format = DateTimeFormatter
				.ofPattern(Utilities.DATE_FORMAT);
		DateTimeFormatter time_format = DateTimeFormatter
				.ofPattern(Utilities.TIME_FORMAT);

		// Append order details to string
		detail_text += "Account: " + order.getEmail() + "\n";
		detail_text += "Order placed: " + order.getDate().format(date_format)
				+ " at " + order.getDate().format(time_format) + "\n";

		// Add separator line between sections
		for (int i = 0; i < SEPARATOR_LENGTH; i++) {
			detail_text += '-';
		}

		detail_text += "\n" + order.getItems().toString() + "\n";

		for (int i = 0; i < SEPARATOR_LENGTH; i++) {
			detail_text += '-';
		}
		// Add price information
		detail_text += String.format(
				"\nSubtotal: $%.2f\nTax (%d%%): $%.2f\nTotal: $%.2f",
				order.getSubtotal(), (int) (Utilities.TAX * 100),
				order.getTax(), order.getTotal());

		// Set text area to display order information
		details.setText(detail_text);
		address.setText(order.getAddress().formated());
	}
}
