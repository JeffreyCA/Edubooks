package application;

import java.time.LocalDateTime;

public class Order {

	private ShoppingCart items;
	private double tax;
	private LocalDateTime date;

	private String name;

	public ShoppingCart getItems() {
		return items;
	}

	public void setItems(ShoppingCart items) {
		this.items = items;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getSubtotal() {
		return items.getTotal();
	}

	public double getTotal() {
		return tax + items.getTotal();
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

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
		System.out.println("items: " + items.size);
		for (int i = 0; i < items.size; i++) {
			s += items.getBook(i) + "\n";
			s += items.getNode(i).getQuantity() + "\n";
		}
		s += tax + "\n";
		s += date + "\n";
		s += name + "\n" + address + "\n" + city + "\n" + province + "\n"
				+ postal + "\n" + country + "\n" + phone;
		return s;
	}
}
