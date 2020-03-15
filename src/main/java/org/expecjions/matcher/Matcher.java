package org.expecjions.matcher;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Matcher<V> {
	Matcher.Context<V> getContext();
	void setContext(Matcher.Context<V> context);

	class Context<CV> {
		private final CV value;
		private List<Combination> combinations = new LinkedList<>();

		public Context(CV value) {
			this.value = value;
		}

		public MatcherResult unwrap(MatcherResult result) {
			BiFunction<MatcherResult, Combination, MatcherResult> reducer = (r, c) -> {
				MatcherResult newResult = c.combiner.apply(r);
				newResult.setMethodName(c.methodName);
				newResult.setParameters(c.params);
				newResult.setPreviousResult(r);
				return newResult;
			};
			return combinations.stream().reduce(result, reducer, (r1, r2) -> r2);
		}

		public CV getValue() {
			return value;
		}

		public void pushMethod(String methodName, Parameter[] parameters, Object[] args) {
			Combination combination = new Combination();
			combination.methodName = methodName;

			List<Param> params = new LinkedList<>();
			for(int i=0; i<parameters.length; i++) {
				String name = parameters[i].isNamePresent()? parameters[i].getName() : "arg"+i;
				params.add(new Param(name, args[i]));
			}
			combination.params = params;

			combinations.add(0, combination);
		}

		public void addCombination(Function<MatcherResult, MatcherResult> combiner) {
			combinations.get(0).combiner = combiner;
		}

		private static class Combination {
			public List<Param> params;
			private Function<MatcherResult, MatcherResult> combiner = r->r;
			private String methodName;
		}

	}

	class Param {
		private final String name;
		private final Object value;

		public Param(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}
	}
}
