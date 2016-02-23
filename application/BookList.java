package application;

import java.util.ArrayList;

public class BookList {

	// Needs to be accessible by ShoppingCart class
	protected BookNode head;
	protected int size;

	// Default constructor
	public BookList() {
		head = null;
		size = 0;
	}

	/**
	 * Add book to the linked list
	 * @param item Book to be added
	 */
	public void add(Book item) {
		BookNode node = head;
		BookNode newNode = new BookNode(item);

		// Empty list
		if (node == null) {
			head = newNode;
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

	/**
	 * Delete a book in the list
	 * @param b Book to be deleted
	 * @return True if book is deleted, otherwise false if book is not in
	 *         the list
	 */
	public boolean delete(Book b) {
		BookNode current = head;
		BookNode previous = null;

		// Iterate through list
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

		// Book not found
		return false;
	}

	/**
	 * Display the contents of the linked list
	 */
	public void display() {
		BookNode node = head;

		if (node != null) {
			while (node != null) {
				System.out.println(node.getValue());
				node = node.getLink();
			}
		}
	}

	/**
	 * Retrieve book at position
	 * @param position position number
	 * @return Book object
	 */
	public Book getBook(int position) {
		return traverse(position).getValue();
	}

	/**
	 * Retrieve Book node at position
	 * @param position position number
	 * @return BookNode node
	 */
	public BookNode getNode(int position) {
		return traverse(position);
	}

	/**
	 * Given a book, return its encompassing node
	 * @param b Book considered
	 * @return BookNode which Book is part of
	 */
	public BookNode getNode(Book b) {
		for (int i = 0; i < size; i++) {
			if (traverse(i).getValue().equals(b))
				return traverse(i);
		}
		return null;
	}

	/**
	 * Return size of list
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Return quantity of the book in the shopping cart or wishlist
	 *
	 * Quantity here does not refer to the quantity in stock
	 * @return Quantity
	 */
	public int getItemQuantity() {
		BookNode node = head;
		int counter = 0;

		while (node != null) {
			counter += node.getQuantity();
			node = node.getLink();
		}
		return counter;
	}

	/**
	 * Reach and return ith node in the list
	 * @param i position
	 * @return BookNode of the book
	 */
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

	/**
	 * Check if list is empty
	 * @return
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Make an ArrayList of books out of the list
	 *
	 * ArrayList must be used in the TableView and ListViews
	 * @return ArrayList of books
	 */
	public ArrayList<Book> toArrayList() {
		BookNode node = head;
		ArrayList<Book> list = new ArrayList<Book>();

		while (node != null) {
			list.add(node.getValue());
			node = node.getLink();
		}
		return list;
	}
}
