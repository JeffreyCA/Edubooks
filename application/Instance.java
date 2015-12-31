package application;

import java.util.ArrayList;

public class Instance {
	protected Account account;
	protected ArrayList<Book> list;
	protected CartList cart_list;

	public Instance(ArrayList<Book> list) {
		this.list = list;
		account = null;
	}

	public void loadData() {
		account.load(this);
	}

}
