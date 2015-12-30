package application;

public class BookList {

	private Node head;
	private int size;

	// Constructor
	public BookList() {
		head = null;
		size = 0;
	}

	public void add(Book item) {
		Node node = head;
		Node newNode = new Node(item, null);

		// Empty list
		if (node == null) {
			head = new Node(item);
		}
		// Not empty list
		if (node != null) {
			while (node.getLink() != null) {
				node = node.getLink();
			}
			node.setLink(newNode);
		}
		size++;
	}

	public Book getBook(int position) {
		return traverse(position).getValue();
	}

	// Reach and return ith node in the list
	public Node traverse(int i) {
		Node n = head;

		if (i < 0)
			return null;

		for (int j = 0; j < i; j++) {
			if (n == null)
				return null;
			n = n.getLink();
		}
		return n;
	}

	// Replace contents of a node
	public boolean replace(Book item, int position) {
		Node node = traverse(position);

		// Node does not exist
		if (node == null) {
			return false;
		}
		// Value of the head is replaced
		else if (position == 0) {
			head.setValue(item);
			return true;
		}
		// Value of a node is replaced
		else {
			node.setValue(item);
			return true;
		}
	}

	// Delete the node at a certain position
	public boolean delete(int position) {
		Node node = traverse(position);
		Node previous = traverse(position - 1);

		// Node does not exist at that position
		if (node == null) {
			return false;
		}
		// The head is removed, and the head points to another node
		if (position == 0 && node.getLink() != null) {
			head = node.getLink();
			size--;
			return true;
		}
		// The head does not point to another node
		else if (node.getLink() == null) {
			head = null;
			size = 0;
			return true;
		}
		// A node that is not the head is removed
		else if (node.getLink() != null) {
			previous.setLink(node.getLink());
			node = null;
			size--;
			return true;
		}
		else {
			return false;
		}
	}

	// Display the contents of the linked list
	public void display() {
		Node node = head;

		if (node == null) {
			System.out.println("null");
		}
		else {
			while (node != null) {
				System.out.println(node.getValue());
				node = node.getLink();
			}
		}
	}

	// Display contents of the linked list in reverse order
	public void displayReverse() {
		if (head == null) {
			System.out.println("null");
		}
		else {
			reverse(head, 0);
		}
	}

	// Recursion
	private void reverse(Node node, int i) {
		if (node.getLink() != null) {
			reverse(node.getLink(), i + 1);
		}
		System.out.println(node.getValue());
	}

	// Insert a new node with item as data at ith place in the list
	public boolean insert(Book item, int position) {
		Node previous;
		Node node = new Node(item);

		// Insert new head
		if (position == 0) {
			if (!isEmpty())
				node.setLink(head);
			head = node;
		}
		else {
			previous = traverse(position - 1);
			if (previous == null)
				return false;
			node.setLink(previous.getLink());
			previous.setLink(node);
		}
		size++;
		return true;
	}

	// Return Book at a position
	public Book lookUp(int position) {
		Node node = traverse(position);
		if (node == null) {
			return null;
		}
		else {
			return node.getValue();
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
