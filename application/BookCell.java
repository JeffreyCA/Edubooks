package application;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BookCell extends ListCell<Book> {

	// Dimensions
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int PRICE_SIZE = 20;
	final int SPACING = 10;

	// Button text
	final String CART_BUTTON_TEXT = "Add to cart";
	final String IN_STOCK_TEXT = "In stock";
	final String NO_STOCK_TEXT = "Out of stock";
	final String WISHLIST_BUTTON_TEXT = "Add to wishlist";

	// Button colour
	final String BOOK_COLOUR = "#D2B394"; // Light brown
	final String IN_STOCK_COLOUR = "#009900"; // Green
	final String NO_STOCK_COLOUR = "FF0000"; // Red

	// Initialize buttons
	Button cart = new Button();
	Button wishlist = new Button();

	Book item;
	Account account;
	Instance i;

	// Default constructors
	public BookCell(Instance i) {
		super();
		this.i = i;
		account = i.account;

		// Add book to cart
		cart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				i.addToCart(item);
				account.save(i);
			}
		});

		// Add book to wishlist
		wishlist.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				i.addToWishlist(item);
				account.save(i);
			}
		});
	}

	@Override
	public void updateItem(Book b, boolean empty) {
		super.updateItem(b, empty);
		item = b;
		if (b != null) {
			// Declare and initialize book cell elements
			HBox book_info = new HBox();
			HBox buttons = new HBox();
			HBox outer = new HBox();
			VBox vbox = new VBox();
			StackPane book_icon = new StackPane();
			Rectangle book_shape = new Rectangle();
			Label text_overlay = new Label();
			Text price = new Text();
			Text stock_availability = new Text();

			// Set spacing between elements
			book_info.setSpacing(SPACING);
			buttons.setSpacing(SPACING);
			HBox.setHgrow(book_info, Priority.ALWAYS);
			HBox.setHgrow(buttons, Priority.ALWAYS);

			// Construct book icon with its category written on it
			book_icon.setAlignment(Pos.CENTER);
			book_icon.setMinSize(0, 0);
			book_icon.setMaxSize(ICON_WIDTH, ICON_HEIGHT);

			// Construct rectangle shape as the shape of the book icon
			book_shape.setWidth(ICON_WIDTH);
			book_shape.setHeight(ICON_HEIGHT);
			book_shape.setFill(Color.web(BOOK_COLOUR));
			book_shape.setStroke(Color.BLACK);

			// Add text overlay
			text_overlay.setFont(new Font(LABEL_SIZE));
			text_overlay.setText(b.getCategory());
			text_overlay.setWrapText(true);
			text_overlay.setTextAlignment(TextAlignment.CENTER);
			text_overlay.setTextFill(Color.BLACK);
			book_icon.getChildren().addAll(book_shape, text_overlay);

			// Add book information beside the icon
			vbox.getChildren().add(new Text(b.getTitle()));
			vbox.getChildren().add(new Text(b.getAuthor()));
			vbox.getChildren().add(stock_availability);
			book_info.getChildren().addAll(book_icon, vbox);

			// Add buttons to the cell
			cart.setText(CART_BUTTON_TEXT);
			wishlist.setText(WISHLIST_BUTTON_TEXT);
			price.setText("$" + String.format("%.2f", b.getPrice()));
			price.setFont(new Font(PRICE_SIZE));

			// Stock availability
			if (b.getQuantity() > 0) {
				stock_availability.setText(IN_STOCK_TEXT);
				stock_availability.setFill(Color.web(IN_STOCK_COLOUR));
			}
			else {
				stock_availability.setText(NO_STOCK_TEXT);
				stock_availability.setFill(Color.web(NO_STOCK_COLOUR));
			}

			// Disable cart button if item is not in stock or if item is already
			// in the cart
			BooleanBinding cart_binding = Bindings.createBooleanBinding(
					() -> i.cart_list.contains(b) || b.getQuantity() <= 0,
					i.cart_list);
			// Disable wishlist if item is already in the wishlist
			BooleanBinding wishlist_binding = Bindings.createBooleanBinding(
					() -> i.wish_list.contains(b), i.wish_list);

			cart.disableProperty().bind(cart_binding);
			wishlist.disableProperty().bind(wishlist_binding);

			buttons.getChildren().addAll(price, wishlist, cart);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(book_info, buttons);

			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}
}
