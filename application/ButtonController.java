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
		// Create admin account if first time accessing the admin panel
		if (!new File(ADMIN_LOGIN).isFile())
			showRegister();
	}

	/**
	 * Show admin login screen
	 */
	private void showLogin() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("AdminLogin.fxml")).load();
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

	/**
	 * Show admin registration screen
	 * Asks for admin password to be used for the admin panel
	 */
	private void showRegister() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("AdminFirstTime.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("Admin Setup");
			stage.setScene(new Scene(root));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		}
		catch (Exception e) {
			System.out.println("Error!");
		}
	}
}
