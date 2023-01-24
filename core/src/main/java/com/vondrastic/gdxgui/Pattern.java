package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

/**
 * @author helmetron
 * 
 */

public class Pattern extends PatternData{
	private int id = 0;
	private int matrixcnt = 0;
	private String name;
	private int pay;
	private byte mincalls = 0; 
	private byte maxcalls = 0;
	private boolean active = true;
	private int cardmask = 0;
	private int type = 0; // 0 = consolation pattern ; 2 = group reward pattern; 1 = game ending pattern
	private List<Integer> datamask = new ArrayList<Integer>();
	
	private static int total = 0;	
	
	public Pattern(String name, int pay, int type, byte mincall, byte maxcall){
		
		this.total +=1;
		this.id = total;
		this.name = name;
		this.pay = pay;
		this.type = type;
		this.mincalls = mincall; // Minimun numbers to get full prize
		this.maxcalls = maxcall; // Max Number to get full prize;
		cardmask = 0;
	}
	
	public boolean loadPatternData(String filename){
		int[] tmp;
		tmp = super.GetPatternData(filename);
		if(tmp == null){
			return false;
		}else{
			datamask.clear();
			for(int i=0; i < tmp.length; i++){
				this.datamask.add(tmp[i]);
				cardmask|=tmp[i];
			}
			this.name = filename;
			return true;
		}
	}
	
	public boolean AddMatrix(int i){
		matrixcnt++;
		this.datamask.add(i);
		cardmask|=i;
		return true;
	}
	// pass 25 character string like "1000110001100011000110001"
	public boolean AddMatrix(String s){
		String n;
		String set = "1";
		int d = 0;
		
		if(s.length() == 25){
			for(int i=0; i<25; i++){
				n = s.substring(i, i+1);
				if(n.equals(set)){
					d |= (1 << i);
				}
			}
			
			AddMatrix(d);
			return true;
		}else{
			return false;
		}
	}
	
	public int getMatrixCnt(){
		return this.matrixcnt;
	}
	
	public int[] getPatternData(){
		int[] i = new int[this.datamask.size()];
		for(int n=0; n<this.datamask.size(); n++){
			i[n] = this.datamask.get(n);
		}
		return i;
	}
	
	public int getPatternData(int idx){
		return this.datamask.get(idx);
	}
	
	public String getPatternName(){
		return this.name;
	}
	
	public int getPatternPay(){
		return this.pay;
	}
	
	public int getPatternID(){
		return this.id;
	}
	
	public int getPatternType(){
		return this.type;
	}
	public int getPatternCardMask(){
		return cardmask;
	}
	
	public void Activate(){
		active = true;
	}
	
	public void deactivate(){
		active = false;
	}
	
}
