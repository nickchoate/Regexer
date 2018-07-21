# Regexer

## What is Regexer?
Regexer is a builder style library to help build Regular Expressions.  
I've always had a love/hate relationship with Regular Expressions and wanted a library to help me build them easily that would also enable self documenting code. There's nothing worse than trying to debug something and coming across a huge regular expression that stops me in my tracks.

## Oh okay, how does it work?
Well, the Regexer library allows you to build a regular expression in code, which will then return either a fully escaped regular expression as a `String` or provide a `Pattern` or `Matcher` object back as needed.

Let's see it in action.

### Phone Number Example
```java
Regexer threeDigits = new Regexer();
threeDigits.digit().times(3);
Regexer fourDigits = new Regexer();
fourDigits.digit().times(4);
Regexer hyphen = new Regexer("-");
Regexer phone = new Regexer();
phone.add(threeDigits, hyphen, threeDigits, hyphen, fourDigits);
```

The above example matches a phone number that looks something like this '111-222-3333', which yields this regular expression: `\d{3}\Q-\E\d{3}\Q-\E\d{4}`.    

Okay, that's okay for a simple example, but what if someone separates the digits with a space or a dot?

```java
Regexer threeDigits = new Regexer();
threeDigits.digit().times(3);
Regexer fourDigits = new Regexer();
fourDigits.digit().times(4);
		
Regexer seperator = new Regexer();
seperator.whitespace().or().literal("-").or().literal(".");
Regexer phone = new Regexer();
phone.add(threeDigits).times(1,seperator).add(threeDigits).times(1,seperator).add(fourDigits);
```

This example will match a phone number that looks like '111-222-3333' or '111.222.3333' or '111 222 3333', with the following regular expression: `\d{3}(\s|\Q-\E|\Q.\E){1}\d{3}(\s|\Q-\E|\Q.\E){1}\d{4}`

As you can see above the intent is to make it easy to compose smaller `Regexer` builders into larger ones to aid with testing and readability.

## Helpful Hints

1. When in doubt, favor `group` over `add`.  Add simply puts the Regulard Expression in at that point in the builder.  Whereas `group` wraps the regular expression in `()` which is *probably* what you want.
2. Break it down.  Try to make your `Regexer` as compositional as possible to improve readability and testability.
3. Use descriptive names for your `Regexer`.  `threeDigits` is more informative than `d3`.
4. Try to make it read like a sentence.  The more readable it is, the easier it is to understand.

## Regexer to Regular Expression Table

For those already familiar with Regular Expressions, the below table can help map `Regexer` functions to their Regular Expression counterpart.

| Function | Regular Expression |
| -------- | ------------------ |
| times(4) | {4} |
| atLeastTimes(4) | {4,} |
| betweenTimes(4,5) | {4,5} |
| onceOrNone() | ? |
| zeroOrMore() | * |
| oneOrMore() | + |
| digit() | \d |
| notDigit() | \D |
| whitespace() | \s |
| notWhitespace() | \S |
| word() | \w |
| nonWord() | \W |
| startsWith() | ^ |
| endsWith() | $ |
| anyChar() | . |
| or | `|` |
| startGroup() | ( |
| endGroup() | ) |
| literal("foo") | foo |
| literal("-") | \Q-\E |

## How can I help?

Two Ways:

1.  Use the library and give me feedback.  I'm open to enhancing this library to make it as usable as possible, while balancing ease of use and readability.
2.  Submit a PR.  If there's a bug or something that might work better, feel free to submit a PR. Just ensure you add a test case for any changes you make.
