package application;

public class StringNode {
	private String value;
	private StringNode link;

	// Constructor
	public StringNode(String value, StringNode link) {
		this.value = value;
		this.link = link;
	}

	public StringNode(String value) {
		this.value = value;
		this.link = null;
	}

	// Getters and setters
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringNode getLink() {
		return link;
	}

	public void setLink(StringNode link) {
		this.link = link;
	}
}
