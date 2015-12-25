package application;

import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("Admin.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("Admin Panel");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showRegister() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("AdminPassword.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("Admin Panel");
			stage.setScene(new Scene(root));
			stage.initModality(Modality.APPLICATION_MODAL);
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
