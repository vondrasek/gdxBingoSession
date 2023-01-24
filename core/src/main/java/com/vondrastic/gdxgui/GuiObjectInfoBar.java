package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GuiObjectInfoBar extends GuiObject{

	private List<TextMessage> msgList = new ArrayList<TextMessage>();
	private float stateTime = 0;
	private int currentIdx = 0;
	
	public GuiObjectInfoBar(float x, float y, float w, float h, boolean visable, TextureRegion t, String textMsg, int durration, int repeat) {
		super(x, y, w, h, visable, t);
		addMessage(textMsg, durration, repeat);
	}
	
	public void addMessage(String msg,int durration, int repeat){
		msgList.add(new TextMessage(msg, durration, repeat));
	}
	
	public void clearMessages(){
		msgList.clear();
		currentIdx = 0;
		stateTime = 0;
	}
	
	public void update(float deltaTime){
		stateTime += deltaTime;
	}
	
	//Manage Messages in que
	private void nextMessage(){
		
		if(msgList.get(currentIdx).displayCnt > 0 ){
			msgList.get(currentIdx).displayedCnt++;
			if(msgList.get(currentIdx).displayedCnt >= msgList.get(currentIdx).displayCnt){
				msgList.remove(currentIdx);
				currentIdx --;
			}
		}
		currentIdx++;
		if(currentIdx >= msgList.size()){
			currentIdx = 0;
		}
		
	}
	
	private String getMessage(){
		if(msgList.size() > 0){
			if(stateTime >= msgList.get(currentIdx).displayTime){
				nextMessage();
				stateTime = 0;
			}
		}
		
		if(msgList.size()>0){
			return msgList.get(currentIdx).text;
		}else{
			stateTime = 0;
			return " ";
		}
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		if(super.visible){
			update(deltaTime);
			String text = getMessage();
			
			float msgWidth = Assets.font32.getBounds(text).width;
			float msgHeight = Assets.font32.getBounds(text).height;
			
			Assets.font32.setColor(1, 1, 1, 1);
			// Draw
			super.draw(sb);
			Assets.font32.draw(sb, text , super.getX() + (super.getWidth() / 2) - (msgWidth / 2)  , super.getY() + super.getHeight() - msgHeight );
		}
	}
	

}
