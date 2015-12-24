package application;

import javafx.fxml.FXML;

public class LoginController {
	@FXML
	private javafx.scene.control.Button next;
	@FXML
	private javafx.scene.control.PasswordField pw;

	@FXML
	private void buttonAction() {
		String pw = pw.getText();
	}
}
