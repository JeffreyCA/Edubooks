package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private javafx.scene.control.Button next;
	@FXML
	private javafx.scene.control.PasswordField pw;

	@FXML
	private void buttonAction() {
		try {
			RandomAccessFile file = new RandomAccessFile(
					ButtonController.ADMIN_LOGIN, "rw");
			file.write(Account.rot13(pw.getText()).getBytes());
			file.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Stage stage = (Stage) next.getScene().getWindow();
		stage.close();
	}
}
