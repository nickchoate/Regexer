package org.regex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.regexbuild.RegexBuilderException;
import org.regexbuild.RegexUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class RegexUtilsTests {
	
	@DataProvider(name = "escape")
	public Object[][] provide() {
		return new Object[][]{
			{"abcd","abcd"},
			{"<","\\Q<\\E"},
			{"(","\\Q(\\E"},
			{"<([{\\^-=$!|]})?*+.>","\\Q<([{\\^-=$!|]})?*+.>\\E"},
			{"",""},
			{null,null}
		};
	}
	
	@Test(dataProvider = "escape")
	public void testExactMatch(String stringToEscape, String expectedResult){
		CharSequence actualResult = RegexUtils.escape(stringToEscape);
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@DataProvider(name = "isEmptyCharSequence")
	public Object[][] provideIsEmptyCharSequence() {
		return new Object[][]{
			{"",true},
			{null,true},
			{"a",false},
			{"ab",false}
		};
	}
	
	@Test(dataProvider = "isEmptyCharSequence")
	public void testIsEmptyCharSequence(CharSequence stringToCheck, boolean expectedResult){
		boolean actualResult = RegexUtils.isEmpty(stringToCheck);
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@DataProvider(name = "isEmptyCollection")
	public Object[][] provideIsEmptyCollection() {
		List<String> list = new ArrayList<>();
		list.add("foo");
		
		return new Object[][]{
			{new ArrayList<String>(),true},
			{Collections.EMPTY_LIST,true},
			{null,true},
			{list,false}
		};
	}
	
	@Test(dataProvider = "isEmptyCollection")
	public void testIsEmptyCollection(Collection<?> colToCheck, boolean expectedResult){
		boolean actualResult = RegexUtils.isEmpty(colToCheck);
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@DataProvider(name = "isEmptyCharArray")
	public Object[][] provideIsEmptyArray() {
		List<String> list = new ArrayList<>();
		list.add("foo");
		char[] empty = {};
		char[] oneElement = {'f'};
		return new Object[][]{
			{empty,true},
			{null,true},
			{oneElement,false}
		};
	}
	
	@Test(dataProvider = "isEmptyCharArray")
	public void testIsEmptyCharArray(char[] arrayToCheck, boolean expectedResult){
		boolean actualResult = RegexUtils.isEmpty(arrayToCheck);
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@DataProvider(name = "isEmptyObjectArray")
	public Object[][] provideIsObjectArray() {
		List<String> list = new ArrayList<>();
		list.add("foo");
		Object[] empty = {};
		Object[] oneElement = {Integer.valueOf(1)};
		return new Object[][]{
			{empty,true},
			{null,true},
			{oneElement,false}
		};
	}
	
	@Test(dataProvider = "isEmptyObjectArray")
	public void testIsEmptyObjectArray(Object[] arrayToCheck, boolean expectedResult){
		boolean actualResult = RegexUtils.isEmpty(arrayToCheck);
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@DataProvider(name = "verifyGroupName")
	public Object[][] provideVerifyGroupName() {
		return new Object[][]{
			{"abcd1234",false},
			{"a1",false},
			{"1a",true},
			{"1324",true},
			{"!#$abcd1234",true},
			{"abcd1234.",true}
		};
	}
	
	@Test(dataProvider = "verifyGroupName")
	public void testVerifyGroupName(String groupName, boolean throwsException){
		
		try{
			RegexUtils.verifyGroupName(groupName);
			if(throwsException){
				Assert.fail("Should have thrown RegexBuilderException");
			}
		}
		catch(RegexBuilderException e){
			if(!throwsException){
				Assert.fail("Should not have thrown RegexBuilderException");
			}
		}
		
	}
	
	@DataProvider(name = "verifySingleChar")
	public Object[][] provideVerifySingleChar() {
		return new Object[][]{
			{"a",false},
			{"1",false},
			{"b",false},
			{"#",false},
			{"", true},
			{"abc",true},
			{"123",true},
			{null,true}
		};
	}
	
	@Test(dataProvider = "verifySingleChar")
	public void testVerifySingleChar(String singleChar, boolean throwsException){
		
		try{
			RegexUtils.verifySingleChar(singleChar);
			if(throwsException){
				Assert.fail("Should have thrown RegexBuilderException");
			}
		}
		catch(RegexBuilderException e){
			if(!throwsException){
				Assert.fail("Should not have thrown RegexBuilderException");
			}
		}
		
	}
	
	@DataProvider(name = "substring")
	public Object[][] provideSubstring() {
		return new Object[][]{
			{"a","abcd",0,1},
			{"ab","abcd",0,2},
			{"abcd","abcd",0,50},
			{"",null,45,100},
			{"abcd","abcd",0,-40},
			{"bc","abcd",1,2}
		};
	}
	
	@Test(dataProvider = "substring")
	public void testSubstring(String expectedResult, String str, int start, int length){
		String result = RegexUtils.substring(str, start, length);
		Assert.assertEquals(expectedResult, result);
		
	}
	


}
