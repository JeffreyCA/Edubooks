package application;

public class Node {

	private Book value;
	private int quantity;
	private Node link;

	// Constructor
	public Node(Book value, int quantity, Node link) {
		this.value = value;
		this.quantity = quantity;
		this.link = link;
	}

	public Node(Book value, Node link) {
		this.value = value;
		this.quantity = 1;
		this.link = link;
	}

	public Node(Book value) {
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

	public Node getLink() {
		return link;
	}

	public void setLink(Node link) {
		this.link = link;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
