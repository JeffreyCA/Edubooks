package application;

/**
 * BookNode class, which stores Book objects
 */
public class BookNode {
	// Private fields
	private Book value;
	private BookNode link;
	// This quantity refers to the quantity of a book in shopping cart
	private int quantity;

	// Default constructor
	public BookNode(Book value) {
		this.value = value;
		this.quantity = 1;
		this.link = null;
	}

	/**
	 * Used in context of a shopping cart
	 */
	public BookNode(Book value, int quantity, BookNode link) {
		this.value = value;
		this.quantity = quantity;
		this.link = link;
	}

	/**
	 * Used in other contexts (list, wishlist, which do not require the
	 * quantity)
	 */
	public BookNode(Book value, BookNode link) {
		this.value = value;
		this.quantity = 1;
		this.link = link;
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
