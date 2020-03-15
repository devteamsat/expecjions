package org.expecjions.implementations;

import org.expecjions.matcher.CombinableMatcher;
import org.expecjions.matcher.MatcherResult;

public interface BooleanMatcher extends CombinableMatcher<Boolean, BooleanMatcher> {
	default MatcherResult beTrue() {
		if(getContext().getValue()) {
			return MatcherResult.match();
		}
		return MatcherResult.mismatch();
	}

	default MatcherResult beFalse() {
		if(getContext().getValue()) {
			return MatcherResult.mismatch();
		}
		return MatcherResult.match();
	}
}
