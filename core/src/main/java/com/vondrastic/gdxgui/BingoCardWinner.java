package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// Display the Card that has a winning Bingo on it. 
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GuiObjectButton.TYPE;

public class BingoCardWinner extends GuiObject{
	
	private List <BingoCard> bcd = new ArrayList<BingoCard>();
	private boolean loaded = false;
	private GuiObjectButton btnClose = new GuiObjectButton((float)0, (float)0, (float)128, (float)64, (float)1, "Next", Assets.keybtnUP_region, Assets.keybtnDN_region, TYPE.ACTION,null,null );
	private GuiObjectButton btnNext = new GuiObjectButton((float)0, (float)0, (float)128, (float)64, (float)1, "Next", Assets.keybtnUP_region, Assets.keybtnDN_region, TYPE.ACTION,null,null );
	
	public BingoCardWinner(float x, float y, float w, float h, boolean visable, TextureRegion t) {
		super(x, y, w, h, visable, t);
		if(loaded == false){
			loadAssets();
			loaded = true;
		}
	}
	
	public void addBingoCard(BingoCard bc){
		bcd.add(bc);
	}
	// Check for touch. Advance to next card if touched. 
	public boolean Touched(Vector3 point){
		if(OverlapTester.pointInRectangle(super.getRectangle(), point.x, point.y)){
			return true;
		}
		return false;
	}
	// Load Button 
	private void loadAssets(){
		
	}
	// Load the next bingo card in the winner list
	private void nextCard(){
		
	}
	// Draw cardID, Winning pattern, Card Level, gameNbr
	private void drawTextInfo(){
		
	}
	
	// Only draw winning pattern area of the card
	public void draw(SpriteBatch sb, float deltatime){
		
	}

}
