package org.regexbuild;

public class CharClass {
	
	private StringBuilder exp;
	
	public CharClass(){
		this.exp = new StringBuilder();
	}
	
	public CharClass simple(String chars){
		this.exp.append(chars);
		return this;
	}
	
	public CharClass simple(char... chars) {
		if (!RegexUtils.isEmpty(chars)) {
			for (int i = 0; i < chars.length; i++) {
				this.exp.append(chars[i]);
			}
		}
		return this;
	}
	
	public CharClass range(char first, char second){
		this.exp.append(first);
		this.exp.append("-");
		this.exp.append(second);
		return this;
	}
	
	public CharClass range(String first, String second){
		this.exp.append(first);
		this.exp.append("-");
		this.exp.append(second);
		return this;
	}
	
	public CharClass not(){
		this.exp.append("^");
		return this;
	}
	
	public CharClass and(){
		this.exp.append("&&");
		return this;
	}
	
	public CharClass nest(CharClass charClass){
		this.exp.append(charClass.build());
		return this;
	}
	
	public String build(){
		return "[" + this.exp.append("]").toString();
	}

}
