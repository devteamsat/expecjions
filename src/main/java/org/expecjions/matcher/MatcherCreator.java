package org.expecjions.matcher;

import java.lang.reflect.Proxy;
import java.util.function.Function;

public class MatcherCreator {
	public static <V, M extends Matcher<V>> M matcherFor(Class<M> matcherType, Matcher.Context<V> context) {
		M matcher = (M) Proxy.newProxyInstance(matcherType.getClassLoader(), new Class[]{matcherType},
				new MatcherInvocationHandler(context.getValue(), matcherType));
		matcher.setContext(context);
		return matcher;
	}

	public static <M extends Matcher<?>> M combine(M baseMatcher, Function<MatcherResult, MatcherResult> combination) {
		baseMatcher.getContext().addCombination(combination);
		return baseMatcher;
	}
}
