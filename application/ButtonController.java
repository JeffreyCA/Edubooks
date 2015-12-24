package application;

import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ButtonController {

	final static String ADMIN_LOGIN = "admin.dat";
	@FXML
	private javafx.scene.control.Button button;

	@FXML
	private void adminButtonAction() {
		showLogin();
		if (!fileExists(ADMIN_LOGIN))
			showRegister();
	}

	private void showLogin() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("Admin.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Admin Panel");
			stage.setScene(new Scene(root1));
			stage.setResizable(false);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showRegister() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("AdminPassword.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Admin Panel");
			stage.setScene(new Scene(root1));
			stage.setResizable(false);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean fileExists(String filename) {
		return new File(filename).isFile();
	}
}
