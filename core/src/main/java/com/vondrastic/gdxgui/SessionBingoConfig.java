package com.vondrastic.gdxgui;
// Loaded for the config file. 
public class SessionBingoConfig {
	  
	public int TotalGameRecord; 	// Total Number of Game records not including Footer 
	public String FormatName;			// Name of the Schedule loaded
	public int ReceiptID; 		// Sales Receipt information
	public int SaleType;		// 0 = New Sale Wipe out any old data | 1 = Add On Sale Keep Old data Append to Existing. 
	public int PlayerNbr; 		// Used for Customer Tracking
	public int PlayerCredits; 		// Cash in pennies Loaded into the Machine.
	public int ValidSessionNbr; 	// Security Session ID. Validates sale to a Session
	public byte BingoSound; 			// Play a sound when there is a bingo
	public byte DaubModeAllowed;			// 3 settings :0 = No RF allowed,1 = RF Restricted :2 = RF ALLOWED No restrictions
	public int  GamesAllowed; 		// BitMask of the game player can play. For Future Use
	public byte hours;				//number of hours valid 0-always.
	public byte volume;				//volume 1-127
	
	public static final byte daubmode_manual = 0;
	public static final byte daubmode_confirm = 1;
	public static final byte daubmode_auto = 2;
	public static final byte daubmode_gamechange = 3;
  
}
