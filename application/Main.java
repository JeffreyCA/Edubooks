package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*
 * TODO Undocumented Classes:
 *
 * StoreFront
 *
 * Improvements: Payment methods Change password Save address Save payment
 * method Change tax rate Add shipping fee
 *
 * Modifying inventory (adding book or deleting book) messes up customer
 * shopping cart + wishlist
 */

public class Main extends Application {

	// Primary stage
	private Stage stage;

	/**
	 * Initial execution
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.getIcons().add(Utilities.ICON);
		this.stage.setTitle("Your Bookstore");
		showLandingScreen();
	}

	/**
	 * Application landing screen
	 * Customer login and admin login
	 */
	public void showLandingScreen() {
		try {
			AnchorPane l = (AnchorPane) FXMLLoader
					.load(getClass().getResource("Landing.fxml"));

			// Show the scene containing the root layout.
			Scene scene = new Scene(l);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();

		}
		catch (IOException e) {
			System.out.println("Error");
		}
	}
}