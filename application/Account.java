package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account {

	private String email;
	private String password;
	private ShoppingCart cart;
	private Wishlist wishlist;
	private OrderStack order_stack;

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
	}

	public void addToCart(Book book) {
		cart.add(book);
	}

	public void clearCart() {
		cart = new ShoppingCart();
	}

	public void removeFromCart(Book book) {
		cart.delete(book);
	}

	public void addToWishlist(Book book) {
		wishlist.add(book);
	}

	public void save(Instance i) {
		// File writers
		FileWriter file;
		BufferedWriter writer;
		String name = this.email + ".dat";
		ArrayList<Book> book_list = i.list;

		try {
			// Initialize file writers
			file = new FileWriter(name, false);
			writer = new BufferedWriter(file);

			writer.write(String.valueOf(cart.getSize()));
			writer.newLine();
			for (int j = 0; j < cart.getSize(); j++) {
				writer.write(
						String.valueOf(book_list.indexOf(cart.getBook(j))));
				writer.newLine();
			}

			writer.write(String.valueOf(wishlist.getSize()));
			writer.newLine();
			for (int j = 0; j < wishlist.getSize(); j++) {
				writer.write(
						String.valueOf(book_list.indexOf(wishlist.getBook(j))));
				writer.newLine();
			}

			writer.write(String.valueOf(order_stack.size));
			writer.newLine();

			// Iterate through
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

	public void load(Instance i) {
		// File readers
		FileReader file_reader;
		BufferedReader reader;
		cart = new ShoppingCart();
		wishlist = new Wishlist();

		String name = this.email + ".dat";
		ArrayList<Book> book_list = i.list;
		order_stack = new OrderStack();

		try {
			// Initialize file readers for customer file
			file_reader = new FileReader(name);
			reader = new BufferedReader(file_reader);

			int cart_items = Integer.parseInt(reader.readLine());

			for (int j = 0; j < cart_items; j++) {
				int index = Integer.parseInt(reader.readLine());
				if (index < book_list.size()
						&& book_list.get(index).getQuantity() > 0)
					cart.add(book_list.get(index));
			}

			int wishlist_items = Integer.parseInt(reader.readLine());
			for (int j = 0; j < wishlist_items; j++) {
				int index = Integer.parseInt(reader.readLine());
				if (index < book_list.size()
						&& book_list.get(index).getQuantity() > 0)
					wishlist.add(book_list.get(index));
			}
			int orders = Integer.parseInt(reader.readLine());

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
				String address = reader.readLine();
				String city = reader.readLine();
				String province = reader.readLine();
				String postal = reader.readLine();
				String country = reader.readLine();
				String phone = reader.readLine();

				Order o = new Order(order_cart, tax, date, fullname, address,
						city, province, postal, country, phone);
				this.order_stack.push(o);
			}

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
			// System.out.println("error");
			// cart = new ShoppingCart();
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
