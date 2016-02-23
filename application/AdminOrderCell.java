package application;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminOrderCell extends ListCell<Order> {

	// Dimensions
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int DATE_SIZE = 16;
	final int PRICE_SIZE = 18;
	final int DATE_WIDTH = 145;
	final int SPACING = 10;

	// Button text
	final String DETAILS_BUTTON_TEXT = "View Details";

	// Button to view order details
	Button details;

	public AdminOrderCell() {
		super();
	}

	@Override
	public void updateItem(Order o, boolean empty) {
		super.updateItem(o, empty);

		if (o != null) {
			// Declare and initialize order cell elements
			HBox buttons = new HBox();
			HBox left = new HBox();
			HBox right = new HBox();
			HBox outer = new HBox();
			VBox prices = new VBox();
			VBox date_box = new VBox();
			Text account = new Text();
			Text number_items = new Text();
			Text date = new Text();
			Text time = new Text();

			// Date Format Example: January 01, 2016
			DateTimeFormatter date_format = DateTimeFormatter
					.ofPattern(Utilities.DATE_FORMAT);
			// Time Format Example: 12:00 AM
			DateTimeFormatter time_format = DateTimeFormatter
					.ofPattern(Utilities.TIME_FORMAT);

			// Set spacing between elements
			buttons.setSpacing(SPACING);
			HBox.setHgrow(buttons, Priority.ALWAYS);

			// Set date and time text
			date.setFont(new Font(DATE_SIZE));
			time.setFont(new Font(DATE_SIZE));
			date.setText(o.getDate().format(date_format));
			time.setText(o.getDate().format(time_format));

			// Display account email on top-left corner
			account.setText(o.getEmail());
			account.setFont(Font.font(null, FontWeight.BOLD, 12));

			// Group date and time in a box
			date_box.getChildren().addAll(account, date, time);
			date_box.setAlignment(Pos.CENTER_LEFT);
			date_box.setMinWidth(DATE_WIDTH);

			number_items.setFont(new Font(DATE_SIZE));
			number_items.setText(String
					.valueOf(o.getItems().getItemQuantity() + " item(s)"));

			left.setAlignment(Pos.CENTER_LEFT);
			left.getChildren().addAll(date_box, number_items);

			// Add book information beside the icon
			String tax = String.format("$%.2f", o.getTax());
			String subtotal = String.format("$%.2f", o.getSubtotal());
			String total = String.format("$%.2f", o.getTotal());
			Text total_text = new Text("Total: " + total);

			total_text.setFont(new Font(PRICE_SIZE));
			prices.getChildren().add(new Text("Tax:\t\t" + tax));
			prices.getChildren().add(new Text("Subtotal:\t" + subtotal));
			prices.getChildren().add(total_text);

			right.setAlignment(Pos.CENTER_RIGHT);
			right.getChildren().addAll(prices);

			// View details button
			details = new Button();
			details.setText(DETAILS_BUTTON_TEXT);
			// On-click action
			details.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					viewOrderDetails(o);
				}
			});

			buttons.getChildren().addAll(right, details);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(left, buttons);
			outer.setAlignment(Pos.CENTER);
			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}

	/**
	 * View order details in a new window
	 *
	 * @param o the order to be viewed
	 */
	public void viewOrderDetails(Order o) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("OrderDetails.fxml"));
			Parent root = loader.load();
			OrderController controller = loader.getController();
			Stage stage = new Stage();

			controller.initializeOrder(o);
			stage.setTitle("Order Details");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error!");
		}
	}
}
