package com.vondrastic.gdxgui;

public class BingoPatternConfig {
	public int idx; 	 // PatternIdx
    public int cnt; // Number of PatternData // 2 bytes
    public String name; // Name of the Schedule loaded
    public int dataOffset; // Position in data file
    public int res; // Filler // 2 bytes
    // Loaded with all the pattern data
    public int[] data;

}
