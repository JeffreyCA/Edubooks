package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringStack {
	protected StringNode top;
	protected int size;

	public StringStack() {
		top = null;
		size = 0;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public void sort() {
		StringStack temp = new StringStack();

		if (isEmpty())
			return;

		while (!isEmpty()) {
			String o = pop();
			while (!temp.isEmpty() && ((temp.peek().compareTo(o) < 0))) {
				push(temp.pop());
			}
			temp.push(o);
		}

		this.top = temp.top;
	}

	public String pop() {
		if (top != null) {
			String data = top.getValue();
			top = top.getLink();
			size--;
			return data;
		}
		else {
			return null;
		}
	}

	public void push(String item) {
		StringNode temp = new StringNode(item, top);
		top = temp;
		size++;
	}

	public String peek() {
		if (top == null) {
			throw new RuntimeException("Empty stack");
		}
		else {
			String data = top.getValue();
			return data;
		}
	}

	public void merge(StringStack stack) {
		while (!stack.isEmpty()) {
			push(stack.pop());
		}
	}

	public boolean contains(String s) {
		StringNode node = top;

		while (node != null) {
			if (node.getValue().equals(s)) {
				return true;
			}
			node = node.getLink();
		}
		return false;
	}

	public Iterator<String> iterator() {
		Iterator<String> it = new Iterator<String>() {
			StringNode node = top;
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size && node.getValue() != null;
			}

			@Override
			public String next() {

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

	private void bottomPop(String o) {
		if (isEmpty())
			push(o);
		else {
			String a = top.getValue();
			pop();
			bottomPop(o);
			push(a);
		}
	}

	// Recursion
	public void reverse() {
		if (!isEmpty()) {
			String o = top.getValue();
			pop();
			reverse();
			bottomPop(o);
		}
	}

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
