package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.sun.prism.impl.Disposer.Record;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class ListController implements Initializable {

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

	ArrayList<Book> list = new ArrayList<Book>();
	ObservableList<Book> data;
	final String BOOK_FILE = "books.txt";

	public int countLines(String filename) {
		// Declaration
		FileReader file;
		BufferedReader reader;
		int counter = 0;
		String line;

		try {
			file = new FileReader(filename);
			reader = new BufferedReader(file);

			while ((line = reader.readLine()) != null) {
				// Increment line count
				counter++;
			}
		}
		catch (IOException e) {
			System.out.println("File read error");
		}
		return counter;
	}

	/**
	 * Checks if the given string is a number
	 *
	 * @param value A string containing a value
	 * @return true, if it is able to be parsed into a double, otherwise false
	 */
	public boolean isNumber(String value) {
		try {
			double test = Double.parseDouble(value);
		}
		// If there is an exception caught, then the value is not numeric
		catch (NumberFormatException e) {
			return false;
		}
		// It is numeric if no exception is thrown
		return true;
	}

	public void processBooks() {

		// Constant Declaration
		final int LINES_PER_BOOK = 5;
		final String ERROR = "Error reading file.";

		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = countLines(BOOK_FILE);
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
			file_reader = new FileReader(BOOK_FILE);
			reader = new BufferedReader(file_reader);

			// Loop through all customers
			for (int i = 0; i < books; i++) {
				valid = true;
				// Read and process input
				title = reader.readLine();
				author = reader.readLine();
				category = reader.readLine();
				quantity = reader.readLine();
				if (!isNumber(quantity))
					valid = false;

				price = reader.readLine();
				if (!isNumber(price))
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		processBooks();
		data = FXCollections.observableArrayList(list);

		title.setCellValueFactory(
				new PropertyValueFactory<Book, String>("title"));
		title.setCellFactory(TextFieldTableCell.forTableColumn());
		title.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow())
						.setTitle(t.getNewValue());
				saveData();
			}
		});

		author.setCellValueFactory(
				new PropertyValueFactory<Book, String>("author"));
		author.setCellFactory(TextFieldTableCell.forTableColumn());
		author.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
			@Override
			public void handle(CellEditEvent<Book, String> t) {
				t.getTableView().getItems().get(t.getTablePosition().getRow())
						.setAuthor(t.getNewValue());
				saveData();
			}
		});
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
						saveData();
					}
				});

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
						saveData();

					}
				});

		// Format prices to two decimal places
		price.setCellValueFactory(film -> {
			SimpleStringProperty property = new SimpleStringProperty();
			property.setValue(
					String.format("%.2f", film.getValue().getPrice()));
			return property;
		});

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

		items.setItems(data);

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
					data.remove(book);
					list.remove(book);
					saveData();
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

	public void saveData() {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(BOOK_FILE, false);
			writer = new BufferedWriter(file);

			for (Book b : list) {
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
}
