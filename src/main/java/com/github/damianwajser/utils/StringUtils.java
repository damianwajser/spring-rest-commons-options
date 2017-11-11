package com.github.damianwajser.utils;

public final class StringUtils {

	private StringUtils() {
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
	}

	public static String deleteIfEnd(String text, String regex) {
		if (text.endsWith(regex)) {
			text = replaceLast(text, regex, "");
		}
		return text;
	}
}
