package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AdminPasswordController {

	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.Button done;
	@FXML
	private javafx.scene.control.PasswordField pw;

	/**
	 * Close window when back button is pressed
	 */
	@FXML
	private void cancelButtonAction() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void buttonAction() {
		if (pw.getText().length() == 0) {
			// Display alert: Password cannot be empty
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("Password cannot be empty.");
			alert.showAndWait();
		}
		else {
			try {
				RandomAccessFile file = new RandomAccessFile(
						Utilities.ADMIN_LOGIN, "rw");
				file.write(Account.rot13(pw.getText()).getBytes());
				file.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			Stage stage = (Stage) done.getScene().getWindow();
			stage.close();
		}
	}
}
