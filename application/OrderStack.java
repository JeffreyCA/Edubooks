package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderStack {
	protected OrderNode top;
	protected int size;

	public OrderStack() {
		top = null;
		size = 0;
	}

	public boolean isEmpty() {
		return top == null;
	}

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

	public void push(Order item) {
		OrderNode temp = new OrderNode(item, top);
		top = temp;
		size++;
	}

	public Order peek() {
		if (top == null) {
			throw new RuntimeException("Empty stack");
		}
		else {
			Order data = top.getValue();
			return data;
		}
	}

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

	private void bottomPop(Order o) {
		if (isEmpty())
			push(o);
		else {
			Order a = top.getValue();
			pop();
			bottomPop(o);
			push(a);
		}
	}

	// Recursion
	public void reverse() {
		if (!isEmpty()) {
			Order o = top.getValue();
			pop();
			reverse();
			bottomPop(o);
		}
	}

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