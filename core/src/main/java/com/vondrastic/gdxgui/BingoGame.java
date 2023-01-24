package com.vondrastic.gdxgui;

public class BingoGame {
	private int SORTIDX;
	
	private String NAME;
	
	private int CARDCOST = 1; // Price of Card for pay to play in credits.
	private int CARDCOSTQTY = 3; // Nbr of cards per card cost. default 3 per credit
	private int CARDMAX = 12; // Maxumum cards allowed per game; 
	private boolean TRADESALLOWED = false; 
	private int TRADEVALUE = 0; // Value of traded cards if allowed
	private static int GAMEIDX;
	
	public PatternGroup PATTERNGROUP;
	public CardGroup CARDGROUP;
	public Flashboard75 FLASHBOARD;
	public Payout PAYOUT;
	
	public enum GAMETYPE{REGULAR, CARZYKITE, BONANZA}
	
	public BingoGame(String name, PatternGroup pg, CardGroup cg, Payout po, int cardmax, int cardcost, boolean tradesallowed, int tradevalue){
		
		GAMEIDX++;
		SORTIDX = GAMEIDX;
		NAME = name;
		CARDMAX = cardmax;
		CARDCOST = cardcost;
		TRADESALLOWED = tradesallowed;
		TRADEVALUE = tradevalue;
		
		FLASHBOARD = new Flashboard75();
		PATTERNGROUP = pg;
		CARDGROUP = cg;
		PAYOUT = po;
	}
	
	public void addBallCalled(byte n){
		this.FLASHBOARD.addBall(n);
	}
	
	public void removeBallCalled(byte n){
		this.FLASHBOARD.removeBall(n);
	}
	
	public int markCards(){
		
		return -1;
	}
	// --------------------------------------
	// Score Cards Against the pattern group
	// --------------------------------------
	public void scoreCards(){
		this.CARDGROUP.scoreCards(this.PATTERNGROUP);
	}
	
	public boolean addCards(CardGroup cg){
		
		return false;
	}
}
