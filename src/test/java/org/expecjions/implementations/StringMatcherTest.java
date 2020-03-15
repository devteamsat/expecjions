package org.expecjions.implementations;

import org.expecjions.matcher.Matcher;
import org.expecjions.matcher.MatcherResult;
import org.junit.jupiter.api.Test;

import static org.expecjions.Expecjions.expect;
import static org.expecjions.matcher.MatcherCreator.matcherFor;

public class StringMatcherTest {
	@Test public void stringEqualsMatcherIsMatchWhenBothStringsAreEqual() {
		MatcherResult matcherResult = matcherFor(StringMatcher.class, new Matcher.Context<>("foo")).equal("foo");

		expect(matcherResult, MatchMatcher.class, to -> to.beMatch());
	}

	@Test public void stringEqualsMatcherIsMismatchWhenBothStringsAreNotEqual() {
		MatcherResult matcherResult = matcherFor(StringMatcher.class, new Matcher.Context<>("foo")).equal("bar");

		expect(matcherResult, MatchMatcher.class, to -> to.beMismatch());
	}

	@Test void stringStartsWithMatcherIsMismatchWhenStringDoesNotStartWithSubstring() {
		MatcherResult matcherResult = matcherFor(StringMatcher.class, new Matcher.Context<>("foo")).startWith("ba");

		expect(matcherResult, MatchMatcher.class, to -> to.beMismatch());
	}

	@Test void stringStartsWithMatcherIsMatchWhenStringDoesStartWithSubstring() {
		MatcherResult matcherResult = matcherFor(StringMatcher.class, new Matcher.Context<>("bar")).startWith("ba");

		expect(matcherResult, MatchMatcher.class, to -> to.beMatch());
	}

	@Test void stringStartWithMatcherDescriptionFitsError() {
		Matcher.Context<String> context = new Matcher.Context<>("foo");
		MatcherResult matcherResult = context.unwrap(matcherFor(StringMatcher.class, context).startWith("bar"));

		expect(matcherResult.describe(), to -> to.startWith("start with \"bar\""));
	}
}
