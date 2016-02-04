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
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int DATE_SIZE = 16;
	final int PRICE_SIZE = 18;
	final int DATE_WIDTH = 145;
	final int SPACING = 10;

	final String DETAILS_BUTTON = "View Details";
	final String BOOK_COLOUR = "#D2B394"; // Light brown
	final String IN_STOCK = "In stock";
	final String IN_STOCK_COLOUR = "#009900"; // Green
	final String NO_STOCK = "Out of stock";
	final String NO_STOCK_COLOUR = "FF0000"; // Red

	Button details;

	public AdminOrderCell() {
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
			Text account = new Text();
			Text number_items = new Text();
			VBox prices = new VBox();

			DateTimeFormatter date_format = DateTimeFormatter
					.ofPattern(Utilities.DATE_FORMAT);
			DateTimeFormatter time_format = DateTimeFormatter
					.ofPattern(Utilities.TIME_FORMAT);

			Text date = new Text();
			Text time = new Text();

			// Set spacing between elements
			buttons.setSpacing(SPACING);
			// date_box.setSpacing(SPACING);

			HBox.setHgrow(buttons, Priority.ALWAYS);

			date.setFont(new Font(DATE_SIZE));
			date.setText(o.getDate().format(date_format));

			time.setFont(new Font(DATE_SIZE));
			time.setText(o.getDate().format(time_format));

			account.setText(o.getEmail());
			account.setFont(Font.font(null, FontWeight.BOLD, 12));

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

			details = new Button();
			details.setText(DETAILS_BUTTON);
			right.setAlignment(Pos.CENTER_RIGHT);
			right.getChildren().addAll(prices);

			buttons.getChildren().addAll(right, details);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(left, buttons);
			outer.setAlignment(Pos.CENTER);

			details.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					viewOrderDetails(o);
				}
			});
			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}

	public void viewOrderDetails(Order o) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("OrderDetails.fxml"));
			Parent root = loader.load();
			OrderController controller = loader.getController();
			Stage stage = new Stage();

			controller.initializeOrder(o);
			stage.setTitle("Order Details");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error!");
		}
	}
}
