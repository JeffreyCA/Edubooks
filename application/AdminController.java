package application;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AdminController {

	@FXML
	private javafx.scene.control.Button back_button;

	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) back_button.getScene().getWindow();
		// do what you have to do
		stage.close();
	}
}
