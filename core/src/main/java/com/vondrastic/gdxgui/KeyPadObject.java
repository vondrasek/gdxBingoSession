package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;

public class KeyPadObject extends GuiObject{
	private String txt = "";
	private List<GuiObjectButton> GOB = new ArrayList<GuiObjectButton>();
	private GuiObjectButton InputButton;
	private GuiObjectButton EnterButton;
	private TextureRegion DN;
	private TextureRegion UP;
	private float Alpha = 1.0f;
	
	private int minInputValue = 0;
	private int maxInputValue = 75;
	private int maxInputLength = 2;
	
	
	//TODO Center Keypad. Draw BG sprite, 
	public KeyPadObject(float x, float y, float w, float h, boolean visable, TextureRegion bg, TextureRegion up, TextureRegion dn, float alpha) {
		super(x, y, w, h, visable, bg);
		UP = up;
		DN = dn;
		Alpha = alpha;
		CreatePhoneStyleButtons();
	}
	
	public void SetLoginMode(){
		minInputValue = 0;
		maxInputValue = 9999999;
		maxInputLength = 7;	
		InputButton.setSize(400, InputButton.getHeight());
	}
	
	public void SetBallEnterMode(){
		minInputValue = 0;
		maxInputValue = 75;
		maxInputLength = 2;	
		InputButton.setSize(128, InputButton.getHeight());
	}
	// Move base up
	public void Show(){
		
	}
	// Move Base down
	public void Hide(){
		
	}
	
	public void MoveToTouch(float x, float y){
		//TODO. Fix so it doe snot jump off screen
		if( x - (super.getWidth()/2) < -(Assets.screenwidth/2)){ 
			x = -(Assets.screenwidth/2) + (super.getWidth()/2);
		}
		
		if(x  + (super.getWidth()/2) > (Assets.screenwidth/2)){ 
			x = (Assets.screenwidth/2) - (super.getWidth()/2);
		}
		
		if( y - (super.getHeight()/2) < -(Assets.screenheight/2)){ 
			y = -(Assets.screenheight/2) + (super.getHeight()/2);
		}
		
		if(y  + (super.getHeight()/2) > (Assets.screenheight/2)){ 
			y = (Assets.screenheight/2) - (super.getHeight()/2);
		}
		
		
		//center
		x = x - (super.getWidth()/2);
		y = y - (super.getHeight()/2);
		
		//x = (x - (super.getW()/2));
		super.setPosition(x, y);
		CreatePhoneStyleButtons();
	}
	
	private void CreateFlashboardStyleButtons(){
		float x; float y; float w; float h;
		GOB.clear();
		x = super.getX();
		y = super.getY();
		w = 128;
		h = 100;
		int i = 1;
		for(int r=3; r>0; r--){
			for(int c=0; c<3; c++){
				GOB.add(new GuiObjectButton(x + (c*w), y+(h*r), w, h, 0, String.valueOf(i), UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION));
				i++;
			}
		}
		int c = 0;
		int r = 0;
		GOB.add(new GuiObjectButton(x + (c*w), y+(h*r), w, h, 0, String.valueOf(0), UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION));
		// Create Text InputBox
		c=1;
		//InputButton = new GuiObjectButton(x + (c*w), y+(h*r), w*2, h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		//InputButton = new GuiObjectButton(-(w/2), (Assets.screenheight/2) - h, w , h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		InputButton = new GuiObjectButton(x + (c*w), y+(h*4), w , h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		EnterButton = new GuiObjectButton(x + (c*w), y+(h*r), w*2, h, 0, "Enter", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		
	}
	
	private void CreatePhoneStyleButtons(){
		float x; float y; float w; float h;
		GOB.clear();
		x = super.getX();
		y = super.getY();
		w = 128;
		h = 100;
		int i = 1;
		for(int r=3; r>0; r--){
			for(int c=0; c<3; c++){
				GOB.add(new GuiObjectButton(x + (c*w), y+(h*r), w, h, 0, String.valueOf(i), UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION));
				i++;
			}
		}
		int c = 0;
		int r = 0;
		GOB.add(new GuiObjectButton(x + (c*w), y+(h*r), w, h, 0, String.valueOf(0), UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION));
		// Create Text InputBox
		c=1;
		//InputButton = new GuiObjectButton(x + (c*w), y+(h*r), w*2, h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		//InputButton = new GuiObjectButton(-(w/2), (Assets.screenheight/2) - h, w , h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		InputButton = new GuiObjectButton(x + (c*w), y+(h*4), w , h, 0, "", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		EnterButton = new GuiObjectButton(x + (c*w), y+(h*r), w*2, h, 0, "Enter", UP, DN, Alpha, GuiObjectButton.TYPE.ACTION, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
		
	}
	
	public String GetKeypadText(){
		return txt;
	}
	
	public int GetKeypadNbr(){
		if(txt == ""){
			return 0;
		}
		return Integer.parseInt(txt);
	}
	
	public boolean touched(Vector3 point){

		for(int i=0; i<10; i++){
			if(GOB.get(i).touched(point)){
				GOB.get(i).touchDown();
				return true;
			}
		}

		if(txt.length() > 0 && EnterButton.touched(point)){
			EnterButton.touchDown();
			return true;
		}
		return false;
	}
	
	public boolean touchedUP(Vector3 point){
		for(int i=0; i<10; i++){
			if(GOB.get(i).clicked(point)){
				txt = txt + GOB.get(i).GetText();//String.valueOf(i);
				if(txt.length()>maxInputLength){
					//txt = txt.substring(1,3);
					txt = GOB.get(i).GetText();
				}
				if(Integer.parseInt(txt) == minInputValue || Integer.parseInt(txt) > maxInputValue){
					txt = "";
				}
				//InputButton.SPRITE.setX(GOB.get(i).SPRITE.getX());
				InputButton.setText(txt);
				return true;
			}
		}
		
		if(txt.length() > 0 && EnterButton.clicked(point)){
			return true;
		}
		return false;
	}
	
	public boolean Enter(Vector3 point){
		if(EnterButton.clicked(point)){
			return true;
		}
		return false;
	}
	
	public void reset(){
		txt = "";
		InputButton.setText(txt);
	}
	
	public void draw(SpriteBatch sb, float deltatime){
		
		if(super.visible){
			super.update(deltatime);
			for(int i=0; i<10; i++){
				GOB.get(i).draw(sb, deltatime);
			}

			InputButton.draw(sb, deltatime);
			EnterButton.draw(sb, deltatime);
		}
	}

}
