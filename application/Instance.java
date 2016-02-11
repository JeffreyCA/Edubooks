package application;

import java.util.ArrayList;

public class Instance {
	protected Account account;
	protected ArrayList<Book> list;
	protected CartList cart_list;
	protected CartList wish_list;

	public Instance(ArrayList<Book> list) {
		this.list = list;
		account = null;
	}

	public void loadData() {
		account.load(this);
	}

	public void addToCart(Book b) {
		account.addToCart(b);
		cart_list.add(b);
	}

	public void removeFromCart(Book b) {
		account.removeFromCart(b);
		cart_list.remove(b);
	}

	public void addToWishlist(Book b) {
		account.addToWishlist(b);
		wish_list.add(b);
	}

	public void removeFromWishlist(Book b) {
		account.removeFromWishlist(b);
		wish_list.remove(b);
	}
}
