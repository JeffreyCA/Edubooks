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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class ItemTable implements Initializable {

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
	private javafx.scene.control.Button add;

	ArrayList<Book> list = new ArrayList<Book>();
	ObservableList<Book> data;
	SortedList<Book> sortedData;
	final static String BOOK_FILE = "books.txt";

	public void processBooks() {

		// Constant Declaration
		final int LINES_PER_BOOK = 5;
		final String ERROR = "Error reading file.";

		// Variable Declaration
		boolean valid;
		// Calculate number of customers from the number of lines in the
		// customer data file
		int lines = Utilities.countLines(BOOK_FILE);
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
				saveData(BOOK_FILE, data);
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
				saveData(BOOK_FILE, data);
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
						saveData(BOOK_FILE, data);
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
						saveData(BOOK_FILE, data);

					}
				});

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
				saveData(BOOK_FILE, data);
			}
		});

		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<Book> filteredData = new FilteredList<>(data, p -> true);
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
						|| book.getAuthor().toLowerCase().contains(filter)
						|| book.getCategory().toLowerCase().contains(filter)) {
					return true;
				}
				// Filter does not match
				return false;
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		sortedData = new SortedList<Book>(filteredData);
		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(items.comparatorProperty());

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
		items.setItems(sortedData);

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
					saveData(BOOK_FILE, data);
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

	public static void saveData(String book_file, ObservableList<Book> data) {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		try {
			// Initialize file writers
			file = new FileWriter(book_file, false);
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

	@FXML
	private void addButtonAction() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("AddItem.fxml"));
			Parent root = (Parent) loader.load();
			AddItemController controller = loader.getController();
			controller.setTable(items);
			controller.setList(data);

			Stage stage = new Stage();
			stage.setTitle("Add Item");
			stage.setScene(new Scene(root));
			stage.initStyle(StageStyle.UNIFIED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
		}
	}
}
