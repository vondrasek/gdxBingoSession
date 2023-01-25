package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PatternInfoObject extends GuiObject{
	TextureRegion ON;
	PatternGroup PG;
	float StateTime;
	boolean VISABLE = true;
	int PatternMatrixTotal = 0;
	float ShowDurration = 1; // Number of seconds to display cureent pattern
	
	public PatternInfoObject(float x, float y,TextureRegion bg, TextureRegion on, PatternGroup pg){
		super(x, y, true, bg);
		PG = pg;
		ON = on;
		PatternMatrixTotal = GetTotalPatternCount();
	}
	
	public PatternInfoObject(float x, float y,float w, float h, TextureRegion bg, TextureRegion on, PatternGroup pg){
		super(x, y, w, h,true, bg);
		PG = pg;
		ON = on;
		PatternMatrixTotal = GetTotalPatternCount();
	}
	
	public void setPattern(PatternGroup pg){
		PG = pg;
		PatternMatrixTotal = GetTotalPatternCount();
	}
	// Based on state time get the next
	private int GetTotalPatternCount(){
		int pCnt = 0;
		for(int i=0; i<PG.getPatternGroupCount(); i++){
			pCnt+=PG.getPatternByIndex(i).getMatrixCnt();
		}
		return pCnt;	
	}
	
	private int GetCurrentIdx(){
		return (int) ((StateTime / ShowDurration ) % PatternMatrixTotal);
	}
	
	private Rectangle GetCellRectangle(int x, int y){
		float spacer = 4;
		float heightoffset = spacer * 6;
		float widthoffset = spacer * 6;
		
		
		float cellwidth = ((super.getWidth() - widthoffset ) / (float)5);
		float cellheight = ((super.getHeight() - heightoffset) / (float)5);
		
		Rectangle r = new Rectangle(super.getX() + (x * cellwidth) + spacer + (x*spacer) , super.getY() + (y * cellheight) + spacer + (y*spacer), cellwidth, cellheight);	
		return r;
	}
	
	public void update(float DeltaTime){
		StateTime+=DeltaTime;
	}
	
	public void draw(SpriteBatch SB, float DeltaTime){
		update(DeltaTime);
		if(VISABLE){
			super.draw(SB);
			if(PG == null || PG.getPatternGroupCount() == 0) return;
			int mask = PG.getPatternByIndex(0).getPatternData(GetCurrentIdx());
			int i = 0;
			Rectangle rect;
			
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				for(int c = 0; c<5; c++){
					int d = (mask & (1 << i));
					if(d>0){
						rect = GetCellRectangle(c,y);
						SB.draw(ON,rect.x, rect.y, rect.width,rect.height);
					}
					i++;
				}
			}
			String s =  PG.getName();
			float msgWidth = Utils.getTextBoundsWidth(Assets.font24, s);
			//float msgHeight = Assets.font32.getBounds(s).height;
			Assets.font24.setColor(1, 1, 1, 1);
			Assets.font24.draw(SB, s , super.getX() + (super.getWidth() / 2) - (msgWidth / 2)  , super.getY());
		}
	}
}
