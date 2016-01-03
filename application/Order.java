package application;

import java.time.LocalDateTime;

public class Order {

	private ShoppingCart items;
	private double tax;
	private LocalDateTime date;

	private String name;
	private String address;
	private String city;
	private String province;
	private String postal;
	private String country;
	private String phone;

	public Order(ShoppingCart items, double tax, LocalDateTime date,
			String name, String address, String city, String province,
			String postal, String country, String phone) {
		this.items = items;
		this.tax = tax;
		this.date = date;
		this.name = name;
		this.address = address;
		this.city = city;
		this.province = province;
		this.postal = postal;
		this.country = country;
		this.phone = phone;
	}

	@Override
	public String toString() {
		String s = "";
		s += items.size + "\n";

		for (int i = 0; i < items.size; i++) {
			s += items.getBook(i) + "\n";
		}
		s += tax + "\n";
		s += LocalDateTime.now() + "\n";
		s += name + "\n" + address + "\n" + city + "\n" + province + "\n"
				+ postal + "\n" + country + "\n" + phone + "\n";
		return s;
	}
}
