package application;

public class Account {

	public static void main(String[] args) {
		String s = "password";
		System.out.println(Account.rot13(s));
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
}
