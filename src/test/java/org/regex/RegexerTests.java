package org.regex;

import java.util.regex.Pattern;

import org.regexbuild.Regexer;
import org.regexbuild.RegexUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class RegexerTests {
	
	private boolean matches(Regexer rb, String stringToMatch){
		return Pattern.matches(rb.build(), stringToMatch);
	}
	
	@DataProvider(name = "constructor")
	public Object[][] provideConstructor() {
		return new Object[][]{
			{new Regexer()},
			{new Regexer(4)},
			{new Regexer("f")}
		};
	}
	
	@Test(dataProvider = "constructor")
	public void testFullExampleConstructor(Regexer rb){

		if(RegexUtils.isEmpty(rb.toString())){
			rb.literal("f");
		}
		rb.anyChar().oneOrMore();
		Pattern p = rb.buildAndCompile();
		Pattern p2 = rb.buildAndCompile(Pattern.CASE_INSENSITIVE);
		Pattern p3 = rb.buildAndCompile(Pattern.CASE_INSENSITIVE,Pattern.COMMENTS);
		Assert.assertEquals(true, p.matcher("foos").matches());
		Assert.assertEquals(true, p2.matcher("FOOS").matches());
		Assert.assertEquals(true, p3.matcher("FOOS(?#testcomment").matches());
		Assert.assertEquals(true, rb.matcher("foos").matches());
	}
	
	@DataProvider(name = "literalMatch")
	public Object[][] provide() {
		return new Object[][]{
			{"abcd","abcd",true},
			{"abcd","abc",false}
		};
	}
	
	@Test(dataProvider = "literalMatch")
	public void testExactMatch(String stringToMatch, String exact, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(exact);
		
		Regexer rb2 = new Regexer();
		rb2.literalGroup(exact);
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		Assert.assertEquals(expectedResult, matches(rb2,stringToMatch));
	}
	
	@DataProvider(name = "literalMatchEmpty")
	public Object[][] provideLiteralMatchEmpty() {
		return new Object[][]{
			{"",null},
			{"",""}
		};
	}
	
	@Test(dataProvider = "literalMatchEmpty")
	public void testLiteralMatchEmpty(String expectedResult, String exact){
		Regexer rb = new Regexer();
		rb.literal(exact);
		Assert.assertEquals(expectedResult, rb.toString());
	}
	
	@DataProvider(name = "literalMatchNoEscape")
	public Object[][] provideLiteralMatchNoEscape() {
		return new Object[][]{
			{"foos","f.*s",true},
			{"foos","b.*s",false}
		};
	}
	
	@Test(dataProvider = "literalMatchNoEscape")
	public void testLiteralMatchNoEscape(String stringToMatch, String exact, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literalNoEscape(exact);
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
	}
	
	@DataProvider(name = "exactTimes")
	public Object[][] provideExactTimes() {
		return new Object[][]{
			{"a", "a", 1, true},
			{"aa","a",2, true},
			{"aaaaaa","a",3, false}
		};
	}
	
	@Test(dataProvider = "exactTimes")
	public void testExactTimes(String stringToMatch, String exact, int times, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(exact);
		
		Regexer wrapper = new Regexer();
		wrapper.times(times, rb);
		rb.times(times);
		
		
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		Assert.assertEquals(expectedResult, matches(wrapper,stringToMatch));
	}
	
	@DataProvider(name = "atLeastTimes")
	public Object[][] provideAtLeastTimes() {
		return new Object[][]{
			{"a", "a", 1, true},
			{"aa","a",2, true},
			{"aaaaaa","a",3, true},
			{"aa","a",3,false}
		};
	}
	
	@Test(dataProvider = "atLeastTimes")
	public void testAtLeastTimes(String stringToMatch, String exact, int times, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(exact);
		Regexer grouper = new Regexer();
		grouper.atLeastTimes(times, rb);
		
		rb.atLeastTimes(times);
		
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		Assert.assertEquals(expectedResult, matches(grouper,stringToMatch));
	}
	
	@DataProvider(name = "betweenTimes")
	public Object[][] provideBetweenTimes() {
		return new Object[][]{
			{"a", "a", 1, 1, true},
			{"aa","a", 1, 2, true},
			{"aaaaaa","a",2, 6, true},
			{"aa","a",3,7, false}
		};
	}
	
	@Test(dataProvider = "betweenTimes")
	public void testBetweenTimes(String stringToMatch, String exact, int atLeast, int noMoreThan, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(exact);
		Regexer grouper = new Regexer();
		grouper.betweenTimes(atLeast, noMoreThan, rb);
		
		rb.betweenTimes(atLeast, noMoreThan);
		
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		Assert.assertEquals(expectedResult, matches(grouper,stringToMatch));
	}
	
	@DataProvider(name = "onceOrNone")
	public Object[][] provideOnceOrNone() {
		return new Object[][]{
			{"foo_a", "foo_a", true},
			{"foo_", "foo_a", true},
			{"foo_aa","foo_a", false}
		};
	}
	
	@Test(dataProvider = "onceOrNone")
	public void testOnceOrNone(String stringToMatch, String exact, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(exact);		
		rb.onceOrNone();
		
		
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
	}
	
	@DataProvider(name = "onceOrNoneGroup")
	public Object[][] provideOnceOrNoneGroup() {
		return new Object[][]{
			{"foo_a", "foo_", "a", true},
			{"foo_", "foo_", "a", true},
			{"foo_aa","foo_","a", false}
		};
	}
	
	@Test(dataProvider = "onceOrNoneGroup")
	public void testOnceOrNoneGroup(String stringToMatch, String literal, String onceOrMoreLiteral, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.literal(onceOrMoreLiteral);		

		Regexer grouper = new Regexer();
		grouper.literal(literal);
		grouper.onceOrNone(rb);
			
		Assert.assertEquals(expectedResult, matches(grouper,stringToMatch));
	}
	
	@DataProvider(name = "digitMatch")
	public Object[][] provideDigitMatch() {
		return new Object[][]{
			{"1", true},
			{"2", true},
			{"a", false},
			{"!", false}
		};
	}
	
	@Test(dataProvider = "digitMatch")
	public void testDigitMatch(String stringToMatch, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.digit();
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		
		Regexer rb2 = new Regexer();
		rb2.notDigit();
		Assert.assertEquals(!expectedResult, matches(rb2,stringToMatch));
	}
	
	@DataProvider(name = "startWith")
	public Object[][] provideStartWith() {
		return new Object[][]{
			{"foo_bar", "foo", true},
			{"foo", "foo", true},
			{"a", "foo", false},
			{"bar_foo", "foo", false}
		};
	}
	
	@Test(dataProvider = "startWith")
	public void testStartWith(String stringToMatch, String startsWith, boolean expectedResult){
		Regexer rb = new Regexer();
		rb.startsWith().literalGroup(startsWith).anyChar().zeroOrMore();
		Assert.assertEquals(expectedResult, matches(rb,stringToMatch));
		
	}
	
	@DataProvider(name = "specificExamples")
	public Object[][] provideSpecificExamples() {
		Regexer threeDigits = new Regexer();
		threeDigits.digit().times(3);
		Regexer fourDigits = new Regexer();
		fourDigits.digit().times(4);
		Regexer hyphen = new Regexer("-");
		Regexer phone = new Regexer();
		phone.add(threeDigits, hyphen, threeDigits, hyphen, fourDigits);
		
		Regexer seperator = new Regexer();
		seperator.whitespace().or().literalNoEscape("-");
		Regexer phone2 = new Regexer();
		phone2.add(threeDigits).times(1,seperator).add(threeDigits).times(1,seperator).add(fourDigits);
		
		Regexer tagName = new Regexer();
		tagName.word().oneOrMore();
		
		Regexer tagContent = new Regexer();
		tagContent.anyChar().zeroOrMore();
		
		Regexer tagAndContent = new Regexer();
		tagAndContent.literalNoEscape("<").group(tagName).anyChar().zeroOrMore()
						.literalNoEscape(">").group(tagContent).literalNoEscape("<")
						.literal("/").groupRef(1).literalNoEscape(">");
		
		Regexer findTextFiles = new Regexer();
		findTextFiles.anyChar().oneOrMore().literal(".").startGroup().literal("txt").endGroup().endsWith();
		
		return new Object[][]{
			{"111-222-3333", phone, true, "Phone regex failed." + phone.build()},
			{"111*222*3333", phone, false, "Phone regex failed." + phone.build()},
			{"111-222-3333", phone2, true, "Phone regex failed." + phone2.build()},
			{"111 222 3333", phone2, true, "Phone regex failed." + phone2.build()},
			{"111*222*3333", phone2, false, "Phone regex failed." + phone2.build()},
			{"111", threeDigits, true, "Three Digit regex failed." + threeDigits.build()},
			{"1111", threeDigits, false, "Three Digit regex failed." + threeDigits.build()},
			{"1111", fourDigits, true, "Four Digit regex failed." + fourDigits.build()},
			{"11111", fourDigits, false, "Four Digit regex failed." + fourDigits.build()},
			{"<a> foo </a>", tagAndContent, true, "Tag Content failed." + tagAndContent.build()},
			{"<a> </a>", tagAndContent, true, "Tag Content failed." + tagAndContent.build()},
			{"<a></a>", tagAndContent, true, "Tag Content failed." + tagAndContent.build()},
			{"<a> foo", tagAndContent, false, "Tag Content failed." + tagAndContent.build()},
			{"foo.txt", findTextFiles, true, "Find Text File failed." + findTextFiles.build()},
			{"foo.jar", findTextFiles, false, "Find Text File failed." + findTextFiles.build()}
		};
	}
	
	@Test(dataProvider = "specificExamples")
	public void testSpecificExamples(String stringToMatch, Regexer rb, boolean expectedResult, String assertMessage){

		Assert.assertEquals(assertMessage, expectedResult, matches(rb,stringToMatch));
	}

}
