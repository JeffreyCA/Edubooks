package application;

import java.time.LocalDateTime;

/**
 * Order class stores information on a customer order, which contains the
 * orders bought and purchase information
 */
public class Order {
	// Private fields
	private double tax;
	private String email;
	private Address address;
	private LocalDateTime date;
	private ShoppingCart items;

	// Default constructor
	public Order(ShoppingCart items, double tax, LocalDateTime date,
			Address address, String email) {
		this.items = items;
		this.tax = tax;
		this.date = date;
		this.address = address;
		this.email = email;
	}

	/**
	 * Display order info, one piece of information per line
	 */
	@Override
	public String toString() {
		String s = "";
		s += items.size + "\n";
		for (int i = 0; i < items.size; i++) {
			s += items.getBook(i) + "\n";
			s += items.getNode(i).getQuantity() + "\n";
		}
		s += tax + "\n";
		s += date + "\n";
		s += address;
		return s;
	}

	// Getters - No setters because orders cannot be modified
	public Address getAddress() {
		return address;
	}

	public String getName() {
		return address.getName();
	}

	public String getStreet() {
		return address.getStreet();
	}

	public String getCity() {
		return address.getCity();
	}

	public String getProvince() {
		return address.getProvince();
	}

	public String getPostal() {
		return address.getPostal();
	}

	public String getCountry() {
		return address.getCity();
	}

	public String getPhone() {
		return address.getPhone();
	}

	public LocalDateTime getDate() {
		return date;
	}

	public ShoppingCart getItems() {
		return items;
	}

	public double getSubtotal() {
		return items.getTotal();
	}

	public double getTax() {
		return tax;
	}

	public double getTotal() {
		return tax + items.getTotal();
	}

	public String getEmail() {
		return email;
	}
}
