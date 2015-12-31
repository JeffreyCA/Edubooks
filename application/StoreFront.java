package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class StoreFront implements Initializable {
	@FXML
	private javafx.scene.control.ListView<Book> books;
	@FXML
	private javafx.scene.control.ListView<Book> shopping_cart;

	Instance i;
	ArrayList<Book> list = new ArrayList<Book>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		processBooks();
		i = new Instance(list);

		ObservableList<Book> items = FXCollections.observableArrayList(list);
		books.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new BookCell(i);
					}
				});
		books.setItems(items);

	}

	public void initializeCart(CartList cart_list) {
		i.cart_list = cart_list;
		shopping_cart.setCellFactory(
				new Callback<ListView<Book>, javafx.scene.control.ListCell<Book>>() {
					@Override
					public ListCell<Book> call(ListView<Book> listView) {
						return new ShoppingCartCell(i);
					}
				});
		shopping_cart.setItems(cart_list);
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
					list.add(new Book(title, author, category,
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