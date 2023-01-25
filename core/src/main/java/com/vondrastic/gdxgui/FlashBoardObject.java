package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class FlashBoardObject extends GuiObject {
	String GAMEINFO = "Game Info";
	String PAYOUTINFO = "Payout Info";
	
	PatternGroup PG = null;
	Flashboard75 FB = null;
	private List<Byte> oneAwayList = new ArrayList<Byte>();
	
	public FlashBoardObject(float x, float y, float w, float h, boolean visable, TextureRegion t, Flashboard75 fb) {
		super(x, y, w, h, visable, t);
		if(fb == null){
			FB = new Flashboard75();
		}else{
			FB = fb;
		}
	}
	
	public FlashBoardObject(float x, float y, boolean visable, TextureRegion t, Flashboard75 fb) {
		super(x, y, t.getRegionWidth(), t.getRegionHeight(), visable, t);
		if(fb == null){
			FB = new Flashboard75();
		}else{
			FB = fb;
		}
	}
	
	public boolean TouchedCell(Vector3 point){
		Rectangle rect;
		
		for(int c = 0; c<15; c++){
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				// Invert Y
				rect = GetCellRectangle(c,y);
				if(OverlapTester.pointInRectangle(rect , point.x, point.y )){
					byte nbr = (byte)((r*15)+(c+1));
					if(FB.isCalled(nbr)){
						FB.removeBall(nbr);
					}else{
						FB.addBall(nbr);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private Rectangle GetCellRectangle(int x, int y){
		
		float heightoffset = 0;//42;
		float widthoffset = (float)(super.getWidth() / 16);
		
		float cellwidth = ((super.getWidth() - widthoffset ) / (float)15);
		float cellheight = ((super.getHeight() - heightoffset) / (float)5);
		
		return new Rectangle(super.getX() + widthoffset + (x * cellwidth) , super.getY() + (y * cellheight), cellwidth, cellheight);	

	}
	
	public void SetFlasboard75(Flashboard75 fb){
		FB = fb;
	}
	
	public void SetPatternGroup(PatternGroup pg){
		PG = pg;
	}
	
	public void Reset(){
		oneAwayList.clear();
		FB.clear();
	}
	
	public void ClearOneAway(){
		oneAwayList.clear();
	}
	
	public boolean IsOneAway(byte n){
		for(int i=0; i<oneAwayList.size(); i++){
			if(oneAwayList.get(i) == n){
				return true;
			}
		}
		return false;
	}
	
	public boolean AddOneAway(byte n){
		
		for(int i=0; i<oneAwayList.size(); i++){
			if(oneAwayList.get(i) == n){
				return false;
			}
		}
		oneAwayList.add(n);
		return true;
	}
	
	public void Draw(SpriteBatch sb, float deltatime){

		Rectangle rect;
		Rectangle rect_screen;
		if(super.moving){
			super.update(deltatime);
		}
		
		rect = super.sprite.getBoundingRectangle();
		rect_screen = new Rectangle(-(Assets.screenwidth / 2), -(Assets.screenheight / 2), Assets.screenwidth, Assets.screenheight);
		// Do not draw if off the screen
		if(OverlapTester.overlapRectangles(rect, rect_screen) == false){
			return;
		}
		
		sb.enableBlending();
		super.sprite.draw(sb);
		
		for(int c = 0; c<15; c++){
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				// Invert Y
				rect = GetCellRectangle(c,y);
				byte nbr = (byte)((r*15)+(c+1));
				
				if(FB.isCalled(nbr)){
					if(FB.LASTBALL == nbr){
						String sNbr = Byte.toString(nbr);
						Utils.getTextBoundsWidth(Assets.font32, sNbr);
						float msgWidth = Utils.getTextBoundsWidth(Assets.font32, sNbr);
						float msgHeight = Utils.getTextBoundsHeight(Assets.font32, sNbr);
						Assets.font32.setColor(0, 0, 0, 1);
						
						sb.setColor(1, 1, 1, 1);
						if(FB.LASTBALL > 0 && FB.LASTBALL < 16){
							sb.draw(Assets.bingoballs.get(0), -(Assets.screenwidth / 2) + 20,(Assets.screenheight / 2) - 225,200,200);
							Assets.font32.draw(sb, sNbr ,-(Assets.screenwidth / 2) - msgWidth + 132 ,(Assets.screenheight / 2) - 225 - msgHeight + 130 );
						}else if(FB.LASTBALL > 15 && FB.LASTBALL < 31){
							sb.draw(Assets.bingoballs.get(1), -(Assets.screenwidth / 2) + 20,(Assets.screenheight / 2) - 225,200,200);
							Assets.font32.draw(sb, sNbr ,-(Assets.screenwidth / 2) - msgWidth + 132 ,(Assets.screenheight / 2) - 225 - msgHeight + 130 );
						}else if(FB.LASTBALL > 30 && FB.LASTBALL < 46){
							sb.draw(Assets.bingoballs.get(2), -(Assets.screenwidth / 2) + 20,(Assets.screenheight / 2) - 225,200,200);
							Assets.font32.draw(sb, sNbr ,-(Assets.screenwidth / 2) - msgWidth + 132 ,(Assets.screenheight / 2) - 225 - msgHeight + 130 );
						}else if(FB.LASTBALL > 45 && FB.LASTBALL < 61){
							sb.draw(Assets.bingoballs.get(3), -(Assets.screenwidth / 2) + 20,(Assets.screenheight / 2) - 225,200,200);
							Assets.font32.draw(sb, sNbr ,-(Assets.screenwidth / 2) - msgWidth + 132 ,(Assets.screenheight / 2) - 225 - msgHeight + 130 );
						}else if(FB.LASTBALL > 60 && FB.LASTBALL < 76){
							sb.draw(Assets.bingoballs.get(4), -(Assets.screenwidth / 2) + 20,(Assets.screenheight / 2) - 225,200,200);
							Assets.font32.draw(sb, sNbr ,-(Assets.screenwidth / 2) - msgWidth + 132 ,(Assets.screenheight / 2) - 225 - msgHeight + 130 );
						}
						Assets.font32.setColor(1, 1, 1, 1);
						sb.setColor(0, 1, 0, 1);
					}else{
						sb.setColor(1, 0, 0, 1);
					}
					sb.draw(Assets.cardnbr_region.get(nbr - 1), rect.x , rect.y, rect.width, rect.height);
				}else{
					if(IsOneAway(nbr)){
						sb.setColor(1, 1, 1, 1.0f);
						sb.draw(Assets.oneaway_region.get(0), rect.x-16, rect.y-12, rect.width+32, rect.height+32);
						sb.setColor(0, 0, 0.7f, 0.80f);
						sb.draw(Assets.cardnbr_region.get(nbr - 1), rect.x+8 , rect.y+8, rect.width - 16, rect.height -16);
					}else{
						sb.setColor(0, 0, 0, 0.5f);
						sb.draw(Assets.cardnbr_region.get(nbr - 1), rect.x , rect.y, rect.width, rect.height);
					}
				}
			}
		}
		sb.setColor(1, 1, 1, 1);
	}

}
