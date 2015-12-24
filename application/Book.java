package application;

public class Book {

	private String author;
	private Categories category;
	private String title;
	private double price;
	private int quantity;
	private int year;

	enum Categories {
		ENGLISH, MATH, SCIENCE, HISTORY, OTHER, NONE
	}

	// Default constructor
	public Book() {
		author = "";
		title = "";
		setCategory(Categories.NONE);
		price = 0;
		quantity = 0;
		year = 0;
	}

	public Book(String author, String title, Categories category, double price,
			int quantity, int year) {
		this.author = author;
		this.title = title;
		this.setCategory(category);
		this.price = price;
		this.quantity = quantity;
		this.year = year;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Categories getCategory() {
		return category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}
}
