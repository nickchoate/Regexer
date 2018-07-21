package org.regexbuild;

import java.util.Collection;
import java.util.regex.Pattern;

public final class RegexUtils {
	
	private final static String charsToEscapeRegex = "[\\Q<([{\\^-=$!|]})?*+.>\\E]{1,}";
	private final static String groupNameRegex = "[A-Za-z][A-Za-z0-9]*";
	
	public static boolean isEmpty(CharSequence cs){
		boolean isEmpty = false;
		if(cs == null || cs.length() < 1){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static boolean isEmpty(Collection<?> col){
		boolean isEmpty = false;
		if(col == null || col.size() < 1){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static boolean isEmpty(char[] array){
		boolean isEmpty = false;
		if(array == null || array.length < 1){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static boolean isEmpty(Object[] array){
		boolean isEmpty = false;
		if(array == null || array.length < 1){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public static CharSequence escape(CharSequence cs){
		CharSequence escapedSeq = cs;
		if(!isEmpty(cs)){
			String csStr = cs.toString();
			if (csStr.matches(charsToEscapeRegex)){
				escapedSeq = Pattern.quote(csStr);
			}
		}
		return escapedSeq;
	}
	
	public static void verifyGroupName(String name){
		if(!Pattern.matches(groupNameRegex, name)){
			throw new RegexBuilderException("The group name can only contain lower and upperase letters A-Z and digits 0-9. The first character must be a letter.");
		}
	}
	
	public static void verifySingleChar(String input){
		if(isEmpty(input) || input.length() != 1){
			throw new RegexBuilderException("String must be a single character.");
		}
	}
	
	public static String substring(String str, int start, int length){
		if(isEmpty(str)){
			return "";
		}
		if(length < 1){
			return str;
		}
		int end = start + length;
		end = Math.min(end, str.length());  
		String sub = str.substring(start, end);
		return sub;
	}
	
}
