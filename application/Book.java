package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Book class
 */
public class Book {

	/*
	 * Use Property rather than primitive datatypes (String, double, etc.)
	 * because the Book objectsneeds to be manipulatable in a TableView
	 */
	private SimpleStringProperty author;
	private SimpleStringProperty category;
	private SimpleStringProperty title;
	private SimpleLongProperty quantity;
	private SimpleDoubleProperty price;

	// Default constructor
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
		return title.get() + "\n" + author.get() + "\n" + category.get() + "\n"
				+ price.get();
	}

	/**
	 * Formatted toString method, with a heading of each property of the book
	 * followed by the property on individual lines
	 *
	 * @return Formatted string
	 */
	public String formatted() {
		return "Title: " + title.get() + "\nAuthor: " + author.get()
				+ "\nCategory: " + category.get() + "\nPrice: "
				+ String.format("$%.2f", price.get()) + "\n";
	}

	/*
	 * Getter and setter methods
	 *
	 * For Property types, the syntax is different, get() and set() are used
	 */
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
