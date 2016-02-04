package application;

import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Landing {
	@FXML
	private javafx.scene.control.Button admin;
	@FXML
	private javafx.scene.control.Button customer;

	@FXML
	private void customerButtonAction() {
		showCustomerScreen();
	}

	@FXML
	private void adminButtonAction() {
		showAdminLogin();
		// Create admin account if first time accessing the admin panel
		if (!new File(Utilities.ADMIN_LOGIN).isFile())
			showAdminRegister();
	}

	/**
	 * Show customer login screen
	 */
	private void showCustomerScreen() {
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
		catch (Exception e) {
			e.printStackTrace();
		}
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
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show admin registration screen
	 * Asks for admin password to be used for the admin panel
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
		catch (Exception e) {
			System.out.println("Error!");
		}
	}
}
