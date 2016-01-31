package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ShoppingCartCell extends ListCell<Book> {
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final int LABEL_SIZE = 10;
	final int PRICE_SIZE = 20;
	final int SPACING = 10;

	final String CART_BUTTON = "Remove from cart";
	final String BOOK_COLOUR = "#D2B394"; // Light brown
	final String IN_STOCK = "In stock";
	final String IN_STOCK_COLOUR = "#009900"; // Green
	final String NO_STOCK = "Out of stock";
	final String NO_STOCK_COLOUR = "FF0000"; // Red

	Button cart;
	Account account;
	Instance i;
	int qty;
	Text subtotal;
	Text tax;
	Text total;

	public ShoppingCartCell(Instance i, Text subtotal, Text tax, Text total) {
		super();
		this.i = i;
		account = i.account;
		qty = 1;
		this.subtotal = subtotal;
		this.tax = tax;
		this.total = total;
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
			long q = b.getQuantity();
			cart = new Button();

			if (account.getCart().isEmpty()) {
				i.cart_list.clear();
			}
			else
				qty = account.getCart().getNode(b).getQuantity();

			Spinner<Number> quantity = new Spinner<Number>(1, q, qty);
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
			vbox.getChildren().add(quantity);
			book_info.getChildren().addAll(book_icon, vbox);

			// Add buttons to the cell
			cart.setText(CART_BUTTON);
			price.setText("$" + String.format("%.2f", (qty * b.getPrice())));
			price.setFont(new Font(PRICE_SIZE));

			updatePrices();

			quantity.valueProperty().addListener((obs, oldValue, newValue) -> {
				price.setText("$" + String.format("%.2f",
						(b.getPrice() * newValue.doubleValue())));
				qty = newValue.intValue();
				account.getCart().getNode(b).setQuantity(qty);
				updatePrices();
			});

			buttons.getChildren().addAll(price, cart);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			outer.getChildren().addAll(book_info, buttons);

			cart.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					i.cart_list.remove(b);
					// account.getCart().getNode(b).setQuantity(1);
					account.getCart().delete(b);
					account.save(i);
					qty = 1;
					updatePrices();
				}
			});
			setGraphic(outer);
		}
		else {
			setGraphic(null);
		}
	}

	public void updatePrices() {
		account.getCart().updateTotal();
		double sub_price = i.account.getCart().getTotal();
		double tax_price = Utilities.TAX * sub_price;
		double total_price = sub_price + tax_price;

		subtotal.setText(String.format("$%.2f", sub_price));
		tax.setText(String.format("$%.2f", tax_price));
		total.setText(String.format("$%.2f", total_price));
	}

	public boolean existsInCart(Book b, ShoppingCart c) {
		for (int i = 0; i < c.getSize(); i++) {
			if (b.equals(c.getBook(i)))
				return true;
		}
		return false;
	}
}
