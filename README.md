# expecjions: Expectation Library for Java

_expecjions_ allows you to write expressive assertions in
your unit tests. It is extensible and creates easy-to-read
failure messages.

To use _expecjions_ in your project, you musst pass the
```-parameters``` command-line argument to the java compiler
when compiling your tests.

**Warning:** _expecjions_ is, so far, only a proof-of-concept.
Most of the code is untested and everything is incomplete.
Want to follow the progress of _expecjions_? Then follow me
on Twitter: [@dtanzer](https://twitter.com/dtanzer).

## Quick Start

    @Test void testSomeBoolean() {
        boolean actualValue = doActionInSystemUnderTest();

        expect(actualValue, to->to.beTrue());
        expect(actualValue, to->to.not().beFalse());
    }

When an expectation is not satisfied, _expecjions_ figures
out what went wrong and creates an expressive error message.
A test with this expectation:

    expect(false, to->to.not().beFalse());

will fail with the following message:

    expected "false" [Boolean] to not be false
                                      +------+
                                      match
                                  +----------+
                                  mismatch

## Why expecjions

The assertions that come with JUnit are not type-safe and
not extensible. They often do not allow you to write
expressive tests. But they are only a last-resort anyway -
nowadays, many developers use libraries like _hamcrest_ or
_assertj_.

_hamcrest_ is extensible and it allows us to combine matchers.
But it provides very bad IDE support - Figuring out which
matchers are available at a given moment can become a real
pain. It is also not as type-safe as it could be, and writing
your own matchers can become pretty cumbersome.

_assertj_, on the other hand, has a much nicer API and good
IDE support. But it is not extensible and you cannot easily
combine assertions.

_expecjions_ tries to fix all these problems.

## expect

The most general way to write an expectation is:

    expect(value, [MatcherType].class, to -> to.[functionCallOnMatcherType]());

So, the expectation from the example above could be written
as:

    expect(actualValue, BooleanMatcher.class, to -> to.beTrue());

but the library provides some shortcuts for known types, like:

    public static void expect(Boolean value, ToMatch<BooleanMatcher> toMatch) {
        expect(value, BooleanMatcher.class, toMatch);
    }

This means that in the test code, you pass the actual value,
an optional matcher type and a lambda expression that calls
the actual mather. Here's another example:

    expect("foobar", to->to.startWith("bar"));

## Matchers and Combining Matchers

The library comes with predefined matchers for known types.
You can also write your own matchers - this allows you to
write even more expressive tests (see below).

You can also combine matchers (when they extend
```CombinableMatcher```):

    expect("foobar", to->to.not().startWith("foo"));

_expecjions_ will gather the result (```match``` or
```mismatch```) from the matcher itself (```startWith```)
and from all combinators (```not```) and print them in a
human-readable way:

    expected "foobar" [String] to not start with "foo"
                                      +---------------+
                                      match
                                  +-------------------+
                                  mismatch

## Extending: Write Your Own Matcher

Suppose you want to test whether a person has a certain age:

    @Test void personAgeTest() {
        Person person = new Person("Jane", "Doe", 33);
        
        expect(person.getAgeInYears(), to->to.equal(33));
    }

This test is not very expressive. Can we do better?

By writing our own ```PersonMatcher```, we can re-write the
test into

    expect(person, PersonMatcher.class, to->to.haveAge(33));

This already looks better, but by defining our own
```expect``` - function, we can make it even nicer:

    public static void expect(Person person, ToMatch<PersonMatcher> toMatch) {
        Expecjions.expect(person, PersonMatcher.class, toMatch);
    }
    
    //...
    
    expect(person, to->to.haveAge(33));

If there is a mismatch, the failure message will look like:

    expected "Person{firstName='Jane', lastName='Doe', ageInYears=33}" [Person] to have age "34"
                                                                                   +------------+
                                                                                   mismatch

And how do we actually write the matcher? It is almost a one-liner:

    public interface PersonMatcher extends CombinableMatcher<Person, PersonMatcher> {
        default MatcherResult haveAge(int age) {
            return getContext().getValue().getAgeInYears()==age? MatcherResult.match() : MatcherResult.mismatch();
        }
    }

The matcher must be an interface and the actuall implementations
of the matching functions must be default implementations.
This allows _expecjions_ to intercept the calls and do it's
magic.

In the most simple case, a matcher only checks the given
value that was provided by ```getContext()``` and then
returns either ```match``` or ```mismatch```. _expecjions_
will then figure out how to print the error message from
the method name and the parameter names and values.

(**Note:** later, there will also be ways to customize this
error message.)
