package org.magic7.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class SecurityUtil {
	public static String md5(String text, String charset) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}
		try {
			msgDigest.update(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("System doesn't support your  EncodingException.");
		}
		byte[] bytes = msgDigest.digest();
		String md5Str = new String(encodeHex(bytes));
		return md5Str;
	}
	public static String md5(String text) {
		return md5(text, "utf-8");
	}

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}

	private static Random rand = new Random();
	private static Integer[] numbers = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	public static String generateRandomNum(long length) {
		StringBuilder buffer = new StringBuilder();
		int temp = 0;
		for (int i = 0; i < length;) {
			temp = numbers[rand.nextInt(9)];
			if (i == 0 && temp == 0) {
				continue;
			}
			buffer.append(temp);
			i++;
		}
		return buffer.toString();
	}
	public static String generateUUID() {
		UUID u = UUID.randomUUID();
		return u.toString().replaceAll("\\-", "")+md5(System.currentTimeMillis()+"","utf-8");
	}
	public static void main(String[] args) {
		System.out.println(generateUUID());
	}
}
