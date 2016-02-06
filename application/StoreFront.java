package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

public class StoreFront implements Initializable {
	@FXML
	private javafx.scene.control.ListView<Book> books;
	@FXML
	private javafx.scene.control.ListView<Book> shopping_cart;
	@FXML
	private javafx.scene.control.ListView<Order> orders;
	@FXML
	private javafx.scene.control.Button checkout;
	@FXML
	private javafx.scene.control.Tab logout;
	@FXML
	private javafx.scene.control.TextField search;
	@FXML
	private javafx.scene.control.ComboBox<String> category;
	@FXML
	private javafx.scene.text.Text subtotal;
	@FXML
	private javafx.scene.text.Text tax;
	@FXML
	private javafx.scene.text.Text total;
	@FXML
	private javafx.scene.text.Text order_qty;

	Instance i;
	ArrayList<Book> book_list = new ArrayList<Book>();
	ArrayList<Order> order_list = new ArrayList<Order>();
	ObservableList<Order> observable_orders;
	ObservableList<Book> observable_books;
	OrderStack order_stack;

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
			controller.setObservableList(observable_orders);
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
			e.printStackTrace();
		}
	}

	public void setOrders(OrderStack order_stack) {
		this.order_stack = order_stack;
		order_list = order_stack.toArrayList();
	}

	public void closeStage() {
		Stage stage = (Stage) logout.getTabPane().getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		processBooks();
		setLogoutButton();
		initializeCells();
		initializeList();
		initializeCategory();
	}

	public void initializeCategory() {
		StringStack stack = new StringStack();

		for (Book b : observable_books) {
			String genre = b.getCategory();
			if (stack.isEmpty()) {
				stack.push(genre);
			}
			else {
				if (!stack.contains(genre)) {
					stack.push(genre);
				}
			}
		}
		stack.sort();

		ObservableList<String> categories = FXCollections
				.observableArrayList(stack.toArrayList());
		category.getItems().add("-");
		category.getItems().addAll(categories);

		category.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					filterList(newValue);
				});
	}

	public void filterList(String category) {
		final String DEFAULT_CATEGORY = "-";

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

		books.setItems(filteredData);
	}

	public void initializeCells() {
		books.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new BookCell(i);
					}
				});
	}

	public void setLogoutButton() {
		// Manage logout button click behaviour
		logout.getTabPane().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Tab>() {
					@Override
					public void changed(
							ObservableValue<? extends Tab> observable,
							Tab oldTab, Tab newTab) {

						if (newTab == logout) {
							Alert alert = new Alert(
									Alert.AlertType.CONFIRMATION);
							alert.setContentText("Are you sure?");
							alert.showAndWait()
									.filter(response -> response == ButtonType.OK)
									.ifPresent(response -> closeStage());

							logout.getTabPane().getSelectionModel()
									.select(oldTab);
						}
					}
				});
	}

	public void initializeList() {
		i = new Instance(book_list);
		observable_books = FXCollections.observableArrayList(book_list);
		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<Book> filteredData = new FilteredList<>(observable_books,
				p -> true);
		// 2. Set the filter Predicate whenever the filter changes.
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(book -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare book title, author, and category of every book with
				// filter text
				String filter = newValue.toLowerCase();

				// Filter matches title
				if (book.getTitle().toLowerCase().contains(filter)
						|| book.getAuthor().toLowerCase().contains(filter)) {
					return true;
				}
				// Filter does not match
				return false;
			});
		});

		books.setItems(filteredData);
	}

	public void initializeOrders(OrderStack stack) {
		order_list = stack.toArrayList();
		observable_orders = FXCollections.observableArrayList(order_list);

		orders.setCellFactory(
				new Callback<ListView<Order>, javafx.scene.control.ListCell<Order>>() {
					@Override
					public ListCell<Order> call(ListView<Order> listView) {
						return new OrderCell();
					}
				});
		orders.setItems(observable_orders);
		order_qty.setText(stack.size + " Orders");
	}

	public void initializeCart(CartList cart_list) {
		i.cart_list = cart_list;
		shopping_cart.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new ShoppingCartCell(i, subtotal, tax, total);
					}
				});
		shopping_cart.setItems(cart_list);

		double sub_price = i.account.getCart().getTotal();
		double tax_price = Utilities.TAX * sub_price;
		double total_price = sub_price + tax_price;

		subtotal.setText(String.format("$%.2f", sub_price));
		tax.setText(String.format("$%.2f", tax_price));
		total.setText(String.format("$%.2f", total_price));
		BooleanBinding n = Bindings.isEmpty(cart_list);
		checkout.disableProperty().bind(n);
	}

	public Instance getInstance() {
		return i;
	}

	public void setInstance(Instance i) {
		this.i = i;
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
}