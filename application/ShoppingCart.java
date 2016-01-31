package application;

public class ShoppingCart extends BookList {
	private double subtotal;

	public ShoppingCart() {
		super();
		subtotal = 0;
	}

	@Override
	public String toString() {
		String s = "";
		BookNode node = head;
		while (node != null) {
			s += node.getValue().formatted() + "Quantity: "
					+ node.getQuantity();
			node = node.getLink();
		}
		return s;
	}

	public void add(Book item, int quantity) {
		BookNode node = head;
		BookNode new_node = new BookNode(item);
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
		size++;
	}

	public void clear() {
		head = null;
		size = 0;
		subtotal = 0;
	}

	public void updateTotal() {
		BookNode n;
		subtotal = 0;
		for (int i = 0; i < size; i++) {
			n = traverse(i);
			subtotal += n.getQuantity() * n.getValue().getPrice();
		}
	}

	public double getTaxPrice() {
		return subtotal * Utilities.TAX;
	}

	public boolean contains(Book b) {
		for (int i = 0; i < size; i++) {
			if (traverse(i).getValue().equals(b))
				return true;
		}
		return false;
	}

	public double getTotal() {
		updateTotal();
		return subtotal;
	}

	public void setTotal(double subtotal) {
		this.subtotal = subtotal;
	}
}
