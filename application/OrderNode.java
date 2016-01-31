package application;

public class OrderNode {

	private Order value;
	private OrderNode link;

	// Constructor
	public OrderNode(Order value, OrderNode link) {
		this.value = value;
		this.link = link;
	}

	public OrderNode(Order value) {
		this.value = value;
		this.link = null;
	}

	// Getters and setters
	public Order getValue() {
		return value;
	}

	public void setValue(Order value) {
		this.value = value;
	}

	public OrderNode getLink() {
		return link;
	}

	public void setLink(OrderNode link) {
		this.link = link;
	}
}
