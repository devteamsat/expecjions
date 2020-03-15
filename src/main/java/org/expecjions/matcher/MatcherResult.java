package org.expecjions.matcher;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.expecjions.matcher.Indents.indentation;
import static org.expecjions.matcher.Indents.underline;

public abstract class MatcherResult {
	private final String actualValueName;
	protected String methodName;
	private MatcherResult previousResult;
	private List<Matcher.Param> params;

	public MatcherResult(String actualValueName) {
		this.actualValueName = actualValueName;
	}

	public static MatcherResult match() {
		return new Match(null);
	}

	public static MatcherResult mismatch() {
		return new Mismatch(null);
	}

	public static MatcherResult mismatch(String actualValueName) {
		return new Mismatch(actualValueName);
	}

	public String describe() {
		String previousDescription = "";
		if(previousResult != this) {
			previousDescription = " " + previousResult.describe();
		}
		int previousLongestLine = Stream.of(previousDescription.split("\r?\n"))
				.map(String::trim)
				.reduce(0, (r, line) -> Math.max(r, line.length()+1), (r1, r2) -> r2);
		previousDescription = indentPrevious(previousDescription, (methodName+" ").length());

		String readableName = methodName
				.replaceAll("(\\p{javaUpperCase}+)", " $1")
				.toLowerCase();

		String parameterDescriptions = describeParameters();

		return readableName+parameterDescriptions+previousDescription+'\n'+underline((readableName+parameterDescriptions).length()+previousLongestLine)+'\n' + type();
	}

	private String describeParameters() {
		if(params!=null && !params.isEmpty()) {
			return " "+params.stream().reduce("", (r, p)->{
				String separator = (r.length() > 0)? ", ": "";

				if(p.getName() == null) {
					return r+separator+"\""+p.getValue()+"\"";
				}

				return r+separator+p.getName()+" \""+p.getValue()+"\"";
			}, (r1, r2)->r2);
		}
		return "";
	}

	protected abstract String type();

	private String indentPrevious(String previousDescription, int length) {
		return previousDescription.replaceAll("\r?\n", "\n"+indentation(length));
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setPreviousResult(MatcherResult previousResult) {
		this.previousResult = previousResult;
	}

	public String getValueName(String defaultName) {
		String actualValueName = getActualValueName();
		if(actualValueName != null) {
			return actualValueName;
		}
		return defaultName;
	}

	private String getActualValueName() {
		if(this.actualValueName != null) {
			return this.actualValueName;
		}
		if(previousResult != this) {
			return previousResult.getActualValueName();
		}
		return null;
	}

	public void setParameters(List<Matcher.Param> params) {
		this.params = params;
	}

	public static class Match extends MatcherResult {
		private Match(String actualValueName) {
			super(actualValueName);
		}

		@Override
		protected String type() {
			return "match";
		}
	}

	public static class Mismatch extends MatcherResult {
		private Mismatch(String actualValueName) {
			super(actualValueName);
		}

		@Override
		protected String type() {
			return "mismatch";
		}
	}
}
