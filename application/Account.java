package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// Account class for customer objects
public class Account {

	// Login details
	private String email;
	private String password;

	private ShoppingCart cart;
	private Wishlist wishlist;
	private OrderStack order_stack;

	// Default constructor
	public Account(String email, String password, ShoppingCart cart,
			Wishlist wishlist) {
		this.email = email;
		this.password = password;
		this.cart = cart;
		this.wishlist = wishlist;
	}

	// Another constructor for creating accounts with email and password
	public Account(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Add book to shopping cart
	 *
	 * @param book the book
	 */
	public void addToCart(Book book) {
		cart.add(book);
	}

	/**
	 * Clear shopping cart
	 */
	public void clearCart() {
		cart = new ShoppingCart();
	}

	/**
	 * Removes book from cart
	 *
	 * @param book the book to be removed
	 */
	public boolean removeFromCart(Book book) {
		return cart.delete(book);
	}

	/**
	 * Adds book to wishlist
	 *
	 * @param book the book to be added
	 */
	public void addToWishlist(Book book) {
		wishlist.add(book);
	}

	/**
	 * Clear wishlist
	 */
	public void clearWishlist() {
		wishlist = new Wishlist();
	}

	/**
	 * Removes book from wishlist
	 *
	 * @param book the book to be removed
	 */
	public boolean removeFromWishlist(Book book) {
		return wishlist.delete(book);
	}

	/**
	 * Write account data to data file
	 *
	 * @param i the i
	 */
	public void save(Instance i) {
		// File writers
		FileWriter file;
		BufferedWriter writer;

		// File name
		String name = this.email + ".dat";

		// Database list of books
		BookList book_list = i.book_list;

		try {
			// Initialize file writers
			file = new FileWriter(name, false);
			writer = new BufferedWriter(file);

			// Get number of items in the shopping cart
			writer.write(String.valueOf(cart.size));
			writer.newLine();

			/*
			 * Write index value of the book that is in the shopping cart. The
			 * index value refers to the book's position in the store database
			 */
			for (int j = 0; j < cart.size; j++) {
				writer.write(
						String.valueOf(book_list.indexOf(cart.getBook(j))));
				writer.newLine();
			}

			// Get number of items in the wishlist
			writer.write(String.valueOf(wishlist.size));
			writer.newLine();
			/*
			 * Write index value of the book that is in the wishlist. The index
			 * value refers to the book's position in the store database
			 */
			for (int j = 0; j < wishlist.size; j++) {
				writer.write(
						String.valueOf(book_list.indexOf(wishlist.getBook(j))));
				writer.newLine();
			}

			writer.write(String.valueOf(order_stack.size));
			writer.newLine();

			// Write order info by traversing through the order stack
			OrderNode node = order_stack.top;
			for (int j = 0; j < order_stack.size; j++) {
				writer.write(String.valueOf(node.getValue()));
				writer.newLine();
				node = node.getLink();
			}

			// Save and close file
			writer.close();
			file.close();
		}
		catch (IOException e) {
			System.out.println("File write error");
		}
	}

	/**
	 * Load account data from the data file
	 *
	 * @param i the Instance containing extra account info
	 */
	public void load(Instance i) {
		// File readers
		FileReader file_reader;
		BufferedReader reader;

		// File name
		String name = this.email + ".dat";
		BookList book_list = i.book_list;

		// Initialize lists
		cart = new ShoppingCart();
		wishlist = new Wishlist();
		order_stack = new OrderStack();

		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(name);
			reader = new BufferedReader(file_reader);

			// Get number of items in the shopping cart
			int cart_items = Integer.parseInt(reader.readLine());

			// Add book located at the index value in the book database
			for (int j = 0; j < cart_items; j++) {
				int index = Integer.parseInt(reader.readLine());
				if (index < book_list.size
						&& book_list.get(index).getQuantity() > 0)
					cart.add(book_list.get(index).getValue());
			}

			// Get number of items in the wishlist
			int wishlist_items = Integer.parseInt(reader.readLine());
			System.out.println("Wishlist: " + wishlist_items);
			for (int j = 0; j < wishlist_items; j++) {
				int index = Integer.parseInt(reader.readLine());
				if (index < book_list.size
						&& book_list.get(index).getQuantity() > 0)
					wishlist.add(book_list.get(index).getValue());
			}

			// Get number of orders
			int orders = Integer.parseInt(reader.readLine());

			// Read order information line-by-line
			for (int j = 0; j < orders; j++) {
				int books = Integer.parseInt(reader.readLine());
				ShoppingCart order_cart = new ShoppingCart();
				for (int k = 0; k < books; k++) {
					String title = reader.readLine();
					String author = reader.readLine();
					String category = reader.readLine();
					double price = Double.parseDouble(reader.readLine());
					int quantity = Integer.parseInt(reader.readLine());

					order_cart.add(new Book(title, author, category, 1, price),
							quantity);
				}

				double tax = Double.parseDouble(reader.readLine());
				LocalDateTime date = LocalDateTime.parse(reader.readLine());
				String fullname = reader.readLine();
				String street = reader.readLine();
				String city = reader.readLine();
				String province = reader.readLine();
				String postal = reader.readLine();
				String country = reader.readLine();
				String phone = reader.readLine();
				Address address = new Address(fullname, street, city, province,
						postal, country, phone);

				Order o = new Order(order_cart, tax, date, address, email);
				this.order_stack.push(o);
			}

			// Stack needs to be reversed since newer orders were imported
			// first, and ended up at the bottom of the stack
			order_stack.reverse();

			// Close readers
			reader.close();
			file_reader.close();
		}
		// Handle exception
		catch (IOException e) {
			System.out.println("error");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Password encryption technique
	 *
	 * ROT13 is a simple algorithm which shifts each letter in a string
	 * 13 characters forward/backward in the alphabet. This algorithm
	 * can be used to decode and encode a string since 13 is halfway
	 * between the alphabet (26 letters).
	 *
	 * @param s String to be encoded/decoded
	 * @return Modified string (shifted 13 letters)
	 */
	public static String rot13(String s) {
		// Amount to shift by
		final int ROT = 13;
		String rot_string = "";

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			// Shift forward or backward depending on position in the alphabet
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

	// Setters and getters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return rot13(password);
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

	public void addOrder(Order o) {
		order_stack.push(o);
	}

	public OrderStack getOrderStack() {
		return order_stack;
	}

	public void setOrderStack(OrderStack order_stack) {
		this.order_stack = order_stack;
	}
}
