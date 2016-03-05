package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main class for customer's storefront
 */
public class StoreFront implements Initializable {
	// FXML elements
	@FXML
	private javafx.scene.control.Button checkout;
	@FXML
	private javafx.scene.control.ComboBox<String> category;
	@FXML
	private javafx.scene.control.ListView<Book> books;
	@FXML
	private javafx.scene.control.ListView<Order> orders;
	@FXML
	private javafx.scene.control.ListView<Book> shopping_cart;
	@FXML
	private javafx.scene.control.ListView<Book> wishlist;
	@FXML
	private javafx.scene.control.Tab logout;
	@FXML
	private javafx.scene.text.Text subtotal;
	@FXML
	private javafx.scene.text.Text tax;
	@FXML
	private javafx.scene.text.Text total;
	@FXML
	private javafx.scene.text.Text order_qty;
	@FXML
	private javafx.scene.control.TextField search;

	// Default setting on comboboxes
	final String DEFAULT_CATEGORY = "-";

	// Customer data
	Instance i;
	// Book database
	BookList book_list;
	// Customer orders
	OrderStack order_stack;
	// Visible list in GUI
	ObservableList<Book> observable_books;
	ObservableList<Book> observable_cart;
	ObservableList<Book> observable_wishlist;
	ObservableList<Order> observable_orders;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		processBooks();
		setLogoutButton();
		initializeCells();
		initializeList();
		filterList(DEFAULT_CATEGORY);
		initializeCategories();
	}

	/**
	 * Customer checks out items using the Checkout button
	 */
	@FXML
	private void checkoutButtonAction() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("Checkout.fxml"));
			Parent root = (Parent) loader.load();
			Checkout controller = loader.getController();
			Stage stage = new Stage();
			Scene scene = new Scene(root);

			// Pass data to controller
			controller.setObservableBooks(observable_books);
			controller.setObservableOrders(observable_orders);

			// Set cart information
			controller.setInstance(i);
			controller.setSubtotalText(subtotal);
			controller.setTaxText(tax);
			controller.setTotalText(total);
			controller.setOrderQty(order_qty);
			controller.setList(books);

			stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("Checkout");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	/**
	 * Close window when user logs out
	 */
	public void closeStage() {
		Stage stage = (Stage) logout.getTabPane().getScene().getWindow();
		stage.close();
	}

	/**
	 * Set prices and shopping cart information
	 */
	public void initializeCart() {
		shopping_cart.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new ShoppingCartCell(i, subtotal, tax, total,
								observable_wishlist);
					}
				});
		shopping_cart.setItems(observable_cart);

		// Calculate prices
		double sub_price = i.account.getCart().getTotal();
		double tax_price = Utilities.TAX * sub_price;
		double total_price = sub_price + tax_price;

		// Format prices
		subtotal.setText(String.format("$%.2f", sub_price));
		tax.setText(String.format("$%.2f", tax_price));
		total.setText(String.format("$%.2f", total_price));
		BooleanBinding binding = Bindings.isEmpty(observable_cart);
		checkout.disableProperty().bind(binding);
	}

	/**
	 * Initialize all book categories for the combobox
	 */
	public void initializeCategories() {
		StringStack stack = new StringStack();

		// Add all categories to the combobox
		for (Book book : observable_books) {
			String genre = book.getCategory();
			if (stack.isEmpty()) {
				stack.push(genre);
			}
			else {
				if (!stack.contains(genre)) {
					stack.push(genre);
				}
			}
		}
		// Sort categories alphabetically
		stack.sort();

		// Add categories to the combobox
		ObservableList<String> categories = FXCollections
				.observableArrayList(stack.toArrayList());
		category.getItems().add("-");
		category.getItems().addAll(categories);

		// Change list when combobox is changed
		category.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					filterList(newValue);
				});
	}

	/**
	 * Initialize book cell in storefront
	 */
	public void initializeCells() {
		books.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new BookCell(i, observable_cart,
								observable_wishlist);
					}
				});
	}

	/**
	 * Initialize book list
	 */
	public void initializeList() {
		i = new Instance(book_list);
		observable_books = FXCollections
				.observableArrayList(book_list.toArrayList());
	}

	/**
	 * Initialize shopping cart and wishlist
	 */
	public void initializeObservableLists() {
		observable_cart = FXCollections
				.observableArrayList(i.account.getCart().toArrayList());
		observable_wishlist = FXCollections
				.observableArrayList(i.account.getWishlist().toArrayList());
	}

	/**
	 * Initialize customer order list
	 * @param stack Stack of orders
	 */
	public void initializeOrders(OrderStack stack) {
		observable_orders = FXCollections
				.observableArrayList(stack.toArrayList());

		orders.setCellFactory(
				new Callback<ListView<Order>, javafx.scene.control.ListCell<Order>>() {
					@Override
					public ListCell<Order> call(ListView<Order> listView) {
						return new OrderCell();
					}
				});
		orders.setItems(observable_orders);
		// Display how many orders the customer has made
		order_qty.setText(stack.size + " Orders");
	}

	/**
	 * Initialize customer wishlist contents
	 */
	public void initializeWishlist() {
		wishlist.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new WishlistCell(i, observable_cart);
					}
				});
		wishlist.setItems(observable_wishlist);
	}

	/**
	 * Update observable book list when combobox is changed
	 * @param category Category of sorted books
	 */
	public void filterList(String category) {
		// Wrap the ObservableList in a FilteredList
		FilteredList<Book> filtered_list = new FilteredList<>(observable_books,
				p -> true);

		// Set conditions for search bar queries
		filtered_list.setPredicate(book -> {

			String author = book.getAuthor().toLowerCase();
			String title = book.getTitle().toLowerCase();
			String filter = search.getText().toLowerCase();

			if (book.getCategory().equals(category)
					|| category.equals(DEFAULT_CATEGORY)) {
				// If filter text is empty, display all books
				if (search.getText().isEmpty())
					return true;
				// Compare search query to book list
				else if (author.contains(filter) || title.contains(filter))
					return true;
			}

			// Hide the books that do not match the search query
			return false;
		});

		// Listen for additional changes in the search text field
		search.textProperty().addListener((observable, oldValue, newValue) -> {

			// Conditions same as above
			filtered_list.setPredicate(book -> {
				String author = book.getAuthor().toLowerCase();
				String title = book.getTitle().toLowerCase();
				String filter = newValue.toLowerCase();

				if (book.getCategory().equals(category)
						|| category.equals(DEFAULT_CATEGORY)) {

					// Different condition than above
					if ((newValue == null || newValue.isEmpty()))
						return true;

					else if (title.contains(filter) || author.contains(filter))
						return true;
				}

				// Filter does not match
				return false;
			});
		});

		// Set TableView data
		books.setItems(filtered_list);
	}

	/**
	 * Manage logout button click behaviour
	 */
	public void setLogoutButton() {
		logout.getTabPane().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Tab>() {
					@Override
					public void changed(
							ObservableValue<? extends Tab> observable,
							Tab oldTab, Tab newTab) {

						// Display confirmation message
						if (newTab == logout) {
							Alert alert = new Alert(
									Alert.AlertType.CONFIRMATION);
							alert.setContentText("Are you sure?");
							alert.showAndWait()
									.filter(response -> response == ButtonType.OK)
									.ifPresent(response -> closeStage());

							// Return back to previous tab if user cancels
							// logout
							logout.getTabPane().getSelectionModel()
									.select(oldTab);
						}
					}
				});
	}

	/**
	 * Import books from data file to a BookList
	 */
	public void processBooks() {
		// Lines in the file dedicated to each book
		final int LINES_PER_BOOK = 5;
		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = Utilities.countLines(Utilities.BOOK_FILE);
		int books = lines / LINES_PER_BOOK;
		// Book fields
		String title;
		String author;
		String category;
		String quantity;
		String price;

		// File readers
		FileReader file_reader;
		BufferedReader reader;

		book_list = new BookList();

		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(Utilities.BOOK_FILE);
			reader = new BufferedReader(file_reader);

			// Loop through all customers
			for (int i = 0; i < books; i++) {
				valid = true;
				// Read and process input
				title = reader.readLine();
				author = reader.readLine();
				category = reader.readLine();
				quantity = reader.readLine();

				// Check for valid information
				if (!Utilities.isNumber(quantity))
					valid = false;

				price = reader.readLine();
				if (!Utilities.isNumber(price))
					valid = false;

				// Add book to the the BookList if all file input is valid
				if (valid)
					book_list.add(new Book(title, author, category,
							Integer.parseInt(quantity),
							Double.parseDouble(price)));

			}
			// Close readers
			reader.close();
			file_reader.close();
		}
		// Handle exception
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	// Getters and setters
	public void setOrders(OrderStack order_stack) {
		this.order_stack = order_stack;
	}

	public Instance getInstance() {
		return i;
	}

	public void setInstance(Instance i) {
		this.i = i;
	}
}