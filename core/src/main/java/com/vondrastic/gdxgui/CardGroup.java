package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.Collections;

public class CardGroup {
	public enum CARDTYPE{REGULAR, DOUBLEACTION, STAR, PAGE, BONUSLINE}
	
	private CARDTYPE TYPE;
	private int PACKID;
	private int COLOR32;
	private int QTY;
	private int LEVEL;
	private String LIBRARY;
	// Total Cards = x * Y * Z
	private ArrayList<BingoCard> CARDLIST =  new ArrayList<BingoCard>();
	
	public CardGroup(int packid, CARDTYPE type, int color32, int qty, int level, String library){
		
		this.PACKID = packid;
		this.TYPE = type;
		this.QTY = qty;
		this.LIBRARY = library;
		this.LEVEL = level;
		this.COLOR32 = color32;
		
		if(addCards(PACKID, QTY, LEVEL, LIBRARY)){
			//SUCCESS
		}else{
			//FAILURE
		}
	}
	
	private boolean addCards(int packid, int qty, int level, String library){
		int i = 0;
		for(i=0; i<qty; i++){		
			this.CARDLIST.add(new BingoCard(this.PACKID + (i), this.LEVEL, this.LIBRARY,0));
			QTY++;
		}
		return true;
	}
	// Score all the cards in the pack against the pattern group and all patterns in group; 
	public int scoreCards(PatternGroup pg){
		return 0;
	}
	
	// Sort the cards in the pack by the best first
	public void sortCardsBest(){
		
		Collections.sort(CARDLIST);
		
	}
	
	// Sort the cards by ID number
	public void sortCardsByID(){
		
		Collections.sort(CARDLIST, new CardReg75Comparator());
	
	}
	
	public int getTotalQty(){
		
		return this.QTY;
		
	}
	
}
