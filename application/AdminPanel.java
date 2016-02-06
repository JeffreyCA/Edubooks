package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

	@FXML
	private javafx.scene.control.TextField search;
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
	private javafx.scene.control.ComboBox<String> category_box;
	@FXML
	private javafx.scene.control.ComboBox<String> accounts;
	@FXML
	private javafx.scene.control.Button add;
	@FXML
	private javafx.scene.control.ListView<Order> orders;

	ArrayList<Book> book_list = new ArrayList<Book>();
	ArrayList<Order> order_list = new ArrayList<Order>();
	ObservableList<Book> observable_books;
	ObservableList<Order> observable_orders;

	OrderStack order_stack;
	final String DEFAULT_CATEGORY = "-";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		processBooks();
		initializeElements();
		initializeTitleCell();
		initializeAuthorCell();
		initializeCategoryCell();
		initializeQuantityCell();
		initializePriceCell();
		initializeCategory();
		initializeButton();
		initializeOrders();
		initializeOrderAccounts();
	}

	public void initializeElements() {
		category_box.getSelectionModel().select(DEFAULT_CATEGORY);
		observable_books = FXCollections.observableArrayList(book_list);
		items.setItems(observable_books);
	}

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
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent we) {
					saveData(observable_books);
				}
			});

		}
		catch (IOException e) {
		}
	}

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

		// Adding the Button to the cell
		col_action.setCellFactory(
				new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

					@Override
					public TableCell<Record, Boolean> call(
							TableColumn<Record, Boolean> p) {
						return new ButtonCell();
					}

				});
	}

	public void changeList(String category) {
		SortedList<Book> sorted_book_list;
		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<Book> filteredData = new FilteredList<>(observable_books,
				p -> true);

		filteredData.setPredicate(book -> {
			// If filter text is empty, display all persons.
			String author = book.getAuthor().toLowerCase();
			String title = book.getTitle().toLowerCase();
			String filter = search.getText().toLowerCase();

			if (book.getCategory().equals(category)
					|| category.equals(DEFAULT_CATEGORY)) {
				if (search.getText().isEmpty())
					return true;
				else if (author.contains(filter) || title.contains(filter))
					return true;
			}

			return false;
		});

		search.textProperty().addListener((observable, oldValue, newValue) -> {

			filteredData.setPredicate(book -> {
				String author = book.getAuthor().toLowerCase();
				String title = book.getTitle().toLowerCase();
				String filter = newValue.toLowerCase();

				if (category.equals(DEFAULT_CATEGORY)
						|| book.getCategory().equals(category)) {

					if ((newValue == null || newValue.isEmpty()))
						return true;

					else if (title.contains(filter) || author.contains(filter))
						return true;
				}

				// Filter does not match
				return false;
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		sorted_book_list = new SortedList<Book>(filteredData);
		// 4. Bind the SortedList comparator to the TableView comparator.
		sorted_book_list.comparatorProperty().bind(items.comparatorProperty());

		items.setItems(sorted_book_list);
	}

	public void initializeOrders() {
		OrderStack stack = processOrders();

		order_list = stack.toArrayList();
		observable_orders = FXCollections.observableArrayList(order_list);

		orders.setCellFactory(
				new Callback<ListView<Order>, javafx.scene.control.ListCell<Order>>() {
					@Override
					public ListCell<Order> call(ListView<Order> listView) {
						return new AdminOrderCell();
					}
				});
		orders.setItems(observable_orders);
	}

	public void initializeOrderAccounts() {
		StringStack stack = new StringStack();

		for (Order o : observable_orders) {
			String email = o.getEmail();
			if (stack.isEmpty()) {
				stack.push(email);
			}
			else {
				if (!stack.contains(email)) {
					stack.push(email);
				}
			}
		}
		stack.sort();

		ObservableList<String> categories = FXCollections
				.observableArrayList(stack.toArrayList());
		accounts.getItems().add(DEFAULT_CATEGORY);
		accounts.getItems().addAll(categories);

		accounts.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					filterOrderList(newValue);
				});

	}

	public void filterOrderList(String category) {
		FilteredList<Order> filteredData = new FilteredList<>(observable_orders,
				p -> true);

		filteredData.setPredicate(order -> {
			// If filter text is empty, display all persons.
			if (category.equals(DEFAULT_CATEGORY)) {
				return true;
			}

			// Filter matches title
			if (order.getEmail().equals(category)) {
				return true;
			}
			// Filter does not match
			return false;
		});

		orders.setItems(filteredData);
	}

	public void initializeCategory() {
		StringStack stack = new StringStack();

		for (Book b : observable_books) {
			String genre_main = b.getCategory();
			if (stack.isEmpty()) {
				stack.push(genre_main);
			}
			else {
				if (!stack.contains(genre_main)) {
					stack.push(genre_main);
				}
			}
		}
		stack.sort();

		ObservableList<String> categories = FXCollections
				.observableArrayList(stack.toArrayList());
		category_box.getItems().add(DEFAULT_CATEGORY);
		category_box.getItems().addAll(categories);

		category_box.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					changeList(newValue);
				});
	}

	public void filterList(String category) {
		final String DEFAULT_CATEGORY = "-";
		SortedList<Book> sorted_book_list;
		FilteredList<Book> filteredData = new FilteredList<>(observable_books,
				p -> true);

		filteredData.setPredicate(book -> {
			// If filter text is empty, display all persons.
			if (category.equals(DEFAULT_CATEGORY)) {
				return true;
			}

			// Filter matches title
			if (book.getCategory().equals(category)) {
				return true;
			}
			// Filter does not match
			return false;
		});
		// 3. Wrap the FilteredList in a SortedList.
		sorted_book_list = new SortedList<Book>(filteredData);
		// 4. Bind the SortedList comparator to the TableView comparator.
		sorted_book_list.comparatorProperty().bind(items.comparatorProperty());

		items.setItems(sorted_book_list);
	}

	public void processBooks() {
		// Constant Declaration
		final int LINES_PER_BOOK = 5;
		final String ERROR = "Error reading file.";

		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = Utilities.countLines(Utilities.BOOK_FILE);
		int books = lines / LINES_PER_BOOK;
		String title;
		String author;
		String category;
		String quantity;
		String price;
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
				if (!Utilities.isNumber(quantity))
					valid = false;

				price = reader.readLine();
				if (!Utilities.isNumber(price))
					valid = false;

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
			System.out.println(ERROR);
		}
	}

	public String[] countCustomers() {
		final String FILE_TYPE = ".dat";
		final String AT_CHAR = "@";

		File directory = new File(".");
		File[] files = directory.listFiles();

		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(AT_CHAR)
					&& files[i].getName().endsWith(FILE_TYPE))
				count++;
		}

		String[] customers = new String[count];

		count = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(AT_CHAR)
					&& files[i].getName().endsWith(FILE_TYPE)) {
				customers[count] = files[i].getName();
				count++;
			}
		}
		return customers;
	}

	public OrderStack processOrders() {
		final String FILE_TYPE = ".dat";
		OrderStack stack = new OrderStack();

		// File readers
		FileReader file_reader;
		BufferedReader reader;

		String[] customers = countCustomers();
		int accounts = customers.length;

		for (int i = 0; i < accounts; i++) {
			String file_name = customers[i];
			OrderStack customer_stack = new OrderStack();

			try {
				// Initialize file readers for customer file
				file_reader = new FileReader(file_name);
				reader = new BufferedReader(file_reader);

				int cart_items = Integer.parseInt(reader.readLine());

				for (int j = 0; j < cart_items; j++) {
					reader.readLine();
				}

				int wishlist_items = Integer.parseInt(reader.readLine());
				for (int j = 0; j < wishlist_items; j++) {
					reader.readLine();
				}

				int orders = Integer.parseInt(reader.readLine());
				for (int j = 0; j < orders; j++) {
					int books = Integer.parseInt(reader.readLine());
					ShoppingCart order_cart = new ShoppingCart();

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
					String fullname = reader.readLine();
					String street = reader.readLine();
					String city = reader.readLine();
					String province = reader.readLine();
					String postal = reader.readLine();
					String country = reader.readLine();
					String phone = reader.readLine();

					Address address = new Address(fullname, street, city,
							province, postal, country, phone);

					Order o = new Order(order_cart, tax, date, address,
							file_name.replace(FILE_TYPE, ""));
					customer_stack.push(o);
				}

				stack.merge(customer_stack);

				// Close readers
				reader.close();
				file_reader.close();
			}
			// Handle exception
			catch (IOException e) {
				System.out.println("error");
			}
			catch (NumberFormatException e) {
				System.out.println("error");
				e.printStackTrace();
			}
		}
		stack.sort();
		return stack;
	}

	public void saveData(ObservableList<Book> data) {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(Utilities.BOOK_FILE, false);
			writer = new BufferedWriter(file);

			for (Book b : data) {
				writer.write(b.getTitle());
				writer.newLine();
				writer.write(b.getAuthor());
				writer.newLine();
				writer.write(b.getCategory());
				writer.newLine();
				writer.write(String.valueOf(b.getQuantity()));
				writer.newLine();
				writer.write(String.valueOf(b.getPrice()));
				writer.newLine();
			}

			// Save and close file
			writer.close();
			file.close();

		}
		catch (IOException e) {
			System.out.println("File write error");
		}
	}

	// Define the button cell
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

}
