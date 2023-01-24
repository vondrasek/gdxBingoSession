package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;

public class SessionGameListObject extends GuiObject{
	private List<GameListItem> gameListItem;
	GuiObjectButton Accept; 
	GuiObjectButton  Cancel;
	
	GuiObjectButton ScrollUp; 
	GuiObjectButton ScrollDn;
	private int CurrentPage = 0;
	private int TotalPages = 0;
	private GameInterface GameEvent;
	
	String header = "Session Game List";
	String footer = "Total Games ";
	int itemHeight = 48;
	
	int ItemSelectedDown = 0;
	int ItemSelected = 0;
	
	boolean scrolling = false;
	float stateTime = 0;
	int itemOffset = 0;
	int viewableItems = 0;
	Rectangle listBounds = null;
	
	public SessionGameListObject(GameInterface buttonevent, float x, float y, float w, float h, boolean visable, TextureRegion t, int itemheight, TextureRegion accept, TextureRegion cancel, MENU_CMD a_mc, MENU_CMD c_mc, TextureRegion scrollup, TextureRegion scrolldn ) {
		super(x, y, w, h, visable, t);
		
		listBounds = new Rectangle(x,y+48,w,h-48*2);
		
		Accept =  new GuiObjectButton(x + w - accept.getRegionWidth(), y, 64, 64, 0, "", accept, accept, GuiObjectButton.TYPE.ACTION, a_mc, a_mc);
		Cancel = new GuiObjectButton(x, y, 64, 64, 0, "", cancel, cancel, GuiObjectButton.TYPE.ACTION, c_mc, c_mc);
		
		//ScrollUp =  new GuiObjectButton(x + w - accept.getRegionWidth(), y, accept.getRegionWidth(), accept.getRegionHeight(), 0, "", accept, accept, GuiObjectButton.TYPE.ACTION, a_mc, a_mc);
	    //ScrollDn = new GuiObjectButton(x, y, cancel.getRegionWidth(), cancel.getRegionHeight(), 0, "", cancel, cancel, GuiObjectButton.TYPE.ACTION, c_mc, c_mc);
		
		ScrollUp =  new GuiObjectButton(Accept.getX() -Accept.getWidth() , y, scrollup.getRegionWidth(), scrollup.getRegionHeight(), 0, "", scrollup, scrollup, GuiObjectButton.TYPE.ACTION, null, null);
	    ScrollDn = new GuiObjectButton(Cancel.getX() + Cancel.getWidth(), y, scrolldn.getRegionWidth(), scrolldn.getRegionHeight(), 0, "", scrolldn, scrolldn, GuiObjectButton.TYPE.ACTION, null, null);
	
		GameEvent = buttonevent;
		itemHeight = itemheight;
		gameListItem = new ArrayList<GameListItem>();
	}
	
	public void AddGameInfo(int gameIdx, String gameName, String patternName, int cardCnt ){
		
		gameListItem.add(new GameListItem(gameName, patternName, gameIdx, cardCnt));
		
	}
	
	private void drawItemText(SpriteBatch sb, String txt, float x, float y, Color c){
		
		float msgWidth = Assets.font32.getBounds(txt).width;
		float msgHeight = Assets.font32.getBounds(txt).height;
		Assets.font32.setColor(c);
		Assets.font32.draw(sb, txt , x ,y);
	
	}
	
	public boolean touched(Vector3 point){
		
		if(super.touched(point)){
			if(Accept.touched(point)){
				Accept.touchDown();
				super.visible = false;
				ItemSelectedDown = ItemSelected;
				GameEvent.GameMessage(Accept.GetCmdDn());
			}
			
			if(Cancel.touched(point)){
				Cancel.touchDown();
				super.visible = false;
				GameEvent.GameMessage(Cancel.GetCmdDn());
			}
			
			if(Cancel.touched(point)){
				Cancel.touchDown();
				super.visible = false;
				GameEvent.GameMessage(Cancel.GetCmdDn());
			}
			
			if(ScrollUp.touched(point)){
				scrollUp();
				return true;
			}
			
			if(ScrollDn.touched(point)){
				scrollDn();
				return true;
			}
			
			for(int i=0 + itemOffset; i< gameListItem.size(); i++){
				float y = (listBounds.getY() + listBounds.getHeight()) - ((i-itemOffset) * itemHeight);
				float x = listBounds.getX();
				Rectangle r = new Rectangle(x,y - itemHeight,listBounds.getWidth(), itemHeight);
				if(y < listBounds.getY()){
					viewableItems = (i-itemOffset)+1;
					break;
				}
				if(OverlapTester.pointInRectangle(r, point.x, point.y)){
					ItemSelected = i;
					return true;
				}	
			}
			return true;
		}
		return false;
	}
	
	public int getItemSelected(){
		return ItemSelectedDown;
	}
	
	public void scrollUp(){
		
		if(itemOffset <= gameListItem.size() - viewableItems){
			itemOffset ++;
		}
	}
	
	public void scrollDn(){
		itemOffset --;
		if(itemOffset < 0){
			itemOffset = 0;
		}
	}
	
	public void update(float deltaTime){
		stateTime += deltaTime;
	}
	
	public void drawHeader(SpriteBatch SB){
		
		float msgWidth = Assets.font32.getBounds(header).width;
		float msgHeight = Assets.font32.getBounds(header).height;
		Assets.font32.setColor(1, 1, 1, 1);
		Assets.font32.draw(SB, header , listBounds.getX() + (listBounds.getWidth() / 2) - (msgWidth / 2)  , listBounds.getY() + msgHeight + listBounds.getHeight());
	
	}
	
	public void drawFooter(SpriteBatch SB){
		String S = footer + gameListItem.size();
		float msgWidth = Assets.font32.getBounds(S).width;
		float msgHeight = Assets.font32.getBounds(S).height;
		Assets.font32.setColor(1, 1, 1, 1);
		Assets.font32.draw(SB, S , listBounds.getX() + (listBounds.getWidth() / 2) - (msgWidth / 2)  , listBounds.getY() );
	
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		Color c = new Color(1,1,1,1);
		
		if(super.visible){	
			super.draw(sb, 0.80f);
			
			Accept.draw(sb);
			Cancel.draw(sb);
			ScrollUp.draw(sb);
			ScrollDn.draw(sb);
			
			drawHeader(sb);
			drawFooter(sb);
			for(int i=0 + itemOffset; i< gameListItem.size(); i++){
				float y = (listBounds.getY() + listBounds.getHeight()) - ((i-itemOffset) * itemHeight);
				float x = listBounds.getX();
				if(y < listBounds.getY()){
					viewableItems = (i-itemOffset)+1;
					break;
				}
				
				if(i == ItemSelectedDown){
					c.r = 0; c.g=1; c.b = 0; c.a = 1;
				}else if(i == ItemSelected){
					c.r = 1; c.g=0; c.b = 0; c.a = 1f;
				}else{
					c.r = 0.5f; c.g=0.5f; c.b = 0.5f; c.a = 1f;
				}
				drawItemText(sb, "#" + gameListItem.get(i).gameIdx + " - " + gameListItem.get(i).gameName + " ("+ gameListItem.get(i).cardCnt + ") ", x, y, c );
			}
		}
	}

}
