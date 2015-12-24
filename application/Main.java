package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	final String VERSION = "0.1";
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Edubooks v" + VERSION);

		showLandingScreen();
	}

	/**
	* Shows the person overview inside the root layout.
	*/
	public void showLandingScreen() {
		try {
			AnchorPane l = (AnchorPane) FXMLLoader
					.load(Main.class.getResource("Landing.fxml"));

			// Show the scene containing the root layout.
			Scene scene = new Scene(l);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

}