package application;

/**
 * Customer shopping cart - Inherited from BookList
 */
public class ShoppingCart extends BookList {
	// Price of items in the cart (before taxes)
	private double subtotal;

	// Default Constructor
	public ShoppingCart() {
		super();
		subtotal = 0;
	}

	/**
	 * Add book to the shopping cart with indicated quantity
	 * @param book Book to be added
	 * @param quantity Quantity of the book in the cart
	 */
	public void add(Book book, int quantity) {
		// Declaration
		BookNode node = head;
		// Node to be added to the list
		BookNode new_node = new BookNode(book);
		new_node.setQuantity(quantity);

		// Empty list
		if (node == null) {
			head = new_node;
		}
		// Not empty list
		if (node != null) {
			while (node.getLink() != null) {
				node = node.getLink();
			}
			node.setLink(new_node);
		}
		// Increase cart size (measures number of books, not quantity of books)
		size++;
	}

	/**
	 * Empty the shopping cart
	 */
	public void clear() {
		head = null;
		size = 0;
		subtotal = 0;
	}

	/**
	 * Check if the cart contains a certain book
	 * @param book Book to be checked
	 * @return
	 */
	public boolean contains(Book book) {
		// Sequential search for the book
		for (int i = 0; i < size; i++) {
			if (get(i).getValue().equals(book))
				return true;
		}
		// Not found
		return false;
	}

	@Override
	public String toString() {
		String s = "";
		BookNode node = head;
		while (node != null) {
			s += node.getValue().formatted() + "Quantity: "
					+ node.getQuantity();
			if (node.getLink() != null)
				s += "\n\n";

			node = node.getLink();
		}
		return s;
	}

	// Getters and setters
	public double getTaxPrice() {
		return subtotal * Utilities.TAX;
	}

	public double getTotal() {
		updateTotal();
		return subtotal;
	}

	public void setTotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * Recalculate the subtotal of all the items in the cart
	 */
	public void updateTotal() {
		BookNode n;
		subtotal = 0;
		for (int i = 0; i < size; i++) {
			n = get(i);
			subtotal += n.getQuantity() * n.getValue().getPrice();
		}
	}
}
