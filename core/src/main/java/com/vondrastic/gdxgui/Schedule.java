package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private String NAME;
	private int HALLID;
	private List<BingoGame> GAMELIST = new ArrayList<BingoGame>();
	
	public Schedule(String name){
		this.NAME = name;
		
	}
	
	public boolean saveSchedule(){
		
		return false; 
	}
	
	public boolean loadSchedule(String name){
		
		return false;
	}
	
	public boolean loadSchedule(int index){
		
		return false;
	}
	
	public void addGame(String name, PatternGroup pg, CardGroup cg, Payout po, int cardmax, int cardcost, boolean trades, int tradevalue ){
	
		GAMELIST.add(new BingoGame(name, pg, cg, po, cardmax, cardcost, trades, tradevalue ));
		
	}
	// Move sort order
	public void moveGameUp(int gameid){
		
	}
	
	public void moveGameDown(int gameid){
		
	}
	
	
}
