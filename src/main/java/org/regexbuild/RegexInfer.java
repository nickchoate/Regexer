package org.regexbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RegexInfer {
	
	private List<String> patterns;
	
	public RegexInfer(){
		this.patterns = new ArrayList<>();
	}
	
	public void addPattern(String pattern){
		this.patterns.add(pattern);
	}
	
	public String simpleInfer(){
		return "";
	}
	
	public String complexInfer(){
		List<String> subs = this.explodeSubstrings();
		ArrayList<Regexer> rbList = new ArrayList<>();
		subs.forEach(System.out::println);
		for(String sub: subs){
			
			boolean match = true;
			int relativePosition = 0;
			for(String pattern: patterns){
				match = match && pattern.contains(sub);
				if(match){
					relativePosition += pattern.indexOf(sub);
				}
			}
			
			if(match){
				relativePosition /= 3;
				Regexer rb = convertToRegex(sub);
				
				rbList.add(rb);
			}
		}
		
		rbList.forEach(System.out::println);
		return "";
	}
	
	private Regexer convertToRegex(String pattern){
		Regexer rb = new Regexer();
		rb.startGroup();
		for(char ch: pattern.toCharArray()){
			if(Character.isDigit(ch)){
					rb.digit();
			}
		}
		rb.endGroup();
		return rb;
	}
	
	private List<String> explodeSubstrings(){
		Set<String> subs = new HashSet<>();
		
		for(String pattern: this.patterns){
			System.out.println(pattern);
			for(int i=0; i<pattern.length(); i++){
				for(int j=1; i+j<=pattern.length(); j++){
					String sub = RegexUtils.substring(pattern, i, j);
					subs.add(sub);
				}
			}
		}
		List<String> sortedSubs = new ArrayList<>(subs);
		Collections.sort(sortedSubs,new LengthCompare().reversed());
		return sortedSubs;
	}

}
