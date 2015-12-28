package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BookCell extends ListCell<Book> {
	final int ICON_WIDTH = 40;
	final int ICON_HEIGHT = 50;
	final String LIGHT_BROWN = "#d2b394";
	final String NO_STOCK = "Out of stock";
	final int CATEGORY_SIZE = 10;

	@Override
	public void updateItem(Book b, boolean empty) {
		super.updateItem(b, empty);
		Rectangle rect = new Rectangle();

		if (b != null) {
			HBox box = new HBox();
			box.setSpacing(10);
			VBox vbox = new VBox();
			vbox.getChildren().add(new Text(b.getTitle()));
			vbox.getChildren().add(new Text(b.getAuthor()));

			StackPane p = new StackPane();
			p.setMinSize(0, 0);
			p.setMaxSize(ICON_WIDTH, ICON_HEIGHT);
			p.setAlignment(Pos.CENTER);
			rect.setWidth(ICON_WIDTH);
			rect.setHeight(ICON_HEIGHT);
			rect.setFill(Color.web(LIGHT_BROWN));
			rect.setStroke(Color.BLACK);
			p.getChildren().addAll(rect);

			Label l = new Label();
			l.setFont(new Font(CATEGORY_SIZE));
			l.setText(b.getCategory());
			l.setWrapText(true);
			l.setTextAlignment(TextAlignment.CENTER);
			l.setTextFill(Color.BLACK);
			p.getChildren().addAll(l);

			box.getChildren().addAll(p, vbox);
			box.getChildren()
					.add(new Text("$" + String.format("%.2f", b.getPrice())));

			setGraphic(box);
		}
	}
}
