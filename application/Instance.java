package application;

import java.util.ArrayList;

public class Instance {
	protected Account account;
	protected ArrayList<Book> list;

	public Instance(ArrayList<Book> list) {
		this.list = list;
		account = null;
	}

	public void loadData() {
		account.load(this);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ArrayList<Book> getList() {
		return list;
	}

	public void setList(ArrayList<Book> list) {
		this.list = list;
	}
}
