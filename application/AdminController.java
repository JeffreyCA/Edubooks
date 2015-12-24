package application;

import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AdminController {

	@FXML
	private javafx.scene.control.Button back_button;
	@FXML
	private javafx.scene.control.Button login;
	@FXML
	private javafx.scene.control.PasswordField pw;

	@FXML
	private void closeButtonAction() {
		// get a handle to the stage
		Stage stage = (Stage) back_button.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	@FXML
	private void loginButtonAction() {
		try {
			RandomAccessFile file = new RandomAccessFile(
					ButtonController.ADMIN_LOGIN, "r");
			String password = file.readLine();
			file.close();

			if (Account.rot13(password).equals(pw.getText())) {
				System.out.println("Works!");
			}
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Invalid Password!");
				alert.showAndWait();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
