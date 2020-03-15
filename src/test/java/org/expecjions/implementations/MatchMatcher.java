package org.expecjions.implementations;

import org.expecjions.matcher.CombinableMatcher;
import org.expecjions.matcher.MatcherResult;

public interface MatchMatcher extends CombinableMatcher<MatcherResult, MatchMatcher> {
	default MatcherResult beMatch() {
		return getContext().getValue() instanceof MatcherResult.Match? MatcherResult.match() : MatcherResult.mismatch("result");
	}

	default MatcherResult beMismatch() {
		return getContext().getValue() instanceof MatcherResult.Mismatch? MatcherResult.match() : MatcherResult.mismatch("result");
	}
}
