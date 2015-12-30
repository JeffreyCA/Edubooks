package application;

public class Wishlist extends BookList {

	public void addToCart(Book b, ShoppingCart c) {
		c.add(b);
	}
}
