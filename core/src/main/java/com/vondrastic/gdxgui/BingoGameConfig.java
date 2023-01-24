package com.vondrastic.gdxgui;

public class BingoGameConfig {
	   public int gamenbr; 		//Number referenced by Schedule
	   public String gamedesc;  			//Game Name for information to display
	   public String prizedesc; 			//Prize Description for display
	   public byte cardColor;			//color of card
	   public int GameType; 		//Type of game playing. Rules and Such. Used for scoring and Play.
	   public int NbrOfDiffernPatMatchReq; // Number of Pattern Needed for a Win.
	   public String permfile;  			//Name of the Perm file to Use for the Cards to load
	   public byte ExtraPurchaseAllowed; 		//Can the Player Use Credits to Buy Additional Cards: Future Use
	   public int MaxCardsAllowed;  	//Maximum Cards allowed for this game. : Requlation Check
	   public int l1permqty;		//Number of Level 1 Cards to load
	   public int l1permstart; 		//First Card to begin load from
	   public int l2permqty;		//Number of Level 2 Cards to load
	   public int l2permstart; 		//First Card to begin load from
	   public int l3permqty;		//Number of Level 3 Cards to load
	   public int l3permstart; 		//First Card to begin load from
	   public int l4permqty;		//Number of Level 4 Cards to load
	   public int l4permstart; 		//First Card to begin load from
	   public int NbrOfPatternPacks; 	//Game Can reference multiple patterns
	   public String Pattern1Name; 
	   public String Pattern2Name; 
	   public String Pattern3Name; 
	   public String Pattern4Name; 
	   public int page_col; 		//Number of columns on the page. If 0 then just load the normal way
	   public int page_row;		//Number of rows on the page
	   public int page_drop; 		//after every page we need to add this to the orginal starting seed.
	   public int page_ho; 		//horizontal offset
	   public int page_vo; 		//vertical offset
	   public byte res1;
	   public byte res2;
	   public int GetCardCountTotal(){
		   return l1permqty + l2permqty + l3permqty + l4permqty;
	   }
}
