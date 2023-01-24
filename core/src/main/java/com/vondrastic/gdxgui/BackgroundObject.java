package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// Copied 
public class BackgroundObject extends GuiObject{

	float statetime = 0;
	float updatetime = 240;
	
	public BackgroundObject(float x, float y, boolean visable, TextureRegion t) {
		super(x,y,Assets.screenwidth,Assets.screenheight,true,t);
	}
	
	public void update(float deltatime){
		statetime += deltatime;
		if(statetime >= updatetime){
			
		}
	}
	
	public void draw(SpriteBatch SB, float deltatime){
		update(deltatime);
		if(super.visible){
			SB.setColor(1, 1, 1, 1);
			super.draw(SB);
			
			//SB.draw(Assets.backgroundRegion, super.getX(), super.getY(),Assets.screenwidth, Assets.screenheight);
			
		}
	}
	

}
