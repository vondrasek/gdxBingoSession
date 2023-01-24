package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

// Advertisement Object wit a link to the web if clicked.
// Timer to auto hide after a set time. 
public class GuiObjectAd extends GuiObject {

	private float stateTime = 0;
	private float displayTime = 10;
	private boolean clickHide = true;
	public int gameIdx = 0;
	
	public GuiObjectAd(float x, float y, float width, float height, TextureRegion tr, int gameidx) {
		super(x, y, width, height, false, tr);
		gameIdx = gameidx;
	}
	
	public void setAd(TextureRegion tr){
		super.sprite.setRegion(tr);
	}
	
	public void setAd(int x, int y, int w, int h, TextureRegion tr){
		super.sprite.setRegion(tr, x, y, w, h);
	}
	
	public boolean touched(Vector3 point){
		
		if(super.visible){
			if(OverlapTester.pointInRectangle(super.getRectangle(), point.x, point.y)){
				super.visible = false;
				stateTime = 0;
				return true;
			}
		}
		return false;
	}
	
	public void update(float deltaTime){
		
		if(super.visible){
			stateTime+=deltaTime;
			if(stateTime >= displayTime){
				super.visible = false;
				stateTime = 0;
			}
		}
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		update(deltaTime);
		if(super.visible){
			super.draw(sb);
		}
	}

}
