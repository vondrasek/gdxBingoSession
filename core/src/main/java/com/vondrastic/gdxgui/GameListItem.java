package com.vondrastic.gdxgui;

public class GameListItem {
	
	public String gameName;
	public String patternName;
	public int gameIdx;
	public int cardCnt;
	
	public GameListItem(String gamename, String patternname, int gameidx, int cardcnt){
		gameName = gamename;
		patternName = patternname;
		gameIdx = gameidx;
		cardCnt = cardcnt;
	}
}
