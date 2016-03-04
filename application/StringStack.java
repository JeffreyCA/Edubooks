package application;

import java.util.ArrayList;

/**
 * Stack of strings
 */
public class StringStack {
	protected StringNode top;
	protected int size;

	// Default Constructor
	public StringStack() {
		top = null;
		size = 0;
	}

	/**
	 * Check if the stack contains a given string
	 * @param s String to be checked
	 * @return true, if the stack contains the string, otherwise false
	 */
	public boolean contains(String s) {
		StringNode node = top;

		// Search node-by-node
		while (node != null) {
			if (node.getValue().equals(s)) {
				return true;
			}
			node = node.getLink();
		}
		return false;
	}

	/**
	 * Check if stack is empty
	 * @return true, if empty, otherwise false
	 */
	public boolean isEmpty() {
		return top == null;
	}

	/**
	 * Get the top string in the stack
	 * @return String at the top of the stack
	 */
	public String peek() {
		if (top == null) {
			throw new RuntimeException("Empty stack");
		}
		else {
			String data = top.getValue();
			return data;
		}
	}

	/**
	 * Remove the String at the top of the stack
	 * @return the String at the top
	 */
	public String pop() {
		// Stack is not empty
		if (top != null) {
			String data = top.getValue();
			top = top.getLink();
			size--;
			return data;
		}
		// Empty stack
		else {
			return null;
		}
	}

	/**
	 * Add String to top of stack
	 * @param string String to be added
	 */
	public void push(String string) {
		StringNode temp = new StringNode(string, top);
		top = temp;
		size++;
	}

	/**
	 * Sort Strings in stack in alphabetical order
	 */
	public void sort() {
		StringStack temp = new StringStack();

		if (isEmpty())
			return;

		while (!isEmpty()) {
			String s = pop();
			while (!temp.isEmpty() && ((temp.peek().compareTo(s) < 0))) {
				push(temp.pop());
			}
			temp.push(s);
		}

		this.top = temp.top;
	}

	/**
	 * Convert stack to an ArrayList
	 * @return ArrayList of Strings
	 */
	public ArrayList<String> toArrayList() {
		StringNode node = top;
		ArrayList<String> list = new ArrayList<String>();

		while (node != null) {
			list.add(node.getValue());
			node = node.getLink();
		}
		return list;
	}
}
