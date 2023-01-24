package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

public class BingoCard extends PermData implements Comparable<BingoCard> {
	public static PatternGroup PG;
	public static Flashboard75 FB;
	
	private int ID = 0;
	private int LEVEL = 0;
	private int DAUBMASK = 0;
	private byte LASTNBRDAUBED = 0;
	private int DAUBCNT = 0;
	private byte[][] DATA = new byte[5][5];
	
	public int SCORE = 24; //How many away from Bingo
	public boolean WINNER_FLAG = false;
	public List<Byte> OneAwayList = new ArrayList<Byte>();
	public boolean ONEAWAY = false;
	
	public BingoCard(int id, int level, byte[][] data ){
		this.ID = id;
		this.LEVEL = level;
		this.DATA = data;
		Reset();
	}
	
	public BingoCard(int id, int level, String library, int cardtype ){
		
		this.ID = id;
		this.LEVEL = level;
		this.DATA = super.GetCardData(library, id, cardtype);
		Reset();
	}
	
	public void SetPatternGroup(PatternGroup pg){
		PG = pg;
	}
	
	public void SetFlashboard(Flashboard75 fb){
		FB = fb;
	}
	
	public void ScoreCard(){
		
		int cMask = getDaubmask();
		int best = 24;
		int bestPatternIdx =0;
		ONEAWAY = false;
		
		if(PG == null){return;}
		for(int i=0; i<PG.getPatternGroupCount(); i++){
			int tPatterns = PG.getPatternByIndex(i).getMatrixCnt();
			for(int n=0; n< tPatterns; n++){
				int pMask = PG.getPatternByIndex(i).getPatternData(n);
				int mask = (int)(cMask & pMask);
				mask = (mask ^ pMask);
				int away = Utils.NbrOfBitsSet(mask);
				if(away == 1){ 
					byte[] nbr = getDataByBitPosition(mask);
					for(int q=0; q<nbr.length; q++){
						AddOneAway(nbr[q]);
					}
					ONEAWAY = true;
				}
				if(away < best ){
					best = away;
					bestPatternIdx = n;
				}
			}
		}
		SCORE = best;
	}
	
	public byte GetLastNbrDaubed(){
		return LASTNBRDAUBED;
	}
	
	public void AddOneAway(byte n){
		for(int i=0; i<OneAwayList.size(); i++){
			if(n == OneAwayList.get(i)){
				return; //Already in the list
			}
		}
		OneAwayList.add(n);
	}
	public void RemoveOneAway(byte n){
		for(int i=0; i<OneAwayList.size(); i++){
			if(n == OneAwayList.get(i)){
				OneAwayList.remove(i);
				return; //Already in the list
			}
		}
	}
	
	public void ClearOneAway(){
		OneAwayList.clear();
	}
	
	public List<Byte> GetOneAwayList(){
		return OneAwayList;
	}
	
	public boolean IsOneAway(byte n){
		if(IsOneAway()){
			for(int i=0; i<OneAwayList.size(); i++){
				if(n == OneAwayList.get(i)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean IsOneAway(){
		return ONEAWAY;
	}
	
	public boolean IsWinner(){
		if(SCORE == 0){
			return true;
		}
		return false;
	}
	
	public void Reset(){
		ClearOneAway();
		DAUBMASK = (1<<12); // mark Free space
		LASTNBRDAUBED = 0;
		SCORE = 24;
		DAUBCNT = 0;
		ONEAWAY = false;
		WINNER_FLAG = false;
	}
	
	public boolean daub(int nbr){
		int x = 0;
		int y = 0;
		// Get the correct column
		if(nbr >=1 && nbr <= 15){
			x = 0;
		}else if(nbr >=16 && nbr <= 30){
			x = 1;
		}else if(nbr >=31 && nbr <= 45){
			x = 2;
		}else if(nbr >=46 && nbr <= 60){
			x = 3;
		}else if(nbr >=61 && nbr <= 75){
			x = 4;
		}
		if(DATA[0].length == 10){
			for(;y<5;y++){
				if(this.DATA[x][y] == nbr || this.DATA[x][y+5] == nbr){
					MarkMask(x,y);
					LASTNBRDAUBED = (byte) nbr;
					return true;
				}
			}
		}else{
			for(;y<5;y++){
				if(this.DATA[x][y] == nbr){
					MarkMask(x,y);
					LASTNBRDAUBED = (byte) nbr;
					return true;
				}
			}
		}
		return false;
	}
	
	public byte[] getDataByBitPosition(int n){
		int pos = Utils.getPosByBit(n);
		
		int x = pos % 5;
		int y = pos / 5;
		
		if(DATA[0].length ==10){
			byte[] d = new byte[2];
			d[0] = DATA[x][y];
			d[1] = DATA[x][y+5];
			return d;
		}else{
			byte[] d = new byte[1];
			d[0] = DATA[x][y];
			return d;
		}
	}
	
	public void daub(int x, int y){
		MarkMask(x,y);
		LASTNBRDAUBED = this.DATA[x][y];
	}
	
	public boolean undaub(int nbr){
		int x = 0;
		int y = 0;
		// Get the correct column
		if(nbr >=1 && nbr <= 15){
			x = 0;
		}else if(nbr >=16 && nbr <= 30){
			x = 1;
		}else if(nbr >=31 && nbr <= 45){
			x = 2;
		}else if(nbr >=46 && nbr <= 60){
			x = 3;
		}else if(nbr >=61 && nbr <= 75){
			x = 4;
		}
		
		for(;y<5;y++){
			if(this.DATA[x][y] == nbr){
				UnMarkMask(x,y);
				LASTNBRDAUBED = 0;
				return true;
			}
		}
		return false;
	}
	
	public void undaub(int x, int y){
		UnMarkMask(x,y);
		LASTNBRDAUBED = 0;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public int getScore(){
		return SCORE;
	}
	
	public int getLevel(){
		return this.LEVEL;
	}
	
	public int getDaubmask(){
		return this.DAUBMASK;
	}
	
	public byte[][] getDataArray(){
		return this.DATA;
	}
	
	public int getCellData(int x, int y){
		return this.DATA[x][y];
	}
	
	public boolean isDaubed(int x, int y){
		int tmp = (1 << ((5 * y) + x));
		int mask = (this.DAUBMASK & tmp);
		if(mask == tmp){
			return true;
		}else{
			return false;
		}
	}
	
	private void MarkMask(int x, int y){
		this.DAUBMASK|= (1 << ((5 * y) + x));
		DAUBCNT++;
	}
	
	private void UnMarkMask(int x, int y){
		this.DAUBMASK ^= (1 << ((5 * y) + x));
		DAUBCNT--;
	}
	
	private int getScoreSort(){
		//return (SCORE - OneAwayList.size()) * 100  - (DAUBCNT); 
		return ((SCORE  - OneAwayList.size()) * 100) - DAUBCNT; 
	}

	@Override
	public int compareTo(BingoCard card) {
		return this.getScoreSort() - card.getScoreSort(); 
		
	}
}
