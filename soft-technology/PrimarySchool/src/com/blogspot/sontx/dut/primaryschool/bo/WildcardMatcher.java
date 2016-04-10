package com.blogspot.sontx.dut.primaryschool.bo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WildcardMatcher {
	private static final Pattern regex = Pattern.compile("[^*?]+|(\\*)|(\\?)");
	private final Pattern escapeRegex;

	private String getEscapeExpression(String expression) {
		Matcher matcher = regex.matcher(expression);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			if (matcher.group(1) != null)
				matcher.appendReplacement(buffer, ".*");
			else if (matcher.group(2) != null)
				matcher.appendReplacement(buffer, ".");
			else
				matcher.appendReplacement(buffer, "\\\\Q" + matcher.group(0) + "\\\\E");
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public boolean matches(String st) {
		return escapeRegex.matcher(st).matches();
	}

	public WildcardMatcher(String expression) {
		String escapeExression = getEscapeExpression(expression);
		escapeRegex = Pattern.compile(escapeExression);
	}
}