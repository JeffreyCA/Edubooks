package application;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class CartList extends SimpleListProperty<Book> {

	public CartList() {
		super(FXCollections.observableArrayList());
	}

	public void copy(BookList list) {
		BookNode n = list.head;

		while (n != null) {
			this.add(n.getValue());
			n = n.getLink();
		}
	}
}