package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

	private SimpleStringProperty author;
	private SimpleStringProperty category;
	private SimpleStringProperty title;
	private SimpleLongProperty quantity;
	private SimpleDoubleProperty price;

	public Book(String title, String author, String category, long quantity,
			double price) {
		this.title = new SimpleStringProperty(title);
		this.author = new SimpleStringProperty(author);
		this.category = new SimpleStringProperty(category);
		this.quantity = new SimpleLongProperty(quantity);
		this.price = new SimpleDoubleProperty(price);
	}

	@Override
	public String toString() {
		return title.get() + " by " + author.get() + ", $" + price.get();
	}

	public String getAuthor() {
		return author.get();
	}

	public void setAuthor(String author) {
		this.author.set(author);
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public double getPrice() {
		return price.get();
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	public long getQuantity() {
		return quantity.get();
	}

	public void setQuantity(long l) {
		this.quantity.set(l);
	}

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}
}
