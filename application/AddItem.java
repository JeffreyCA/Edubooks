package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AddItem implements Initializable {

	// FXML fields
	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.Button ok;
	@FXML
	private javafx.scene.control.TextField author;
	@FXML
	private javafx.scene.control.TextField category;
	@FXML
	private javafx.scene.control.TextField title;
	@FXML
	private javafx.scene.control.TextField price;
	@FXML
	private javafx.scene.control.TextField quantity;

	// Underlying TableView for book database
	private javafx.scene.control.TableView<Book> table;
	private ObservableList<Book> data;

	/**
	 * Close window the "Cancel" button is clicked
	 */
	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	/**
	 *  Add new item to book list when the "Okay" button is clicked
	 */
	@FXML
	private void okButtonAction() {
		// Next index in the book list
		int nextIndex = table.getSelectionModel().getSelectedIndex() + 1;

		try {
			Book book = new Book(title.getText(), author.getText(),
					category.getText(), Long.valueOf(quantity.getText()),
					Double.valueOf(price.getText()));
			data.add(nextIndex, book);

			table.getSelectionModel().select(nextIndex);
			Stage stage = (Stage) ok.getScene().getWindow();
			stage.close();
		}
		catch (NumberFormatException e) {
			// Display error if fields contain invalid characters
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Invalid Input!");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Only enable button when all fields are filled in
		BooleanBinding filled = title.textProperty().isEqualTo("")
				.or(author.textProperty().isEqualTo(""))
				.or(category.textProperty().isEqualTo(""))
				.or(quantity.textProperty().isEqualTo(""))
				.or(price.textProperty().isEqualTo(""));
		ok.disableProperty().bind(filled);
	}

	// Setter methods for book list and table
	public void setTable(TableView<Book> table) {
		this.table = table;
	}

	public void setList(ObservableList<Book> list) {
		data = list;
	}
}
