package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*
 * TODO Uncommented Classes:
 *
 * BookNode CartList Checkout CustomerEntrance Instance Landing Main Order
 * OrderController OrderNode OrderStack ShoppingCart StoreFront StringNode
 * StringStack Utilities Wishlist
 *
 * Improvements: Payment methods Change password Save address Save payment
 * method Change tax rate Add shipping fee
 */

public class Main extends Application {

	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.getIcons().add(Utilities.ICON);
		this.primaryStage.setTitle("Your Bookstore");
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
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		}
		catch (IOException e) {
			System.out.println("Error!");
		}
	}
}