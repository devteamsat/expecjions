package org.expecjions.functions;

import org.expecjions.matcher.MatcherResult;

public interface ToMatch<M> {
	MatcherResult match(M m);
}
