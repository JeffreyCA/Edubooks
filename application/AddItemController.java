package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AddItemController implements Initializable {

	@FXML
	private javafx.scene.control.Button ok;
	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.TextField title;
	@FXML
	private javafx.scene.control.TextField author;
	@FXML
	private javafx.scene.control.TextField category;
	@FXML
	private javafx.scene.control.TextField quantity;
	@FXML
	private javafx.scene.control.TextField price;

	private javafx.scene.control.TableView<Book> items;

	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();

	}

	@FXML
	private void okButtonAction() {
		int nextIndex = items.getSelectionModel().getSelectedIndex() + 1;
		try {
			Book new_book = new Book(title.getText(), author.getText(),
					category.getText(), Long.valueOf(quantity.getText()),
					Double.valueOf(price.getText()));
			items.getItems().add(nextIndex, new_book);
			items.getSelectionModel().select(nextIndex);
			ListController.saveData(ListController.BOOK_FILE, items.getItems());
			Stage stage = (Stage) ok.getScene().getWindow();
			stage.close();
		}
		catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Invalid Input!");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		BooleanBinding filled = title.textProperty().isEqualTo("")
				.or(author.textProperty().isEqualTo(""))
				.or(category.textProperty().isEqualTo(""))
				.or(quantity.textProperty().isEqualTo(""))
				.or(price.textProperty().isEqualTo(""));

		// only enable the ok button when there has been some text entered.
		ok.disableProperty().bind(filled);
	}

	public void setTable(TableView<Book> t) {
		items = t;
	}
}