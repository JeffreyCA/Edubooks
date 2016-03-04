package application;

import javafx.collections.ObservableList;

/**
 * General class for passing information between classes
 * when not possible by regular ways
 *
 * Contains customer account, its shopping cart and wishlist contents,
 * and the database of books
 */
public class Instance {
	// Customer's account
	protected Account account;
	// Book database
	protected BookList book_list;
	// ObservableList of customer's shopping cart
	protected ObservableList<Book> observable_cart;
	// ObservableList of customer's wishlist
	protected ObservableList<Book> observable_wishlist;

	// Default Constructor
	public Instance(BookList book_list) {
		this.book_list = book_list;
		account = null;
	}

	/**
	 * Load account
	 */
	public void loadData() {
		account.load(this);
	}

	// Add and remove books from cart and wishlist
	// Modifies both the list and the observable list
	public void addToCart(Book book) {
		account.addToCart(book);
		observable_cart.add(book);
	}

	public void removeFromCart(Book book) {
		account.removeFromCart(book);
		observable_cart.remove(book);
	}

	public void addToWishlist(Book book) {
		account.addToWishlist(book);
		observable_wishlist.add(book);
	}

	public void removeFromWishlist(Book book) {
		account.removeFromWishlist(book);
		observable_wishlist.remove(book);
	}
}
