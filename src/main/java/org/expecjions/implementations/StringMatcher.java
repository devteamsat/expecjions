package org.expecjions.implementations;

import org.expecjions.matcher.CombinableMatcher;
import org.expecjions.matcher.MatcherResult;

public interface StringMatcher extends CombinableMatcher<String, StringMatcher> {
	default MatcherResult equal(String expected) {
		return expected.equals(getContext().getValue())?
				MatcherResult.match() :
				MatcherResult.mismatch();
	}

	default MatcherResult startWith(String with) {
		return getContext().getValue().startsWith(with)?
				MatcherResult.match() :
				MatcherResult.mismatch();
	}
}
