package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Account {

	private String email;
	private String password;
	private ShoppingCart cart;
	private Wishlist wishlist;

	public Account(String email, String password, ShoppingCart cart,
			Wishlist wishlist) {
		this.setEmail(email);
		this.setPassword(password);
		this.setCart(cart);
		this.setWishlist(wishlist);
	}

	public Account(String email, String password) {
		this.setEmail(email);
		this.setPassword(password);
		cart = new ShoppingCart();
		wishlist = new Wishlist();
	}

	public void addToCart(Book book) {
		cart.add(book);
	}

	public void addToWishlist(Book book) {
		wishlist.add(book);
	}

	public void save(Instance i) {
		// File writers
		FileWriter file;
		BufferedWriter writer;
		String name = this.email + ".dat";
		try {
			// Initialize file writers
			file = new FileWriter(name, false);
			writer = new BufferedWriter(file);

			writer.write(String.valueOf(cart.getSize()));
			writer.newLine();
			for (int j = 0; j < cart.getSize(); j++) {
				writer.write(
						String.valueOf(i.getList().indexOf(cart.getBook(j))));
				writer.newLine();
			}

			writer.write(String.valueOf(wishlist.getSize()));
			writer.newLine();
			for (int j = 0; j < wishlist.getSize(); j++) {
				writer.write(String
						.valueOf(i.getList().indexOf(wishlist.getBook(j))));
				writer.newLine();
			}

			// Save and close file
			writer.close();
			file.close();

		}
		catch (IOException e) {
			System.out.println("File write error");
		}
	}

	public void load(Instance i) {
		// File readers
		FileReader file_reader;
		BufferedReader reader;

		String name = this.email + ".dat";
		ArrayList<Book> book_list = i.getList();
		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(name);
			reader = new BufferedReader(file_reader);

			int cart_items = Integer.parseInt(reader.readLine());

			// Loop through all customers
			for (int j = 0; j < cart_items && j < book_list.size(); j++) {
				cart.add(book_list.get(j));
			}

			int wishlist_items = Integer.parseInt(reader.readLine());

			// Loop through all customers
			for (int j = 0; j < wishlist_items && j < book_list.size(); j++) {
				wishlist.add(book_list.get(j));
			}

			// Close readers
			reader.close();
			file_reader.close();
		}
		// Handle exception
		catch (IOException e) {
			System.out.println("error");
		}
	}

	public static String rot13(String s) {
		final int ROT = 13;
		String rot_string = "";

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'm')
				c += ROT;
			else if (c >= 'A' && c <= 'M')
				c += ROT;
			else if (c >= 'n' && c <= 'z')
				c -= ROT;
			else if (c >= 'N' && c <= 'Z')
				c -= ROT;
			rot_string += c;
		}
		return rot_string;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public String getEncryptedPassword() {
		return rot13(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ShoppingCart getCart() {
		return cart;
	}

	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}
}
