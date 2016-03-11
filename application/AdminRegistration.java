package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Controller for admin registration dialog
 */
public class AdminRegistration implements Initializable {
	// FXML elements
	@FXML
	private javafx.scene.control.Button cancel;
	@FXML
	private javafx.scene.control.Button done;
	@FXML
	private javafx.scene.control.PasswordField pw;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Disable button if password field is empt
		done.disableProperty().bind(Bindings.isEmpty(pw.textProperty()));

	}

	/**
	 * Save password to file when done button is clicked
	 */
	@FXML
	private void doneButtonAction() {
		try {
			// Open file
			RandomAccessFile file = new RandomAccessFile(Utilities.ADMIN_LOGIN,
					"rw");
			// Write encrypted password to file
			file.write(Account.rot13(pw.getText()).getBytes());
			file.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File Error");
		}
		catch (IOException e) {
			System.out.println("Error");
		}

		// Close window when done
		Stage stage = (Stage) done.getScene().getWindow();
		stage.close();
	}

	/**
	 * Close window when back button is pressed
	 */
	@FXML
	private void cancelButtonAction() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}
}
