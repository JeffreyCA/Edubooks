package application;

public class BookNode {

	private Book value;
	private int quantity;
	private BookNode link;

	// Constructor
	public BookNode(Book value, int quantity, BookNode link) {
		this.value = value;
		this.quantity = quantity;
		this.link = link;
	}

	public BookNode(Book value, BookNode link) {
		this.value = value;
		this.quantity = 1;
		this.link = link;
	}

	public BookNode(Book value) {
		this.value = value;
		this.quantity = 1;
		this.link = null;
	}

	// Getters and setters
	public Book getValue() {
		return value;
	}

	public void setValue(Book value) {
		this.value = value;
	}

	public BookNode getLink() {
		return link;
	}

	public void setLink(BookNode link) {
		this.link = link;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
