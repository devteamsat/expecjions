package org.expecjions.matcher;

public class Indents {
	public static String underline(int length) {
		int dashes = Math.max(length, "mismatch".length()) - 2;
		return '+' + new String(new char[dashes]).replace('\0', '-')+'+';
	}

	public static String indentation(int length) {
		return new String(new char[length]).replace('\0', ' ');
	}
}
