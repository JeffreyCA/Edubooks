package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import com.sun.prism.impl.Disposer.Record;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class AdminPanel implements Initializable {

	// FXML elements
	@FXML
	private javafx.scene.control.ComboBox<String> accounts;
	@FXML
	private javafx.scene.control.ComboBox<String> category_box;
	@FXML
	private javafx.scene.control.ListView<Order> orders;
	@FXML
	private javafx.scene.control.TableView<Book> items;
	@FXML
	private javafx.scene.control.TableColumn<Book, String> title;
	@FXML
	private javafx.scene.control.TableColumn<Book, String> author;
	@FXML
	private javafx.scene.control.TableColumn<Book, String> category;
	@FXML
	private javafx.scene.control.TableColumn<Book, Number> quantity;
	@FXML
	private javafx.scene.control.TableColumn<Book, String> price;
	@FXML
	private javafx.scene.control.TextField search;

	final String DEFAULT_CATEGORY = "-";

	// ObservableLists of book database and customer orders
	ObservableList<Book> observable_books;
	ObservableList<Order> observable_orders;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		processBooks();
		initializeTitleCell();
		initializeAuthorCell();
		initializeCategoryCell();
		initializeQuantityCell();
		initializePriceCell();
		// Initialize filter showing all books
		filterList(DEFAULT_CATEGORY);
		initializeCategories();
		initializeButton();
		initializeOrders();
		initializeOrderAccounts();
	}

	/**
	 * Open separate dialog to add a new item when "Add Item" button is clicked
	 */
	@FXML
	private void addButtonAction() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("AddItem.fxml"));
			Parent root = (Parent) loader.load();
			AddItem controller = loader.getController();
			controller.setTable(items);
			controller.setList(observable_books);

			Stage stage = new Stage();
			stage.setTitle("Add Item");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(new Scene(root));
			stage.initStyle(StageStyle.UNIFIED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			stage.show();

			// Save newly added book info to the data file
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent we) {
					saveData(observable_books);
				}
			});
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	/**
	 * Update the book list whenever a search term is inputted or a category
	 * is selected using the combo box
	 *
	 * @param category Category selected in the combo box
	 */
	public void filterList(String category) {
		SortedList<Book> sorted_book_list;

		// Wrap the ObservableList in a FilteredList
		FilteredList<Book> filteredData = new FilteredList<>(observable_books,
				p -> true);

		// Set conditions for search bar queries
		filteredData.setPredicate(book -> {
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
			filteredData.setPredicate(book -> {
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

		// Wrap the FilteredList in a SortedList so that the admin can use the
		// column tabs to sort data
		sorted_book_list = new SortedList<Book>(filteredData);
		sorted_book_list.comparatorProperty().bind(items.comparatorProperty());

		// Set TableView data
		items.setItems(sorted_book_list);
	}

	/**
	 * Update order view when combobox is changed
	 * @param account Email of the selected account
	 */
	public void filterOrderList(String account) {
		FilteredList<Order> filteredData = new FilteredList<>(observable_orders,
				p -> true);

		filteredData.setPredicate(order -> {
			// If filter text is empty, display all orders
			if (account.equals(DEFAULT_CATEGORY)) {
				return true;
			}

			// Filter matches title
			if (order.getEmail().equals(account)) {
				return true;
			}
			// Filter does not match
			return false;
		});

		orders.setItems(filteredData);
	}

	public void initializeCategories() {
		StringStack category_stack = new StringStack();

		for (Book book : observable_books) {
			String genre_main = book.getCategory();
			if (category_stack.isEmpty()) {
				category_stack.push(genre_main);
			}
			else {
				if (!category_stack.contains(genre_main)) {
					category_stack.push(genre_main);
				}
			}
		}
		category_stack.sort();

		ObservableList<String> categories = FXCollections
				.observableArrayList(category_stack.toArrayList());
		category_box.getItems().add(DEFAULT_CATEGORY);
		category_box.getItems().addAll(categories);

		category_box.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					filterList(newValue);
				});
	}

	/**
	 * Read book data from data file
	 */
	public void processBooks() {
		// Constant Declaration
		final int LINES_PER_BOOK = 5;

		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = Utilities.countLines(Utilities.BOOK_FILE);
		int books = lines / LINES_PER_BOOK;
		// Book information
		String title;
		String author;
		String category;
		String quantity;
		String price;

		// Linked list storing all books
		BookList book_list = new BookList();

		// File readers
		FileReader file_reader;
		BufferedReader reader;

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

				// Check validity of file contents
				if (!Utilities.isNumber(quantity))
					valid = false;

				price = reader.readLine();
				if (!Utilities.isNumber(price))
					valid = false;

				// Add new book to the list if valid info was given
				if (valid) {
					book_list.add(new Book(title, author, category,
							Integer.parseInt(quantity),
							Double.parseDouble(price)));
				}
			}
			// Close readers
			reader.close();
			file_reader.close();
		}
		catch (IOException e) {
			System.out.println("Error reading file");
		}

		// Update table
		observable_books = FXCollections
				.observableArrayList(book_list.toArrayList());
		items.setItems(observable_books);
	}

	/**
	 * Count number of customers according to the number of customer .dat files
	 * @return array of emails of customers
	 */
	public String[] countCustomers() {
		// Constant declaration
		final String FILE_TYPE = ".dat";
		final String AT_CHAR = "@";

		// Set directory to current path
		File directory = new File(".");
		// Get list of all the files in the current path
		File[] files = directory.listFiles();

		// Count the number of .dat files containing an email (signifies
		// customer file)
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(AT_CHAR)
					&& files[i].getName().endsWith(FILE_TYPE))
				count++;
		}

		String[] customers = new String[count];
		count = 0;

		// Add emails to an array of strings
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(AT_CHAR)
					&& files[i].getName().endsWith(FILE_TYPE)) {
				customers[count] = files[i].getName();
				count++;
			}
		}
		return customers;
	}

	/**
	 * Read order data from customer data files
	 * @return Stack of orders of all customers
	 */
	public OrderStack processOrders() {
		// Constant declaration
		final String FILE_TYPE = ".dat";
		// Stack containing all orders
		OrderStack order_stack = new OrderStack();
		// Store data filenames in array
		String[] customers = countCustomers();
		int accounts = customers.length;

		// File readers
		FileReader file_reader;
		BufferedReader reader;

		// Loop through each customer
		for (int i = 0; i < accounts; i++) {
			String file_name = customers[i];
			// Order stack for a particular customer
			OrderStack customer_stack = new OrderStack();

			try {
				// Initialize file readers for customer file
				file_reader = new FileReader(file_name);
				reader = new BufferedReader(file_reader);

				// Skip through shopping cart items
				int cart_items = Integer.parseInt(reader.readLine());

				for (int j = 0; j < cart_items; j++) {
					reader.readLine();
				}
				// Skip through wishlist items
				int wishlist_items = Integer.parseInt(reader.readLine());
				for (int j = 0; j < wishlist_items; j++) {
					reader.readLine();
				}

				// Read in order info for each customer
				int orders = Integer.parseInt(reader.readLine());
				for (int j = 0; j < orders; j++) {
					int books = Integer.parseInt(reader.readLine());
					ShoppingCart order_cart = new ShoppingCart();

					// Data for each book in the order
					for (int k = 0; k < books; k++) {
						String title = reader.readLine();
						String author = reader.readLine();
						String category = reader.readLine();
						double price = Double.parseDouble(reader.readLine());
						int quantity = Integer.parseInt(reader.readLine());

						order_cart.add(
								new Book(title, author, category, 1, price),
								quantity);
					}

					double tax = Double.parseDouble(reader.readLine());
					LocalDateTime date = LocalDateTime.parse(reader.readLine());
					// Read in address information
					String full_name = reader.readLine();
					String street = reader.readLine();
					String city = reader.readLine();
					String province = reader.readLine();
					String postal = reader.readLine();
					String country = reader.readLine();
					String phone = reader.readLine();

					Address address = new Address(full_name, street, city,
							province, postal, country, phone);

					// Email is the file name minus the .dat extension
					Order order = new Order(order_cart, tax, date, address,
							file_name.replace(FILE_TYPE, ""));
					customer_stack.push(order);
				}

				// Merge the customer order stack to the master order stack
				order_stack.merge(customer_stack);

				// Close readers
				reader.close();
				file_reader.close();
			}
			// Handle exception
			catch (IOException e) {
				System.out.println("Error");
			}
			catch (NumberFormatException e) {
				System.out.println("Error");
			}
		}
		// Sort stack according to date (most recent at top, oldest at
		// bottom
		order_stack.sort();
		return order_stack;
	}

	/**
	 * Write book data to the data file
	 * @param data ObservableList of Book objects
	 */
	public void saveData(ObservableList<Book> data) {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(Utilities.BOOK_FILE, false);
			writer = new BufferedWriter(file);

			// Write all book info to the file
			for (Book book : data) {
				writer.write(book.getTitle());
				writer.newLine();
				writer.write(book.getAuthor());
				writer.newLine();
				writer.write(book.getCategory());
				writer.newLine();
				// BufferedWriter can only write Strings properly
				writer.write(String.valueOf(book.getQuantity()));
				writer.newLine();
				writer.write(String.valueOf(book.getPrice()));
				writer.newLine();
			}

			// Save and close file
			writer.close();
			file.close();
		}
		catch (IOException e) {
			System.out.println("File write error!");
		}
	}

	// Initialization methods for TableView
	// (Author, title, category, price, quantity)
	public void initializeAuthorCell() {
		author.setCellValueFactory(
				new PropertyValueFactory<Book, String>("author"));
		author.setCellFactory(TextFieldTableCell.forTableColumn());
		author.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow())
						.setAuthor(t.getNewValue());
				saveData(observable_books);
			}
		});
	}

	public void initializeTitleCell() {
		title.setCellValueFactory(
				new PropertyValueFactory<Book, String>("title"));
		title.setCellFactory(TextFieldTableCell.forTableColumn());
		title.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow())
						.setTitle(t.getNewValue());
				saveData(observable_books);
			}
		});
	}

	public void initializeCategoryCell() {
		category.setCellValueFactory(
				new PropertyValueFactory<Book, String>("category"));
		category.setCellFactory(TextFieldTableCell.forTableColumn());
		category.setOnEditCommit(
				new EventHandler<CellEditEvent<Book, String>>() {
					@Override
					public void handle(CellEditEvent<Book, String> t) {
						t.getTableView().getItems()
								.get(t.getTablePosition().getRow())
								.setCategory(t.getNewValue());
						saveData(observable_books);
					}
				});
	}

	public void initializePriceCell() {
		// Format prices to two decimal places
		price.setCellValueFactory(film -> {
			SimpleStringProperty property = new SimpleStringProperty();
			property.setValue(
					String.format("%.2f", film.getValue().getPrice()));
			return property;
		});
		price.setCellFactory(TextFieldTableCell.forTableColumn());
		price.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow())
						.setPrice(Double.valueOf(t.getNewValue()));
				saveData(observable_books);
			}
		});
	}

	public void initializeQuantityCell() {
		quantity.setCellValueFactory(
				new PropertyValueFactory<Book, Number>("quantity"));
		quantity.setCellFactory(TextFieldTableCell
				.<Book, Number> forTableColumn(new NumberStringConverter()));
		quantity.setOnEditCommit(
				new EventHandler<CellEditEvent<Book, Number>>() {
					@Override
					public void handle(CellEditEvent<Book, Number> t) {

						t.getTableView().getItems()
								.get(t.getTablePosition().getRow())
								.setQuantity((long) t.getNewValue());
						saveData(observable_books);

					}
				});
	}

	/**
	 * Initialize order list cells
	 */
	public void initializeOrders() {
		OrderStack stack = processOrders();
		observable_orders = FXCollections
				.observableArrayList(stack.toArrayList());

		orders.setCellFactory(
				new Callback<ListView<Order>, javafx.scene.control.ListCell<Order>>() {
					@Override
					public ListCell<Order> call(ListView<Order> listView) {
						return new AdminOrderCell();
					}
				});
		orders.setItems(observable_orders);
	}

	/**
	 * Initialize combobox for selecting account in order view
	 */
	public void initializeOrderAccounts() {
		StringStack account_stack = new StringStack();

		// Add all accounts to a list
		for (Order order : observable_orders) {
			String email = order.getEmail();
			if (account_stack.isEmpty()) {
				account_stack.push(email);
			}
			else {
				if (!account_stack.contains(email)) {
					account_stack.push(email);
				}
			}
		}
		account_stack.sort();

		ObservableList<String> categories = FXCollections
				.observableArrayList(account_stack.toArrayList());
		accounts.getItems().add(DEFAULT_CATEGORY);
		accounts.getItems().addAll(categories);

		// When combobox is changed
		accounts.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					filterOrderList(newValue);
				});

	}

	/**
	 * Class for initializing the delete button for the order view
	 *
	 * Must be nested in the AdminPanel class because it needs access
	 * to the ObservableList
	 */
	private class ButtonCell extends TableCell<Record, Boolean> {
		final Button cellButton = new Button("Delete");

		ButtonCell() {

			// Action when the button is pressed
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					// get Selected Item
					Book book = (Book) ButtonCell.this.getTableView().getItems()
							.get(ButtonCell.this.getIndex());
					// remove selected item from the table list
					observable_books.remove(book);
					saveData(observable_books);
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
			else {
				setGraphic(null);
			}
		}
	}

	/**
	 * Initialize delete button for the books
	 * The button is embedded into the table
	 */
	public void initializeButton() {
		// Insert Button
		TableColumn col_action = new TableColumn<>("Action");
		col_action.setStyle("-fx-alignment: CENTER;");
		items.getColumns().add(col_action);

		col_action.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(
							TableColumn.CellDataFeatures<Record, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		// Add the Button to the cell
		col_action.setCellFactory(
				new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

					@Override
					public TableCell<Record, Boolean> call(
							TableColumn<Record, Boolean> p) {
						return new ButtonCell();
					}

				});
	}
}
