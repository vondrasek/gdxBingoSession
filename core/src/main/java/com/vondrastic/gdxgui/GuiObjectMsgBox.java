package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;
import com.vondrastic.gdxgui.GuiObjectButton.TYPE;

public class GuiObjectMsgBox extends GuiObject{

	String title = "Message Box";
	String text = "Hello";
	float stateTime = 0;
	float timeOut = 10;
	GuiObjectButton yesButton = null;
	GuiObjectButton noButton = null;
	float buttonWidth = 80; 
	float buttonHeight = 64;
	GameInterface GameEvent;
	
	public GuiObjectMsgBox(GameInterface buttonevent, float x, float y, float w, float h, boolean visable, TextureRegion t, String Title, String Text, MENU_CMD cmd_yes, MENU_CMD cmd_no ) {
		super(x, y, w, h, visable, t);
		title = Title;
		text = Text;
		
		GameEvent = buttonevent;
		yesButton = new GuiObjectButton(x+w-buttonWidth, y, buttonWidth, buttonHeight, 0, "YES", t, t, TYPE.ACTION, cmd_yes, cmd_yes);
		noButton = new GuiObjectButton(x, y, buttonWidth, buttonHeight, 0, "NO", t, t, TYPE.ACTION, cmd_no, cmd_no);
		
	}
	// Override update
	public void update(float deltaTime){
		stateTime+=deltaTime;
		if(stateTime >= timeOut){
			hide();
		}
	}
	
	public void show(){
		if(super.visible == false){
			super.visible = true;
			stateTime = 0;
		}
	}
	
	public void hide(){
		if(super.visible){
			super.visible = false;
			stateTime = 0;
		}
	}
	
	public boolean touched(Vector3 point){
		if(super.touched(point)){
			if(yesButton.touched(point)){
				GameEvent.GameMessage(yesButton.GetCmdDn());
				return true;
			}else if(noButton.touched(point)){
				GameEvent.GameMessage(noButton.GetCmdDn());
				return true;
			}
		}
		return false;
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		if(super.visible){
			update(deltaTime);
			super.draw(sb);
			// Draw title
			Utils.DrawText(sb, Assets.font32, super.getX(), super.getY() + super.getHeight() - 50, super.getWidth(), 50, title);
			// draw message
			Utils.DrawText(sb, Assets.font32, super.getX(), super.getY() + super.getHeight() - 200, super.getWidth(), 50, text);
			// draw Yes / No buttons
			yesButton.draw(sb,deltaTime);
			noButton.draw(sb,deltaTime);
		}
	}

}
