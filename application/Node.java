package application;
public class Node {

	private Object value;
	private Node link;

	// Constructor
	public Node(Object value, Node link) {
		this.value = value;
		this.link = link;
	}

	public Node(Object value) {
		this.value = value;
		this.link = null;
	}

	// Getters and setters
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Node getLink() {
		return link;
	}

	public void setLink(Node link) {
		this.link = link;
	}
}
