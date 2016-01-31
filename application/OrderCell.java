package application;

import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OrderCell extends ListCell<Order> {
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int DATE_SIZE = 16;
	final int PRICE_SIZE = 18;
	final int DATE_WIDTH = 145;
	final int SPACING = 10;

	final String CART_BUTTON = "View Details";
	final String BOOK_COLOUR = "#D2B394"; // Light brown
	final String IN_STOCK = "In stock";
	final String IN_STOCK_COLOUR = "#009900"; // Green
	final String NO_STOCK = "Out of stock";
	final String NO_STOCK_COLOUR = "FF0000"; // Red
	final String DATE_FORMAT = "MMMM D, yyyy";
	final String TIME_FORMAT = "hh:mm a";
	Button cart;

	public OrderCell() {
		super();
	}

	@Override
	public void updateItem(Order o, boolean empty) {
		super.updateItem(o, empty);

		if (o != null) {
			// Declare and initialize order cell elements
			HBox left = new HBox();
			HBox right = new HBox();
			VBox date_box = new VBox();
			HBox buttons = new HBox();
			HBox outer = new HBox();
			Text number_items = new Text();
			VBox prices = new VBox();

			DateTimeFormatter date_format = DateTimeFormatter
					.ofPattern(DATE_FORMAT);
			DateTimeFormatter time_format = DateTimeFormatter
					.ofPattern(TIME_FORMAT);

			// Set spacing between elements
			buttons.setSpacing(SPACING);
			date_box.setSpacing(SPACING);

			HBox.setHgrow(buttons, Priority.ALWAYS);

			Text date = new Text();
			date.setFont(new Font(DATE_SIZE));
			date.setText(o.getDate().format(date_format));
			Text time = new Text();
			time.setFont(new Font(DATE_SIZE));
			time.setText(o.getDate().format(time_format));

			date_box.getChildren().addAll(date, time);
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

			cart = new Button();
			cart.setText(CART_BUTTON);
			right.setAlignment(Pos.CENTER_RIGHT);
			right.getChildren().addAll(prices);

			buttons.getChildren().addAll(right, cart);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(left, buttons);
			outer.setAlignment(Pos.CENTER);

			cart.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
				}
			});
			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}
}
