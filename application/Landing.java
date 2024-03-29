package application;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class for landing screen
 */
public class Landing {
	// FXML fields
	@FXML
	private javafx.scene.control.Button admin;
	@FXML
	private javafx.scene.control.Button customer;

	/**
	 * Show customer login/registration window when Customer button is clicked
	 */
	@FXML
	private void customerButtonAction() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("Customer.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("Customer Entrance");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	/**
	 * Show admin login window when Administrator button is clicked
	 */
	@FXML
	private void adminButtonAction() {
		showAdminLogin();
		// Create admin account if first time accessing the admin panel
		if (!new File(Utilities.ADMIN_LOGIN).isFile())
			showAdminRegister();
	}

	/**
	 * Show admin login screen
	 */
	private void showAdminLogin() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("AdminLogin.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("Admin Panel");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}

	/**
	 * Show admin registration screen
	 * Ask for admin password to be used for the admin panel
	 */
	public void showAdminRegister() {
		try {
			Parent root = (Parent) new FXMLLoader(
					getClass().getResource("AdminRegistration.fxml")).load();
			Stage stage = new Stage();
			stage.setTitle("First Time Setup");
			stage.getIcons().add(Utilities.ICON);
			stage.setScene(new Scene(root));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}
}
