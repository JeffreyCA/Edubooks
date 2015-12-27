package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class StoreFront implements Initializable {

	private Account account;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
