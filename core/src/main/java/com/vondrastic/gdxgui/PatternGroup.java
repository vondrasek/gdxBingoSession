package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

// --------------------------------------------------------------------
// Collection of different patterns for multiple pattern combinations
// --------------------------------------------------------------------
public class PatternGroup {
	private int ID;
	private String NAME;
	private int TYPE;
	private int PATTERNGROUPMASK = 0;
	private List<Pattern> PATTERNLIST = new ArrayList<Pattern>();
	
	public PatternGroup(String name, int type){
		this.NAME = name;
		this.TYPE = type;
	}
	
	public void clearPatterns(){
		PATTERNGROUPMASK = 0;
		this.PATTERNLIST.clear();
	}
	
	public boolean addPattern(Pattern pattern){
		
		this.PATTERNLIST.add(pattern);
		PATTERNGROUPMASK |= pattern.getPatternCardMask();
		return true;
	}
	
	public boolean rename(String name){
		this.NAME = name;
		return true;
	}
	
	public String getName(){
		return this.NAME;
	}
	
	public int getPatternGroupCount(){
		return this.PATTERNLIST.size();
	}
	
	public int getPatternCount(int idx){
		return this.PATTERNLIST.get(idx).getMatrixCnt();
	}
	public Pattern getPatternByIndex(int n){
		return this.PATTERNLIST.get(n);
	}
	
	public int getPatternMask(int idx){
		return PATTERNLIST.get(idx).getPatternCardMask();
	}
	
	public int getPatternGroupMask(){
		return this.PATTERNGROUPMASK;
	}
	public boolean save(){
		
		return true;
	}
	
	
}
