package application;

/**
 * Node class which stores Strings
 */
public class StringNode {
	private String value;
	private StringNode link;

	// Constructor, with link
	public StringNode(String value, StringNode link) {
		this.value = value;
		this.link = link;
	}

	// Constructor, without link
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
