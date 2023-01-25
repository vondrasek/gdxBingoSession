package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;

import java.util.ArrayList;
import java.util.List;

public class MenuItemSelectObject extends GuiObject{
	
	float X;
	float Y;
	float W;
	float H;
	
	GuiObjectButton Accept; 
	GuiObjectButton  Cancel;
	int NbrOfPages = 1;
	private List<TextureRegion> Items = null;
	private List<String> ItemsText = null;
	private List<Rectangle> ItemRects = null;
	float StateTime = 0;
	float OpenDurration = 1;
	boolean Opening = false;
	GameInterface GameEvent;
	private Rectangle ItemBounds;
	private int CurrentPage = 0;
	private int TotalPages = 0;
	private int itemSpacer = 5;
	private int itemSize = 64;
	int itemsX = 0;
	int itemsY = 0;
	private String Title = "ItemSelectMenu";
	private String Footer = "Page :";
	int ItemSelectedDown = 0;
	int ItemSelected = 0;
	MENU_CMD ItemSelectedCmd; 
	//TODO: add dauber selcted area to draw the one selected in the box;
	public MenuItemSelectObject(GameInterface buttonevent, float x, float y, float w, float h, TextureRegion bg, TextureRegion accept, TextureRegion cancel, MENU_CMD a_mc, MENU_CMD c_mc,MENU_CMD s_mc, String title, boolean visable){
		super(x,y,w,h,visable,bg);
		X=x;
		Y=y;
		W=w;
		H=h;
		Title = title;
		ItemBounds = new Rectangle(x + 5, y + accept.getRegionHeight(), w -10, h - ( accept.getRegionHeight()*2) );
		itemsX = (int) (ItemBounds.width/(itemSize + itemSpacer));
		itemsY = (int) (ItemBounds.height/(itemSize + itemSpacer));
		ItemSelectedCmd = s_mc;
		Items = new ArrayList<TextureRegion>();
		ItemsText = new ArrayList<String>();
		ItemRects = new ArrayList<Rectangle>();
		Items.clear();
		ItemsText.clear();
		
		// TODO: Hack to try and get around the arraylist bug on items doubling in size. 
		if(Items.size()>0){
			for(int i = 0; i<Items.size(); i++){
				Items.remove(i);
			}
		}
		
		ItemRects.clear();
		if(ItemRects.size()>0){
			for(int i = 0; i<ItemRects.size(); i++){
				ItemRects.remove(i);
			}
		}
		
		GameEvent = buttonevent;
		Accept =  new GuiObjectButton(X + W - accept.getRegionWidth(), Y, accept.getRegionWidth(), accept.getRegionHeight(), 0, "", accept, accept, GuiObjectButton.TYPE.ACTION, a_mc, a_mc);
		Cancel = new GuiObjectButton(X, Y, cancel.getRegionWidth(), cancel.getRegionHeight(), 0, "", cancel, cancel, GuiObjectButton.TYPE.ACTION, c_mc, c_mc);
	}
	
	public void AddItem(TextureRegion item){
		Items.add(item);
	}
	
	public void AddItem(TextureRegion item, String txt){
		Items.add(item);
		ItemsText.add(new String(txt));
		
	}
	
	public void AddDone(){
		ItemLayout();
	}
	
	public int GetItemSelected(){
		return ItemSelected;
	}
	
	public void PageRight(){
		int PageItems = (itemsX * itemsY * (CurrentPage + 1));
		if(PageItems < Items.size()){
			// Move all items 
			for(int i = 0; i< Items.size(); i++){
				ItemRects.get(i).x = ItemRects.get(i).x - ItemBounds.width; 
			}
			CurrentPage++;
		}
	}
	
	public void PageLeft(){
		
		if(CurrentPage > 0){
			// Move all items 
			for(int i = 0; i< Items.size(); i++){
				ItemRects.get(i).x = ItemRects.get(i).x + ItemBounds.width; 
			}
			CurrentPage--;
		}
		
	}
	
	public void update(float DeltaTime){
		StateTime+=DeltaTime;
	}
	
	public boolean touched(Vector3 point){
		
		if(super.touched(point)){
			if(Accept.touched(point)){
				Accept.touchDown();
				super.visible = false;
				GameEvent.GameMessage(Accept.GetCmdDn());
			}
			if(Cancel.touched(point)){
				Cancel.touchDown();
				super.visible = false;
				GameEvent.GameMessage(Cancel.GetCmdDn());
			}
			for(int i=0; i<Items.size(); i++){
				if(OverlapTester.pointInRectangle(ItemRects.get(i), point.x, point.y)){
					// Return item selected
					ItemSelectedDown = i;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean touchedUP(Vector3 point){
		for(int i=0; i<Items.size(); i++){
			if(OverlapTester.pointInRectangle(ItemRects.get(i), point.x, point.y)){
				// Return item selected
				if(ItemSelectedDown == i){
					ItemSelected = i;
					GameEvent.GameMessage(ItemSelectedCmd, ItemSelected);
					return true;
				}
			}
		}
		return false;
	}
	
	public void OpenAnimation(){
		Opening = true;
		StateTime = 0;
	}
	
	private void ItemLayout(){
		
		int i=0;
		double a = (double)Items.size() / (double)(itemsX * itemsY);
		TotalPages = (int)Math.ceil(a);
		ItemRects.clear();
		for(int x=0; x<(Items.size()); x++){
			for(int y=0; y<itemsY; y++){
				if(i < Items.size()){
					ItemRects.add(new Rectangle(ItemBounds.x + ((itemSize+itemSpacer) * x), ItemBounds.y + ((itemSize+itemSpacer) * y), itemSize, itemSize));
					//Items.get(i).setRegion(ItemBounds.x + ((itemSize+itemSpacer) * x), ItemBounds.y + ((itemSize+itemSpacer) * y), itemSize, itemSize);
					i++;
				}else{
					return;
				}
			}
		}
	}
	
	private void drawItems(SpriteBatch SB){
		for(int i=0; i<Items.size(); i++){
			if(OverlapTester.overlapRectangles(ItemRects.get(i), ItemBounds) ){
				if(ItemRects.get(i).x >= ItemBounds.x && ItemRects.get(i).x <= ItemBounds.x + ItemBounds.getWidth() - ItemRects.get(i).getWidth()){
					if(i == ItemSelected){
						SB.setColor(1, 1, 1, 1);
						SB.draw(Items.get(i), ItemRects.get(i).x,ItemRects.get(i).y, ItemRects.get(i).width, ItemRects.get(i).height);	
					}else{
						SB.setColor(1, 1, 1, 0.70f);
						SB.draw(Items.get(i), ItemRects.get(i).x,ItemRects.get(i).y, ItemRects.get(i).width, ItemRects.get(i).height);	
					}
				}
			}
		}
		SB.setColor(1, 1, 1, 1);
	}
	
	public void drawTitle(SpriteBatch SB){
	
		float msgWidth = Utils.getTextBoundsWidth(Assets.font32, Title);
		float msgHeight = Utils.getTextBoundsHeight(Assets.font32, Title);
		Assets.font32.setColor(1, 1, 1, 1);
		Assets.font32.draw(SB, Title , ItemBounds.getX() + (ItemBounds.getWidth() / 2) - (msgWidth / 2)  , ItemBounds.getY() + msgHeight + ItemBounds.getHeight());
		// Draw the Dauber currently selected and in use:
		SB.setColor(1, 1, 1, 1);
		SB.draw(Items.get(ItemSelected), ItemBounds.getX() + (ItemBounds.getWidth() / 2) + (msgWidth/2) + 12 , ItemBounds.getY() +  ItemBounds.getHeight() - msgHeight , ItemRects.get(ItemSelected).width, ItemRects.get(ItemSelected).height);	
 
	}
	
	public void drawFooter(SpriteBatch SB){
		String S = Footer + (CurrentPage + 1) + " of " + TotalPages;
		float msgWidth = Utils.getTextBoundsWidth(Assets.font32, S);
		float msgHeight = Utils.getTextBoundsHeight(Assets.font32, S);
		Assets.font32.setColor(1, 1, 1, 1);
		Assets.font32.draw(SB, S , ItemBounds.getX() + (ItemBounds.getWidth() / 2) - (msgWidth / 2)  , ItemBounds.getY() - msgHeight);
	
	}
	
	public void draw(SpriteBatch SB, float DeltaTime){
		update(DeltaTime);
		if(Opening){
			if(StateTime >= OpenDurration){
				super.setSize(W,H);
				Opening = false;
				super.visible = true;
			}else{
				super.setSize(W, H * (StateTime / OpenDurration));
				super.draw(SB,0.8f);
			}
		}
		if(super.visible){
			
			super.draw(SB,0.8f);
			Accept.draw(SB, DeltaTime);
			Cancel.draw(SB, DeltaTime);
			drawItems(SB);
			drawTitle(SB);
			drawFooter(SB);
			
		}
	}
	
}
