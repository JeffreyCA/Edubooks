package application;

public class ShoppingCart extends BookList {
	private double total;

	public ShoppingCart() {
		super();
		total = 0;
	}

	public void clear() {
		head = null;
		size = 0;
		total = 0;
	}

	public void updateTotal() {
		Node n;
		total = 0;
		for (int i = 0; i < size; i++) {
			n = traverse(i);
			System.out.println(n.getQuantity());
			total += n.getQuantity() * n.getValue().getPrice();
		}
	}

	public double getTaxPrice() {
		return total * Utilities.TAX;
	}

	public boolean contains(Book b) {
		for (int i = 0; i < size; i++) {
			if (traverse(i).getValue().equals(b))
				return true;
		}
		return false;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
