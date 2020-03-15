package org.expecjions.implementations;

import org.expecjions.matcher.Matcher;
import org.expecjions.matcher.MatcherResult;
import org.junit.jupiter.api.Test;

import static org.expecjions.Expecjions.expect;
import static org.expecjions.matcher.MatcherCreator.matcherFor;

public class BooleanMatcherTest {
	@Test
	public void booleanTrueMatcherReturnsMatchWhenExpectedIsTrue() {
		MatcherResult matcherResult = matcherFor(BooleanMatcher.class, new Matcher.Context<>(true)).beTrue();

		expect(matcherResult, MatchMatcher.class, to -> to.beMatch());
	}

	@Test
	public void booleanTrueMatcherReturnsMismatchWhenExpectedIsFalse() {
		MatcherResult matcherResult = matcherFor(BooleanMatcher.class, new Matcher.Context<>(false)).beTrue();

		expect(matcherResult, MatchMatcher.class, to -> to.beMismatch());
	}

	@Test
	public void booleanFalseMatcherReturnsMismatchWhenExpectedIsTrue() {
		MatcherResult matcherResult = matcherFor(BooleanMatcher.class, new Matcher.Context<>(true)).beFalse();

		expect(matcherResult, MatchMatcher.class, to -> to.beMismatch());
	}

	@Test
	public void booleanFalseMatcherReturnsMatchWhenExpectedIsFalse() {
		MatcherResult matcherResult = matcherFor(BooleanMatcher.class, new Matcher.Context<>(false)).beFalse();

		expect(matcherResult, MatchMatcher.class, to -> to.beMatch());
	}

}
