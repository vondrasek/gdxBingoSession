package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;

public class GuiObjectButton extends GuiObject {

	static enum TYPE {ACTION, TOGGLE};
	
	float stateTime;
	boolean stateDn = false; // Up or Down
	TextureRegion dn;
	String text = "button";
	Color textColor = new Color(1,1,1,1);
	float Alpha = 1.0f;
	String CMD = "BUTTON";
	TYPE ButtonType = TYPE.ACTION;
	MENU_CMD BUTTON_CMD_UP;
	MENU_CMD BUTTON_CMD_DN;
	
	
	public GuiObjectButton (float x, float y,float w, float h, float layer, String txt, TextureRegion upRegion, TextureRegion dnRegion, TYPE buttontype, MENU_CMD CmdUp, MENU_CMD CmdDn ) {
		super(x, y, w, h, true, upRegion);
		text = txt;
		dn = dnRegion;
		stateTime = 0;
		ButtonType = buttontype;
		BUTTON_CMD_UP = CmdUp;
		BUTTON_CMD_DN = CmdDn;
	}
	
	public GuiObjectButton (float x, float y,float w, float h, float layer, String txt, TextureRegion upRegion, TextureRegion dnRegion, float alpha, TYPE buttontype, MENU_CMD CmdUp, MENU_CMD CmdDn ) {
		super(x, y, w, h, true, upRegion);
		text = txt;
		dn = dnRegion;
		stateTime = 0;
		Alpha = alpha;
		ButtonType = buttontype;
		BUTTON_CMD_UP = CmdUp;
		BUTTON_CMD_DN = CmdDn;
	}
	public GuiObjectButton (float x, float y, TextureRegion upRegion, TextureRegion dnRegion, float alpha, TYPE buttontype, MENU_CMD CmdUp, MENU_CMD CmdDn ) {
		super(x, y, upRegion.getRegionWidth(), upRegion.getRegionHeight(), true, upRegion);
		text = "";
		dn = dnRegion;
		stateTime = 0;
		Alpha = alpha;
		ButtonType = buttontype;
		BUTTON_CMD_UP = CmdUp;
		BUTTON_CMD_DN = CmdDn;
	}
	
	public GuiObjectButton (float x, float y, TextureRegion upRegion, TextureRegion dnRegion, float alpha, boolean visable, TYPE buttontype, MENU_CMD CmdUp, MENU_CMD CmdDn ) {
		super(x, y, upRegion.getRegionWidth(), upRegion.getRegionHeight(), visable, upRegion);
		text = "";
		dn = dnRegion;
		stateTime = 0;
		Alpha = alpha;
		ButtonType = buttontype;
		BUTTON_CMD_UP = CmdUp;
		BUTTON_CMD_DN = CmdDn;
	}
	
	public GuiObjectButton (float x, float y, float w, float h, TextureRegion upRegion, TextureRegion dnRegion, float alpha, boolean visable, TYPE buttontype, MENU_CMD CmdUp, MENU_CMD CmdDn ) {
		super(x, y, w, h, visable, upRegion);
		text = "";
		dn = dnRegion;
		stateTime = 0;
		Alpha = alpha;
		ButtonType = buttontype;
		BUTTON_CMD_UP = CmdUp;
		BUTTON_CMD_DN = CmdDn;
	}
	
	public String GetText(){
		return text;
	}
	public MENU_CMD GetCmdUp(){
		return BUTTON_CMD_UP;
	}
	
	public MENU_CMD GetCmdDn(){
		return BUTTON_CMD_DN;
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
	
	public void setTextColor(Color c){
		textColor.r = c.r;
		textColor.g = c.g;
		textColor.b = c.b;
		textColor.a = c.a;
		
	}
	public void draw(SpriteBatch SB, float deltaTime){
		
		super.update(deltaTime);
		
		if(super.visible){
			SB.setColor(1, 1, 1, Alpha);
			if(stateDn){
				SB.draw(dn, super.sprite.getX(), super.sprite.getY(), super.sprite.getWidth(), super.sprite.getHeight());
			}else{
				super.sprite.setColor(1, 1, 1, Alpha);
				super.sprite.draw(SB);
			}
			if(text.length() > 0){
			// float msgWidth = Assets.font32.getBounds(text).width;
				// FontFix
				float msgWidth = Utils.getTextBoundsWidth(Assets.font32, text);
			Assets.font32.setColor(textColor); // Added New Must Test

			Assets.font32.draw(SB, text, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight() * 3/4));
		
			}
			SB.setColor(1, 1, 1, 1);
		}
		
	}

}
