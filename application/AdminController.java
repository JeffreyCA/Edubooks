package application;

import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminController {

	// Declare FXML elements
	@FXML
	private javafx.scene.control.Button back;
	@FXML
	private javafx.scene.control.Button login;
	@FXML
	private javafx.scene.control.PasswordField pw;

	/**
	 * Close window when back button is pressed
	 */
	@FXML
	private void closeButtonAction() {
		Stage stage = (Stage) back.getScene().getWindow();
		stage.close();
	}

	/**
	 * Validate password entered after login button is pressed
	 */
	@FXML
	private void loginButtonAction() {
		try {
			// Read random access password file
			RandomAccessFile file = new RandomAccessFile(Utilities.ADMIN_LOGIN,
					"r");
			String password = file.readLine();
			file.close();

			if (Account.rot13(password).equals(pw.getText())) {
				switchScene();
			}
			else {
				// Display incorrect password alert
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Invalid Password!");
				alert.showAndWait();
			}
		}
		catch (IOException e) {
			new ButtonController().showAdminRegister();
		}
	}

	/**
	 * Enter admin panel scene
	 */
	private void switchScene() {
		AnchorPane l;
		try {
			l = (AnchorPane) FXMLLoader
					.load(Main.class.getResource("AdminPanel.fxml"));
			Stage stage = (Stage) login.getScene().getWindow();
			Scene scene = new Scene(l);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error!");
		}
	}
}
