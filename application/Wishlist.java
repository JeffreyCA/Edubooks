package application;

/**
 * Customer wishlist - Inherited from BookList
 */
public class Wishlist extends BookList {
	/**
	 * Add book that is in the wishlist to the customer's shopping cart
	 * The book remains in the wishlist
	 * @param book Book to be added
	 * @param cart Shopping cart of the customer
	 */
	public void addToCart(Book book, ShoppingCart cart) {
		cart.add(book);
	}
}
