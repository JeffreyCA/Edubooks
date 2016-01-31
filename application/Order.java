package application;

import java.time.LocalDateTime;

public class Order {

	private ShoppingCart items;
	private double tax;
	private LocalDateTime date;
	private Address address;

	public Order(ShoppingCart items, double tax, LocalDateTime date,
			Address address) {
		this.items = items;
		this.tax = tax;
		this.date = date;
		this.address = address;
	}

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

	public void setTax(double tax) {
		this.tax = tax;
	}

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
}
