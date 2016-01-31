package application;

public class BookList {

	protected BookNode head;
	protected int size;

	// Constructor
	public BookList() {
		head = null;
		size = 0;
	}

	public BookNode getHead() {
		return head;
	}

	public void add(Book item) {
		BookNode node = head;
		BookNode newNode = new BookNode(item, null);

		// Empty list
		if (node == null) {
			head = new BookNode(item);
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

	public BookNode getNode(int position) {
		return traverse(position);
	}

	public BookNode getNode(Book b) {
		for (int i = 0; i < size; i++) {
			if (traverse(i).getValue().equals(b))
				return traverse(i);
		}
		return null;
	}

	// Reach and return ith node in the list
	public BookNode traverse(int i) {
		BookNode n = head;

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
		BookNode node = traverse(position);

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

	public boolean delete(Book b) {
		BookNode current = head;
		BookNode previous = null;

		while (current != null) {
			if (current.getValue().equals(b)) {
				if (current == head) {
					head = head.getLink();
				}
				else {
					previous.setLink(current.getLink());
				}
				size--;
				return true;
			}
			else {
				previous = current;
			}
			current = current.getLink();

		}
		return false;
	}

	// Display the contents of the linked list
	public void display() {
		BookNode node = head;

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
	private void reverse(BookNode node, int i) {
		if (node.getLink() != null) {
			reverse(node.getLink(), i + 1);
		}
		System.out.println(node.getValue());
	}

	// Insert a new node with item as data at ith place in the list
	public boolean insert(Book item, int position) {
		BookNode previous;
		BookNode node = new BookNode(item);

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
		BookNode node = traverse(position);
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

	public int getItemQuantity() {
		BookNode node = head;
		int counter = 0;

		while (node != null) {
			counter += node.getQuantity();
			node = node.getLink();
		}
		return counter;
	}

	public boolean isEmpty() {
		return head == null;
	}
}
