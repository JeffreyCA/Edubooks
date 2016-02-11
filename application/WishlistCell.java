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

public class WishlistCell extends ListCell<Book> {
	// Dimensions
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int PRICE_SIZE = 20;
	final int SPACING = 10;

	// Button text
	final String REMOVE_BUTTON_TEXT = "Remove from wishlist";
	final String TRANSFER_BUTTON_TEXT = "Move to cart";

	// Colour codes
	final String BOOK_COLOUR = "#D2B394"; // Light brown
	final String IN_STOCK_COLOUR = "#009900"; // Green
	final String NO_STOCK_COLOUR = "FF0000"; // Red

	// Button to remove item from wishlist
	Button remove;
	// Button to move item to shopping cart
	Button transfer;

	Account account;
	Instance i;

	public WishlistCell(Instance i) {
		super();
		this.i = i;
		account = i.account;
	}

	@Override
	public void updateItem(Book b, boolean empty) {
		super.updateItem(b, empty);

		if (b != null) {
			// Declare and initialize book cell elements
			HBox book_info = new HBox();
			HBox buttons = new HBox();
			HBox outer = new HBox();
			StackPane book_icon = new StackPane();
			Rectangle book_shape = new Rectangle();
			Label text_overlay = new Label();
			Text price = new Text();
			VBox vbox = new VBox();

			// Initialize Buttons
			remove = new Button();
			transfer = new Button();

			// Check if wishlist needs to be cleared
			if (account.getWishlist().isEmpty()) {
				i.wish_list.clear();
			}

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
			book_info.getChildren().addAll(book_icon, vbox);

			// Add buttons to the cell
			remove.setText(REMOVE_BUTTON_TEXT);
			transfer.setText(TRANSFER_BUTTON_TEXT);

			// Set price text
			price.setFont(new Font(PRICE_SIZE));
			price.setText("$" + String.format("%.2f", b.getPrice()));

			/*
			 * The "Move to Cart" button is disabled when the book is already in
			 * the shopping cart
			 */
			BooleanBinding wishlist_binding = Bindings.createBooleanBinding(
					() -> i.cart_list.contains(b), i.cart_list);
			transfer.disableProperty().bind(wishlist_binding);

			// On-click actions for the buttons
			remove.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Remove item from wishlist
					i.removeFromWishlist(b);
					account.save(i);
				}
			});
			transfer.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Move item from wishlist to shopping cart
					i.removeFromWishlist(b);
					i.addToCart(b);
					account.save(i);
				}
			});

			// Set UI elements
			buttons.getChildren().addAll(price, transfer, remove);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(book_info, buttons);

			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}
}
