package org.regexbuild;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexer {
	
	private StringBuilder exp;
	private Set<String> groupNames;
	
	public Regexer(){
		this.exp = new StringBuilder();
		this.groupNames = new HashSet<>();
	}
	
	public Regexer(CharSequence exact){
		this.exp = new StringBuilder(exact.length());
		this.literal(exact);
		this.groupNames = new HashSet<>();
	}
	
	public Regexer(int capacity){
		this.exp = new StringBuilder(capacity);
		this.groupNames = new HashSet<>();
	}
	
	public String toString(){
		return this.exp.toString();
	}
	
	public String build(){
		return this.exp.toString();
	}
	
	public Pattern buildAndCompile(){
		return Pattern.compile(this.build());
	}
	
	public Pattern buildAndCompile(int... flags){
		int patternFlags = -1;
		for(int f : flags){
			if (patternFlags == -1) {
				patternFlags = f;
			} else {
				patternFlags = patternFlags | f;
			}
		}
		return Pattern.compile(this.build(), patternFlags);
	}
	
	public Matcher matcher(String input){
		return this.buildAndCompile().matcher(input);
	}
	
	private Regexer literal(CharSequence exact, boolean doEscape){
		if(!RegexUtils.isEmpty(exact)){
			if (doEscape) {
				this.exp.append(RegexUtils.escape(exact));
			} else {
				this.exp.append(exact);
			}
		}
		return this;
	}
	
	public Regexer literalGroup(CharSequence exact){
		return this.group(new Regexer(exact));
	}
	
	public Regexer literal(CharSequence exact){
		return this.literal(exact, true);
	}
	
	public Regexer literalNoEscape(CharSequence exact){
		return this.literal(exact, false);
	}
	
	public Regexer times(int times){
		this.exp.append("{");
		this.exp.append(times);
		this.exp.append("}");
		return this;
	}
	
	public Regexer times(int times, Regexer regex){
		return this.group(regex).times(times);
	}
	
	public Regexer atLeastTimes(int times){
		this.exp.append("{");
		this.exp.append(times);
		this.exp.append(",");
		this.exp.append("}");
		return this;
	}
	
	public Regexer atLeastTimes(int times, Regexer regex){
		return this.group(regex).atLeastTimes(times);
	}
	
	public Regexer betweenTimes(int atLeast, int noMoreThan){
		this.exp.append("{");
		this.exp.append(atLeast);
		this.exp.append(",");
		this.exp.append(noMoreThan);
		this.exp.append("}");
		return this;
	}
	
	public Regexer betweenTimes(int atLeast, int noMoreThan, Regexer regex){
		return this.group(regex).betweenTimes(atLeast, noMoreThan);
	}
	
	public Regexer onceOrNone(){
		this.exp.append("?");
		return this;
	}
	
	public Regexer onceOrNone(Regexer regex){
		return this.group(regex).onceOrNone();
	}
	
	public Regexer zeroOrMore(){
		this.exp.append("*");
		return this;
	}
	
	public Regexer zeroOrMore(Regexer regex){
		return this.group(regex).zeroOrMore();
	}
	
	public Regexer oneOrMore(){
		this.exp.append("+");
		return this;
	}
	
	public Regexer oneOrMore(Regexer regex){
		return this.group(regex).oneOrMore();
	}
	
	public Regexer digit(){
		this.exp.append("\\d");
		return this;
	}
	
	public Regexer notDigit(){
		this.exp.append("\\D");
		return this;
	}
	
	public Regexer whitespace(){
		this.exp.append("\\s");
		return this;
	}
	
	public Regexer notWhitespace(){
		this.exp.append("\\S");
		return this;
	}
	
	public Regexer word(){
		this.exp.append("\\w");
		return this;
	}
	
	public Regexer nonWord(){
		this.exp.append("\\W");
		return this;
	}
	
	public Regexer startsWith(){
		this.exp.append("^");
		return this;
	}
	
	public Regexer endsWith(){
		this.exp.append("$");
		return this;
	}
	
	public Regexer anyChar(){
		this.exp.append(".");
		return this;
	}
	
	public Regexer not(){
		this.exp.append("^");
		return this;
	}
	
	public Regexer or(){
		this.exp.append("|");
		return this;
	}
	
	public Regexer and(){
		this.exp.append("&&");
		return this;
	}
	
	public Regexer startGroup(){
		this.exp.append("(");
		return this;
	}
	
	public Regexer endGroup(){
		this.exp.append(")");
		return this;
	}
	
	private Regexer addBuilder(Regexer builder, String start, String stop){
		this.groupNames.addAll(builder.groupNames);
		this.exp.append(start);
		this.exp.append(builder.build());
		this.exp.append(stop);
		return this;
	}
	
	public Regexer group(Regexer builder){
		return addBuilder(builder, "(", ")");
	}
	
	public Regexer charClass(Regexer builder){
		return addBuilder(builder, "[", "]");
	}
	
	public Regexer group(Regexer builder, String name){
		RegexUtils.verifyGroupName(name);
		this.groupNames.addAll(builder.groupNames);
		this.groupNames.add(name);
		this.exp.append("(?<");
		this.exp.append(name);
		this.exp.append(">");
		this.exp.append(builder.build());
		this.exp.append(")");
		return this;
	}
	
	public Regexer groupRef(int number){
		this.exp.append("\\");
		this.exp.append(number);
		return this;
	}
	
	public Regexer groupRef(String name){
		verifyGroupNameExists(name);
		this.exp.append("\\k<");
		this.exp.append(name);
		this.exp.append(">");
		return this;
	}
	
	public Regexer add(Regexer builder){
		return this.addBuilder(builder, "", "");
	}
	
	public Regexer add(Regexer... builders){
		for(Regexer builder: builders){
			this.add(builder);
		}
		return this;
	}
	
	
	public Regexer charClass(CharClass charClass){
		this.exp.append(charClass.build());
		return this;
	}
	
	private void verifyGroupNameExists(String name){
		if(!this.groupNames.contains(name)){
			throw new RegexBuilderException("Group reference doesn't exist in expression.");
		}
	}

}
