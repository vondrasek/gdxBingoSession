package com.vondrastic.gdxgui;
// INterface for the network packets when a game change or event happens
public interface BingoNetInterface {
	
	public void NewGameState(int LocationID, int GameId, byte[] BallCall, byte LastBall);
	
}
