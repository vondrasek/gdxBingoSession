package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

public class Flashboard75 {
	
	public  byte[] BOARDMASK = new byte[75];
	public  byte LASTBALL = 0;
	public  byte NBRBALLSCALLED = 0;
	public  String CALLLOG = "LOG STARTED: ";
	
	public enum ENUMWILD{ODDEVEN, TRAILING, LEADING}
	
	public Flashboard75(){
		this.clear();
	}
	
	public boolean isCalled(byte n){
		
		if(BOARDMASK[n-1] == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void clear(){
		for(byte i=0; i< 75; i++){
			BOARDMASK[i] = 0; 
		}
		LASTBALL = 0;
		NBRBALLSCALLED = 0;
		CALLLOG = "LOG STARTED:: ";
	}
	
	public List<Byte> setMask(int b, int i, int n, int g, int o, int lastball){
		int nbc = 0;
		List<Byte> addedBalls = new ArrayList<Byte>(); 
		
		for(int x=0; x<15; x++){
			if((b & (1<<x)) == (1<<x)){
				if(BOARDMASK[x] == 0){
					BOARDMASK[x] = 1;
					addedBalls.add((byte)(x+1));
				}
				nbc++;
			}else{
				BOARDMASK[x] = 0;
			}
		}
		
		for(int x=0; x<15; x++){
			if((i & (1<<x)) == (1<<x)){
				if(BOARDMASK[x+15] == 0){
					BOARDMASK[x+15] = 1;
					addedBalls.add((byte)(x+15+1));
				}
				nbc++;
			}else{
				BOARDMASK[x+15] = 0;
			}
		}
		for(int x=0; x<15; x++){
			if((n & (1<<x)) == (1<<x)){
				if(BOARDMASK[x+30] == 0){
					BOARDMASK[x+30] = 1;
					addedBalls.add((byte)(x+30+1));
				}
				nbc++;
			}else{
				BOARDMASK[x+30] = 0;
			}
		}
		for(int x=0; x<15; x++){
			if((g & (1<<x)) == (1<<x)){
				if(BOARDMASK[x+45] == 0){
					BOARDMASK[x+45] = 1;
					addedBalls.add((byte)(x+45+1));
				}
				nbc++;
			}else{
				BOARDMASK[x+45] = 0;
			}
		}
		for(int x=0; x<15; x++){
			if((o & (1<<x)) == (1<<x)){
				if(BOARDMASK[x+60] == 0){
					BOARDMASK[x+60] = 1;
					addedBalls.add((byte)(x+60+1));
				}
				nbc++;
			}else{
				BOARDMASK[x+60] = 0;
			}
		}
		
		NBRBALLSCALLED = (byte)nbc;
		LASTBALL = (byte)lastball;
		
		return addedBalls;
	}
	
	public void setMask(String fbMask, byte lastball){
		for(int i=0; i<75; i++){
			String s = fbMask.substring(i,i+1);
			if(s.compareTo("1") == 0){
				BOARDMASK[i] = 1;
			}else{
				BOARDMASK[i] = 0;
			}
		}
		LASTBALL = lastball;
	}
	public void addBall(byte n){
		if(BOARDMASK[n-1] == 0){
			BOARDMASK[n-1] = 1;
			LASTBALL = n;
			NBRBALLSCALLED++;
			CALLLOG = CALLLOG + "+" + n + " ";
		}else{
			removeBall(n);
		}
	}
	
	public void removeBall(byte n){
		BOARDMASK[n-1] = 0;
		NBRBALLSCALLED--;
		CALLLOG = CALLLOG + "-" + n + " ";
	}
	
	public String getCallLog(){
		return CALLLOG; 
	}
	
	public void addBall(ENUMWILD e, byte n){
		//Strip number
		
		n = (byte) (n % 10);
		
		if(e == ENUMWILD.ODDEVEN){
			if (n % 2 == 1) {
				// odd
				for(byte b = 0; b<75; b+=2){
					BOARDMASK[b] = 1;
					CALLLOG = CALLLOG + "w" + b + " ";
				}
			} else {
				// even
				for(byte b = 1; b<75; b++){
					BOARDMASK[b] = 1;
					CALLLOG = CALLLOG + "w" + b + " ";
					b = b++;
				}
			}
		}else if(e == ENUMWILD.LEADING){
			n = (byte) (n * 10);
			for(byte b=n; b<75; b ++){
				BOARDMASK[b-1] = 1;
				CALLLOG = CALLLOG + "w" + b + " ";
			}
		}else if(e == ENUMWILD.TRAILING){
			for(byte b=n; b<75; b += 10){
				BOARDMASK[b-1] = 1;
				CALLLOG = CALLLOG + "w" + b + " ";
			}
		}
	}
	
}
