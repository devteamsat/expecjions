package org.expecjions.documentation;

import org.expecjions.Expecjions;
import org.expecjions.functions.ToMatch;
import org.expecjions.matcher.CombinableMatcher;
import org.expecjions.matcher.MatcherResult;
import org.junit.jupiter.api.Test;

public class TestsForDocumentation {
	@Test void testSomeBoolean() {
		boolean actualValue = doActionInSystemUnderTest();

		Expecjions.expect(actualValue, to->to.beTrue());
		Expecjions.expect(actualValue, to->to.not().beFalse());
	}

	@Test void testStartWith() {
		//expect("foobar", to->to.startWith("bar"));
		//expect("foobar", to->to.not().startWith("foo"));
	}

	@Test void personAgeTest() {
		Person person = new Person("Jane", "Doe", 33);

		expect(person, to->to.haveAge(34));
	}
	private boolean doActionInSystemUnderTest() {
		return true;
	}

	public static void expect(Person person, ToMatch<PersonMatcher> toMatch) {
		Expecjions.expect(person, PersonMatcher.class, toMatch);
	}

	public interface PersonMatcher extends CombinableMatcher<Person, PersonMatcher> {
		default MatcherResult haveAge(int age) {
			return getContext().getValue().getAgeInYears()==age? MatcherResult.match() : MatcherResult.mismatch();
		}
	}

	private class Person {
		private final String firstName;
		private final String lastName;
		private final int ageInYears;

		public Person(String firstName, String lastName, int ageInYears) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.ageInYears = ageInYears;
		}

		public int getAgeInYears() {
			return ageInYears;
		}

		@Override
		public String toString() {
			return "Person{" +
					"firstName='" + firstName + '\'' +
					", lastName='" + lastName + '\'' +
					", ageInYears=" + ageInYears +
					'}';
		}
	}
}
