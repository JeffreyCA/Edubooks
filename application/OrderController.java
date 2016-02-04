package application;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;

public class OrderController {
	@FXML
	private javafx.scene.control.TextArea address;
	@FXML
	private javafx.scene.control.TextArea details;
	@FXML
	private javafx.scene.text.Text text;

	public void initializeOrder(Order o) {
		details.setText(o.toString());
		formatOrder(o);
	}

	// TODO Make tax not dependent on value in Utilities.TAX
	private void formatOrder(Order o) {
		final int LINE_BREAK_SIZE = 30;

		String detail_text = "";
		DateTimeFormatter date_format = DateTimeFormatter
				.ofPattern(Utilities.DATE_FORMAT);
		DateTimeFormatter time_format = DateTimeFormatter
				.ofPattern(Utilities.TIME_FORMAT);

		detail_text += "Account: " + o.getEmail() + "\n";
		detail_text += "Order placed: " + o.getDate().format(date_format)
				+ " at " + o.getDate().format(time_format) + "\n";

		for (int i = 0; i < LINE_BREAK_SIZE; i++) {
			detail_text += '-';
		}

		detail_text += "\n" + o.getItems().toString() + "\n";

		for (int i = 0; i < LINE_BREAK_SIZE; i++) {
			detail_text += '-';
		}
		detail_text += String.format(
				"\nSubtotal: $%.2f\nTax (%d%%): $%.2f\nTotal: $%.2f",
				o.getSubtotal(), (int) (Utilities.TAX * 100), o.getTax(),
				o.getTotal());

		details.setText(detail_text);
		address.setText(o.getAddress().formated());
	}
}
