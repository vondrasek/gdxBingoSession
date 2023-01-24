package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;

public class ActionBarObject {
	public static enum ACTIONBAR_POSITION {LEFT,RIGHT,TOP,BOTTOM}
	
	private ACTIONBAR_POSITION POS = ACTIONBAR_POSITION.BOTTOM;
	private List<GuiObjectButton> CHILDBUTTONS = new ArrayList<GuiObjectButton>(); 
	private GuiObjectButton PARENTBUTTON; 
	
	private int W = 64;
	private int H = 64;
	private int X = 0;
	private int Y = 0;
	private float StateTime = 0;
	private float AutoHideTime = 30; // Number of seconds before it autohides
	
	private Timer SHOWTIMER;
	private float SHOWDELAY = 500;
	private boolean SHOWING = false;
	private int SHOWIDX = 0;
	
	public boolean VISABLE = false;
	private boolean OPEN = false;
	// Events
	GameInterface GameEvent;
	
	public ActionBarObject(GameInterface buttonevent, ACTIONBAR_POSITION pos,int x, int y, int w, int h, TextureRegion ParentUP, TextureRegion ParentDN){
		POS = pos;
		X=x;
		Y=y;
		W=w;
		H=h;
		CreateParentButton(ParentUP, ParentDN);
		GameEvent = buttonevent;
	}
	
	public ActionBarObject(GameInterface buttonevent, ACTIONBAR_POSITION pos,int x, int y, TextureRegion ParentUP, TextureRegion ParentDN){
		POS = pos;
		X=x;
		Y=y;
		W=ParentUP.getRegionWidth();
		H=ParentUP.getRegionHeight();
		CreateParentButton(ParentUP, ParentDN);
		GameEvent = buttonevent;
	}
	
	public void PopUpChildren(long interval){
		
		long delay = 10;
		if(SHOWING == false){
			SHOWTIMER = new Timer();
			SHOWING = true;
			SHOWIDX = 0;
			SHOWTIMER.scheduleAtFixedRate(new TimerTask(){ public void run(){
				if(SHOWIDX  >= CHILDBUTTONS.size()){
					SHOWTIMER.cancel();
					SHOWING = false;
					OPEN = true;
				}else{
					CHILDBUTTONS.get(SHOWIDX).visible = true;
					SHOWIDX++;
				}
				}}, delay, interval);
		}
	}
	
	public boolean touched(Vector3 point){
		resetStateTime();
		if(PARENTBUTTON.touched(point)){
			if(OPEN){
				HideChildren();
				OPEN = false;
			}else{
				PopUpChildren(100);
			}
			return true;
		}
		
		for(int i=0; i< CHILDBUTTONS.size(); i++){
			if(CHILDBUTTONS.get(i).visible){
				if(CHILDBUTTONS.get(i).touched(point)){
					// Raise event
					//TODO Add support for action buttons types
					if(CHILDBUTTONS.get(i).stateDn){
						CHILDBUTTONS.get(i).touchUp();
						GameEvent.GameMessage(CHILDBUTTONS.get(i).GetCmdUp());
					}else{
						CHILDBUTTONS.get(i).touchDown();
						GameEvent.GameMessage(CHILDBUTTONS.get(i).GetCmdDn());
					}
				
					return true;
				}
			}
		}
		return false;
	}
	// THis button pops up the other buttons when clicked and hides when clicked again or cancel
	public void CreateParentButton(TextureRegion UP, TextureRegion DN){
		PARENTBUTTON = new GuiObjectButton(X, Y, W, H, 0, "", UP, DN, GuiObjectButton.TYPE.TOGGLE, MENU_CMD.NOACTION, MENU_CMD.NOACTION);
	}
	// Creates Child Button and Adds to the Stack in order it was created
	public void CreateChildButton(TextureRegion UP, TextureRegion DN, MENU_CMD CmdUp, MENU_CMD CmdDn){
		int x = 0; 
		int y = 0;
		
		if(POS == ACTIONBAR_POSITION.BOTTOM){
			x = X + (H * (CHILDBUTTONS.size() + 2)) ; 
			y = Y ;//+ (H * (CHILDBUTTONS.size() + 1)) ;
			
		}else if(POS == ACTIONBAR_POSITION.TOP){
			x = X; 
			y = Y - (H * (CHILDBUTTONS.size() + 1));
			
		}
		
		CHILDBUTTONS.add(new GuiObjectButton((float)x , (float)y,(float)W, (float)H, UP, DN, 1.0f, false, GuiObjectButton.TYPE.TOGGLE, CmdUp, CmdDn));
	}
	
	public void ShowParrent(){
		PARENTBUTTON.visible = true;
		VISABLE = true;
		resetStateTime();
	}
	
	public void HideParrent(){
		PARENTBUTTON.visible = false;
		VISABLE = false;
		OPEN = false;
		HideChildren();
	}
	// Close
	public void HideChildren(){
		for(int i=0; i< CHILDBUTTONS.size(); i++){
			CHILDBUTTONS.get(i).visible = false;
		}
	}
	
	private void autoHideCheck(){
		if(VISABLE){
			if(StateTime >= AutoHideTime){
				HideParrent();
			}
		}
	}
	
	public void update(float DeltaTime){
		StateTime+=DeltaTime;
		autoHideCheck();
	}
	
	private void resetStateTime(){
		StateTime = 0;
	}
	
	public void draw(SpriteBatch SB, float deltaTime){
		update(deltaTime);
		if(VISABLE){
			SB.draw(Assets.menu_bg, PARENTBUTTON.getX(),PARENTBUTTON.getY(),W,H);
			PARENTBUTTON.draw(SB, deltaTime);
			for(int i=0; i<CHILDBUTTONS.size(); i++){
				if(CHILDBUTTONS.get(i).visible){
					SB.draw(Assets.menu_bg, CHILDBUTTONS.get(i).sprite.getX(), CHILDBUTTONS.get(i).sprite.getY(), W, H);
					CHILDBUTTONS.get(i).draw(SB, deltaTime);
				}
			}
			SB.enableBlending();
		}
	}
}
