package org.expecjions;

import org.expecjions.functions.ToMatch;
import org.expecjions.implementations.BooleanMatcher;
import org.expecjions.implementations.StringMatcher;
import org.expecjions.matcher.Matcher;
import org.expecjions.matcher.MatcherResult;

import static org.expecjions.matcher.Indents.indentation;
import static org.expecjions.matcher.MatcherCreator.matcherFor;

public class Expecjions {
	public static void expect(Boolean value, ToMatch<BooleanMatcher> toMatch) {
		expect(value, BooleanMatcher.class, toMatch);
	}

	public static void expect(String value, ToMatch<StringMatcher> toMatch) {
		expect(value, StringMatcher.class, toMatch);
	}

	public static <V, M extends Matcher<V>> void expect(V value, Class<M> matcherType, ToMatch<M> toMatch) {
		Matcher.Context<V> context = new Matcher.Context<>(value);
		M matcher = matcherFor(matcherType, context);
		MatcherResult result = context.unwrap(toMatch.match(matcher));

		if(result instanceof MatcherResult.Mismatch) {
			String description = result.describe();
			throw new AssertionError(describeValue(result, description, value)) {
				@Override
				public String toString() {
					return getMessage();
				}
			};
		}
	}

	private static <V> String describeValue(MatcherResult matcherResult, String description, V value) {
		String valueType = value.getClass().getSimpleName();
		String valueName = matcherResult.getValueName(String.valueOf(value))
				.replaceAll("\r", "\\\\r")
				.replaceAll("\n", "\\\\n");
		String expectedTo = "expected \"" + valueName + "\" [" + valueType + "] to ";
		return expectedTo + indent(description, expectedTo.length());
	}

	private static String indent(String description, int length) {
		return description.replaceAll("\r?\n", "\n"+indentation(length));
	}
}

