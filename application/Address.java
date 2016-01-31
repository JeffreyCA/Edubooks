package application;

public class Address {
	private String name;
	private String street;
	private String city;
	private String province;
	private String postal;
	private String country;
	private String phone;

	public Address(String name, String street, String city, String province,
			String postal, String country, String phone) {

		this.name = name;
		this.street = street;
		this.city = city;
		this.province = province;
		this.postal = postal;
		this.country = country;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return name + "\n" + street + "\n" + city + "\n" + province + "\n"
				+ postal + "\n" + country + "\n: " + phone;
	}

	public String formated() {
		return "Name: " + name + "\nAddress: " + street + "\nCity: " + city
				+ "\nProvince: " + province + "\nPostal Code: " + postal
				+ "\nCountry: " + country + "\nPhone Number: " + phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
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
}
