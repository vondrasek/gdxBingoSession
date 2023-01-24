package com.vondrastic.gdxgui;

public class Payout {
	private int PAY = 0;
	private int LEVEL = 0; // If 0 Pays for all levels
	// Progressive linked payout
	private int CONSOLATION = 0;
	private int FLOOR = 0; // Minimum to pay player. 
	 
	public enum PAYTYPE{REGULAR, PROGRESSIVE}
	private PAYTYPE PAYOUT_TYPE; // Controls the logic for payout.
	
	public Payout(int level, int pay, int floor, int consolation, PAYTYPE paytype ){
		PAY = pay;
		LEVEL = level;
		FLOOR = floor;
		CONSOLATION = consolation;
		PAYOUT_TYPE = paytype;
	}
	
}
