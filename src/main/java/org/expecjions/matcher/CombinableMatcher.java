package org.expecjions.matcher;

public interface CombinableMatcher<V, M extends Matcher<V>> extends Matcher<V> {
	default M not() {
		return MatcherCreator.combine((M) this,
				result -> (result instanceof MatcherResult.Match)? MatcherResult.mismatch() : MatcherResult.match());
	}
}
