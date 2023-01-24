package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// Copied 
public class BingoBallCam extends GuiObject{
	TextureRegion Filler;
	TextureRegion Charger;
	float statetime = 0;
	float updatetime = 240;
	boolean isCharging = false;
	float batteryPct = 0;
	NativeFunctions NF;
	
	public BingoBallCam(NativeFunctions nf, float x, float y, boolean visable, TextureRegion t, TextureRegion filler, TextureRegion charger) {
		super(x, y, visable, t);
		
		Filler = filler;
		Charger = charger;
		NF = nf;
		isCharging = NF.getBatteryCharging();
		batteryPct = NF.getBatteryLevel();
	}
	
	public void update(float deltatime){
		statetime += deltatime;
		if(statetime >= updatetime){
			// get new battery state
			isCharging = NF.getBatteryCharging();
			batteryPct = NF.getBatteryLevel();
			statetime = 0; // Reset State; 
		}
	}
	
	public void draw(SpriteBatch SB, float deltatime){
		update(deltatime);
		if(super.visible){
			SB.setColor(1, 1, 1, 1);
			super.sprite.draw(SB);
			if(batteryPct >0){
				SB.draw(Filler, super.getX() + 17, super.getY() + 11, Filler.getRegionWidth(), Filler.getRegionHeight() * batteryPct);
			}//TODO fix images so this works better.
			if(isCharging){
				SB.setColor(1, 0, 0, 1);
				SB.draw(Charger, super.getX(), super.getY());
			}
			SB.setColor(1, 1, 1, 1);
		}
	}
	

}
