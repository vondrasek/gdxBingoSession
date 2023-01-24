package com.vondrastic.gdxgui;

public class TextMessage {
	public String text;
	public int displayTime = 10; // seconds
	public int displayCnt = 0; // infinite
	public int displayedCnt = 0;
	
	public TextMessage(String t, int durration, int repeatCnt){
		text = t; 
		displayTime = durration;
		displayCnt = repeatCnt;  
	}
}
