package org.regexbuild;

public class RegexBuilderException extends RuntimeException
{
	private static final long serialVersionUID = -5178125264563304895L;
	
	public RegexBuilderException(String msg) {
		super(msg);
	}
	
	public RegexBuilderException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
