package application;

public class Account {

	private String email;
	private String password;

	public Account(String email, String password) {
		this.setEmail(email);
		this.setPassword(password);
	}

	public static String rot13(String s) {
		final int ROT = 13;
		String rot_string = "";

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'm')
				c += ROT;
			else if (c >= 'A' && c <= 'M')
				c += ROT;
			else if (c >= 'n' && c <= 'z')
				c -= ROT;
			else if (c >= 'N' && c <= 'Z')
				c -= ROT;
			rot_string += c;
		}
		return rot_string;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
