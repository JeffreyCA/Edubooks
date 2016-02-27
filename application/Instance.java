package application;

import javafx.collections.ObservableList;

public class Instance {
	protected Account account;
	protected BookList book_list;
	ObservableList<Book> observable_cart;
	ObservableList<Book> observable_wishlist;

	public Instance(BookList book_list) {
		this.book_list = book_list;
		account = null;
	}

	public void loadData() {
		account.load(this);
	}

	public void addToCart(Book b) {
		account.addToCart(b);
		observable_cart.add(b);
	}

	public void removeFromCart(Book b) {
		account.removeFromCart(b);
		observable_cart.remove(b);
	}

	public void addToWishlist(Book b) {
		account.addToWishlist(b);
		observable_wishlist.add(b);
	}

	public void removeFromWishlist(Book b) {
		account.removeFromWishlist(b);
		observable_wishlist.remove(b);
	}
}
