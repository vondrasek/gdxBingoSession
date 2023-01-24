package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class BingoBallObject extends GuiObject {

	boolean stateDn = false; // Up or Down
	TextureRegion dn;
	String text = "75";
	
	public BingoBallObject(float x, float y, float w, float h, boolean visable, TextureRegion t) {
		super(x, y, w, h, visable, t);
		
	}
	
	public BingoBallObject(float x, float y, boolean visable, TextureRegion t) {
		
		super(x, y, t.getRegionWidth(), t.getRegionHeight(), visable, t);
		
	}
	
	public void setText(String txt){
		text = txt;
	}
	
	public void touchDown(){
		stateDn = true;
	}
	
	public void touchUp(){
		stateDn = false;
	}
	// -----------------------------------------------------------------------------------
	// Use on touch up to see if the button was in a down state first and then touched up
	// -----------------------------------------------------------------------------------
	public boolean clicked(Vector3 point){
		
		if(super.touched(point) == true && stateDn == true){
			stateDn = false;
			return true;
		}else{
			stateDn = false;
			return false;
		}
	
	}
	
	public void draw(SpriteBatch SB, float deltaTime){
		if(super.moving){
			super.update(deltaTime);
		}
		
		if(super.visible){
			if(stateDn){
				SB.draw(dn, super.sprite.getX(), super.sprite.getY(), super.sprite.getWidth(), super.sprite.getHeight());
			}else{
				super.sprite.draw(SB);
			}
			float msgWidth = Assets.font32.getBounds(text).width;
			Assets.font32.draw(SB, text, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight() * 3/4));
		}
	}

}
