package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Stack of customer Orders
 */
public class OrderStack {
	protected OrderNode top;
	protected int size;

	/**
	 * Default constructor
	 */
	public OrderStack() {
		top = null;
		size = 0;
	}

	/**
	 * Remove Order at bottom of the stack
	 * @param order Order at the bottom of the stack
	 */
	private void bottomPop(Order order) {
		if (isEmpty())
			push(order);
		else {
			Order a = top.getValue();
			pop();
			bottomPop(order);
			push(a);
		}
	}

	/**
	 * Check if stack is empty
	 * @return true, if empty, otherwise false
	 */
	public boolean isEmpty() {
		return top == null;
	}

	/**
	 * Merge elements of another stack into the current stack
	 * @param stack Stack to be merged
	 */
	public void merge(OrderStack stack) {
		// Push out all elements of the stack and push them into the current
		// stack
		while (!stack.isEmpty()) {
			push(stack.pop());
		}
	}

	/**
	 * Get newest order in stack
	 * @return Order at the top of the stack (newest order)
	 */
	public Order peek() {
		if (top == null) {
			throw new RuntimeException("Empty stack");
		}
		else {
			Order data = top.getValue();
			return data;
		}
	}

	/**
	 * Remove top Order
	 * @return the top Order
	 */
	public Order pop() {
		if (top != null) {
			Order data = top.getValue();
			top = top.getLink();
			size--;
			return data;
		}
		else {
			return null;
		}
	}

	/**
	 * Add Order to top of stack
	 * @param order Order to be added
	 */
	public void push(Order order) {
		OrderNode temp = new OrderNode(order, top);
		top = temp;
		size++;
	}

	/**
	 * Reverses stack contents
	 */
	public void reverse() {
		if (!isEmpty()) {
			Order order = top.getValue();
			pop();
			reverse();
			bottomPop(order);
		}
	}

	/**
	 * Sort Orders by date, from most recent to oldest
	 */
	public void sort() {
		// Create a temporary stack to sort contents
		OrderStack temp = new OrderStack();

		if (isEmpty())
			return;
		while (!isEmpty()) {
			Order order = pop();
			while (!temp.isEmpty()
					&& (temp.peek().getDate().isAfter(order.getDate()))) {
				push(temp.pop());
			}
			temp.push(order);
		}

		this.top = temp.top;
	}

	/**
	 * Allows for iteration through the stack
	 * @return Iterator
	 */
	public Iterator<Order> iterator() {
		Iterator<Order> it = new Iterator<Order>() {
			OrderNode node = top;
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size && node.getValue() != null;
			}

			@Override
			public Order next() {

				if (currentIndex != 0) {
					currentIndex++;
					node = node.getLink();
				}

				currentIndex++;

				if (node == null) {
					throw new NoSuchElementException();
				}
				return node.getValue();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
		return it;
	}

	/**
	 * Convert stack to an ArrayList
	 * @return ArrayList of Orders
	 */
	public ArrayList<Order> toArrayList() {
		OrderNode node = top;
		ArrayList<Order> list = new ArrayList<Order>();

		while (node != null) {
			list.add(node.getValue());
			node = node.getLink();
		}
		return list;
	}
}