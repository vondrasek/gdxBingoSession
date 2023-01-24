package com.vondrastic.gdxgui;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL10;
//import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BingoCardObject extends GuiObject{

	BingoCard CardReg = null;
	CardDbl75 CardDbl = null;
	//static PatternGroup PG = null;
	private Animation CardDabTR;
	private Animation CardOneAway;
	
	private ParticleEffectObject winEffect;
	private TextureRegion CardBaseTR = null;
	private boolean CardBaseTrCreated = false;
	private boolean poppedUp = false;
	private float poppedX = 0;
	private float poppedY = 0;
	private Rectangle homeBounds; // Where the card Comes to rest. 
	public boolean favorite = false; //TODO: Allow player to tag. Keeps card forward
	public int id = 0;
	
	public BingoCardObject(float x, float y, float w, float h, boolean visable, TextureRegion t, BingoCard card, int index, TextureRegion dauber) {
		super(x, y, w, h, visable, t);
		
		winEffect = new ParticleEffectObject(x, y, false, t, "data/effects/splash.p", "data/effects");
		
		CardReg = card;
		CardDbl = null;
		List<TextureRegion> tr = new ArrayList<TextureRegion>();
		tr.add(dauber); // DEfault Dauber
		CardDabTR = new Animation(30, tr);
		CardOneAway = new Animation(0.08f, Assets.oneaway_region);
		id = index;
	}
	
	public BingoCardObject(float x, float y, float w, float h, boolean visable, TextureRegion t, CardDbl75 card, int index, TextureRegion dauber) {
		super(x, y, w, h, visable, t);
		
		winEffect = new ParticleEffectObject(x, y, false, t, "data/effects/splash.p", "data/effects");
		
		CardReg = null;
		CardDbl = card;
		List<TextureRegion> tr = new ArrayList<TextureRegion>();
		tr.add(dauber); // DEfault Dauber
		CardDabTR = new Animation(30, tr);
		CardOneAway = new Animation(0.08f, Assets.oneaway_region);
		id = index;
	}
	
	public BingoCardObject(float x, float y, float w, float h, boolean visable, TextureRegion t, BingoCard card, Array<AtlasRegion> AnimatedDauber,int index) {
		super(x, y, w, h, visable, t);
		CardReg = card;
		List<TextureRegion> tr = new ArrayList<TextureRegion>();
		for(int i = 0; i< AnimatedDauber.size; i++){
			tr.add(AnimatedDauber.get(i));
		}
		CardDabTR = new Animation(0.10f, tr);
		CardOneAway = new Animation(0.08f, Assets.oneaway_region);
		id = index;
	}
	
	// If touch the free space the card pops up to center and then times out or returns if they touch it again. 
	
	public boolean TouchPopUp(Vector3 point){
		Rectangle rect;
		rect = GetCellRectangle(2,2);
		
		if(!poppedUp){
			if(OverlapTester.pointInRectangle(rect , point.x, point.y )){
				poppedUp = true;
				poppedX = super.getX();
				poppedY = super.getY();
				super.moveto(-super.getWidth()/2, -super.getHeight()/2, 1000);
				return true;
			}
		}else{
			if(OverlapTester.pointInRectangle(rect , point.x, point.y )){
				poppedUp = false;
				super.moveto(poppedX, poppedY, 1000);
				return true;
			}
		}
		return false;
	}
	
	public boolean PopUp(){
		
		if(!poppedUp){
				poppedUp = true;
				poppedX = super.getX();
				poppedY = super.getY();
				super.moveto(-super.getWidth()/2, -super.getHeight()/2, 1000);
				return true;
		}else{	
			poppedUp = false;
			super.moveto(poppedX, poppedY, 1000);
			return true;
		}
	}
	
	public boolean isPoppedUp(){
		return poppedUp;
	}
	
	public void SetPatternGroup(PatternGroup pg){
		CardReg.SetPatternGroup(pg);
	}
	
	public void SetDauberTexture(TextureRegion tr){
		List<TextureRegion> newtr = new ArrayList<TextureRegion>();
		newtr.add(tr);
		CardDabTR = new Animation(30, newtr);
	}
	//Support for animated daubers, 
	public void SetDauberTexture(List<TextureRegion> tr, float delay){
		CardDabTR = new Animation(delay, tr);
	}
	
	public void UpdateData(BingoCard Card, int index){
		CardReg = Card;
		id = index;
	}
	
	public void UpdateData(CardDbl75 Card, int index){
		CardDbl = Card;
		id = index;
	}
	
	public boolean TouchedCell(Vector3 point){
		Rectangle rect;
		
		for(int c = 0; c<5; c++){
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				// Invert Y
				rect = GetCellRectangle(c,y);
				if(OverlapTester.pointInRectangle(rect , point.x, point.y )){
					if(CardReg!=null){
						if(CardReg.isDaubed(c, r) == false){
							CardReg.daub(c, r);
							CardDabTR.reset();
						}else{
							CardReg.undaub(c, r);
						}
					}else{
						if(CardDbl.isDaubed(c, r) == false){
							CardDbl.daub(c, r);
							CardDabTR.reset();
						}else{
							CardDbl.undaub(c, r);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void Drag(float x, float y){
		// Do not allow the card to be dragged off screen. 
		//if((super.SPRITE.getX() + x) < -(Assets.screenwidth / 2 )){
		//	return;
		//}
		//if((super.SPRITE.getX() + x + super.SPRITE.getWidth()) >  (Assets.screenwidth / 2 )){
		//	return;
		//}
		//if((super.SPRITE.getY() + y) < -(Assets.screenheight / 2 )){
		//	return;
		//}
		//if((super.SPRITE.getY() + y + super.SPRITE.getHeight()) >  (Assets.screenheight / 2 )){
		//	return;
		//}
		
		super.setPosition(super.getX() + x, super.getY() + y);
	}
	
	private Rectangle GetCellRectangle(int x, int y){
		
		float heightoffset = (float)0.16 * super.getHeight();//42;
		float widthoffset = 0;
		
		float cellwidth = ((super.getWidth() - widthoffset ) / (float)5);
		float cellheight = ((super.getHeight() - heightoffset) / (float)5);
		
		return new Rectangle(super.getX() + (x * cellwidth) , super.getY() + (y * cellheight), cellwidth, cellheight);	


	}
	public void setSize(float w, float h){
		super.sprite.setSize(w, h);
	}
	
	public void draw(SpriteBatch sb, float deltatime){
		if(CardReg!=null){
			drawRegCard(sb,deltatime);
		}else{
			drawDblCard(sb,deltatime);
		}
	}
	
	private void drawDblCard(SpriteBatch sb, float deltatime){
		Rectangle rect;
		Rectangle rect_screen;
		//if(super.moving){
		super.update(deltatime);
		//}
		rect = super.getRectangle();
		rect_screen = new Rectangle(-(Assets.screenwidth / 2), -(Assets.screenheight / 2), Assets.screenwidth, Assets.screenheight);
		// Do not draw if off the screen
		if(OverlapTester.overlapRectangles(rect, rect_screen) == false){
			return;
		}
		
		int PatMask = 0;
		sb.enableBlending();
		//Draw Card
		super.draw(sb);
		//Draw ID and Score
		String s =  String.valueOf("Serial# " +  CardReg.getID() + " (" + CardReg.getLevel() + ")" + "           " + CardReg.SCORE + " Away");
		float msgWidth = Assets.font18.getBounds(s).width;
		float msgHeight = Assets.font18.getBounds(s).height;
		Assets.font18.setColor(0, 0, 0, 1);
		Assets.font18.draw(sb, s , super.getX() + (super.getWidth() / 2) - (msgWidth / 2)  , super.getY() + super.getHeight()  - (0.10f * (super.getHeight())));
		
		// For animated Daubing
		CardDabTR.update(deltatime);
		CardOneAway.update(deltatime);
		int i = 0;
		for(int c = 0; c<5; c++){
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				sb.setColor(1, 1, 1, 1);
				int d1 = CardDbl.getCellData_1(c, r);
				int d2 = CardDbl.getCellData_2(c, r);
				// Invert Y
				rect = GetCellRectangle(c,y);
				if(c == 2 && r == 2){
					// freespace
					if(CardDbl.isDaubed(c, r)){
						sb.setColor(1, 1, 1, (float)0.80);
						sb.draw(CardDabTR.getKeyFrame(0), rect.x , rect.y, rect.width, rect.height);
					}
				}else{
					if(CardDbl.PG != null){
						PatMask = (CardDbl.PG.getPatternGroupMask() & (1 << i));
						if(PatMask>0){
							sb.setColor(0, 0, 0, 1); // Make It Black
						}else{
							sb.setColor(0.5f, 0.5f, 0.5f, 0.5f); // Grey it out. 
						}
					}
					if(CardDbl.isDaubed(c, r) && PatMask > 0){
						sb.setColor(0, 0, 0, (float)0.80);
						sb.draw(Assets.cardnbr_region.get(d1 - 1), rect.x , rect.y + (rect.height/2), rect.width/2, rect.height/2);
						sb.draw(Assets.cardnbr_region.get(d2 - 1), rect.x + (rect.width/2) , rect.y, rect.width/2, rect.height/2);
						sb.setColor(1, 1, 1, (float)0.80);
						if(d1 == CardDbl.GetLastNbrDaubed() || d2 == CardDbl.GetLastNbrDaubed()){//if(LastMarkX == c && LastMarkY == r){
							sb.draw(CardDabTR.getKeyFrame(deltatime, Animation.ANIMATION_LOOPING), rect.x , rect.y, rect.width, rect.height);	
						}else{
							sb.draw(CardDabTR.getKeyFrame(0), rect.x , rect.y, rect.width, rect.height);			
						}
					}else{
						sb.draw(Assets.cardnbr_region.get(d1 - 1), rect.x , rect.y + (rect.height/2), rect.width/2, rect.height/2);
						sb.draw(Assets.cardnbr_region.get(d2 - 1), rect.x + (rect.width/2) , rect.y, rect.width/2, rect.height/2);
						
						if(CardDbl.IsOneAway((byte)d1) || CardDbl.IsOneAway((byte)d2)){
							sb.setColor(1, 1, 1, 0.80f);
							sb.draw(CardOneAway.getKeyFrame(deltatime, Animation.ANIMATION_LOOPING), rect.x , rect.y, rect.width, rect.height);			
						}
						//sb.draw(Assets.cardnbr_region.get(CardReg.getCellData(c, r) - 1),rect.x,  rect.y, rect.x,  rect.y,rect.width, rect.height, (float) 0.5,(float) 0.5, 0);// Assets.cardnbr_region.get(CardReg.getCellData(c, r) - 1)
					}
					
				}
				i++;
			}
		}
		if(CardDbl.IsWinner() && CardDbl.WINNER_FLAG == false){
			CardDbl.WINNER_FLAG = true;
			winEffect.visible = true;
			winEffect.start();
		}
		if(winEffect.visible){
			winEffect.setPosition(super.getX() + (super.getWidth()/2) - (winEffect.getWidth()/2), super.getY() + (super.getHeight()/2) - (winEffect.getHeight()/2));
			winEffect.draw(sb,deltatime);
		}
		// Draw Card ID and Status. 
		sb.setColor(1, 1, 1, 1);
	}
	
	private void drawRegCard(SpriteBatch sb, float deltatime){

		Rectangle rect;
		Rectangle rect_screen;
		//if(super.moving){
		super.update(deltatime);
		//}
		rect = super.getRectangle();
		rect_screen = new Rectangle(-(Assets.screenwidth / 2), -(Assets.screenheight / 2), Assets.screenwidth, Assets.screenheight);
		// Do not draw if off the screen
		if(OverlapTester.overlapRectangles(rect, rect_screen) == false){
			return;
		}
		
		int PatMask = 0;
		sb.enableBlending();
		//Draw Card
		super.draw(sb);
		//Draw ID and Score
		String s =  String.valueOf("Serial# " +  CardReg.getID() + " (" + CardReg.getLevel() + ")" + "           " + CardReg.SCORE + " Away");
		float msgWidth = Assets.font18.getBounds(s).width;
		float msgHeight = Assets.font18.getBounds(s).height;
		Assets.font18.setColor(0, 0, 0, 1);
		Assets.font18.draw(sb, s , super.getX() + (super.getWidth() / 2) - (msgWidth / 2)  , super.getY() + super.getHeight()  - (0.125f * (super.getHeight())));
		
		// For animated Daubing
		CardDabTR.update(deltatime);
		CardOneAway.update(deltatime);
		int i = 0;
		byte[][] carddata = CardReg.getDataArray();
		boolean dbl = false;
		float dblScale = 0.60f;
		if(carddata[0].length == 10){
			dbl = true;
		}
		for(int c = 0; c<5; c++){
			for(int r = 0,y=4; r<5; r++, y--){ // Y is upside down in Java. WTF . Just for Drawing the Card data
				sb.setColor(1, 1, 1, 1);
				// Invert Y
				rect = GetCellRectangle(c,y);
				if(c == 2 && r == 2){
					// freespace
					if(CardReg.isDaubed(c, r)){
						sb.setColor(1, 1, 1, (float)0.80);
						sb.draw(CardDabTR.getKeyFrame(0), rect.x , rect.y, rect.width, rect.height);
					}
				}else{
					if(CardReg.PG != null){
						PatMask = (CardReg.PG.getPatternGroupMask() & (1 << (c+(r*5)))); // Fixed. Issue with Pattern Mask.
						if(PatMask>0){
							sb.setColor(0, 0, 0, 1); // Make It Black
						}else{
							sb.setColor(0.5f, 0.5f, 0.5f, 0.5f); // Grey it out. 
						}
					}
					if(CardReg.isDaubed(c, r) && PatMask > 0){
						sb.setColor(0, 0, 0, (float)0.80);
						if(dbl){
							sb.draw(Assets.cardnbr_region.get(carddata[c][r] - 1), rect.x , rect.y + rect.height - (rect.height*dblScale), rect.width*dblScale, rect.height*dblScale);
							sb.draw(Assets.cardnbr_region.get(carddata[c][r+5] - 1), rect.x + rect.width - (rect.width*dblScale) , rect.y, rect.width*dblScale, rect.height*dblScale);
						}else{
							sb.draw(Assets.cardnbr_region.get(carddata[c][r] - 1), rect.x , rect.y, rect.width * (float)0.80 , rect.height * (float)0.80);
						}
						sb.setColor(1, 1, 1, (float)0.80);
						if(carddata[c][r] == CardReg.GetLastNbrDaubed()){
							sb.draw(CardDabTR.getKeyFrame(deltatime, Animation.ANIMATION_LOOPING), rect.x , rect.y, rect.width, rect.height);	
						}else{
							sb.draw(CardDabTR.getKeyFrame(0), rect.x , rect.y, rect.width, rect.height);			
						}
					}else{
						if(dbl){
							sb.draw(Assets.cardnbr_region.get(carddata[c][r] - 1), rect.x , rect.y + rect.height - (rect.height*dblScale), rect.width*dblScale, rect.height*dblScale);
							sb.draw(Assets.cardnbr_region.get(carddata[c][r+5] - 1), rect.x + rect.width - (rect.width*dblScale) , rect.y , rect.width*dblScale, rect.height*dblScale);
						}else{
							sb.draw(Assets.cardnbr_region.get(carddata[c][r] - 1), rect.x , rect.y, rect.width * (float)0.80, rect.height * (float)0.80);
						}
						if(CardReg.IsOneAway(carddata[c][r])){
							sb.setColor(1, 1, 1, 0.80f);
							sb.draw(CardOneAway.getKeyFrame(deltatime, Animation.ANIMATION_LOOPING), rect.x , rect.y, rect.width, rect.height);			
						}
						//sb.draw(Assets.cardnbr_region.get(CardReg.getCellData(c, r) - 1),rect.x,  rect.y, rect.x,  rect.y,rect.width, rect.height, (float) 0.5,(float) 0.5, 0);// Assets.cardnbr_region.get(CardReg.getCellData(c, r) - 1)
					}
					
				}
				i++;
			}
		}
		if(CardReg.IsWinner() && CardReg.WINNER_FLAG == false){
			CardReg.WINNER_FLAG = true;
			winEffect.visible = true;
			winEffect.start();
		}
		if(winEffect.visible){
			winEffect.setPosition(super.getX() + (super.getWidth()/2) - (winEffect.getWidth()/2), super.getY() + (super.getHeight()/2) - (winEffect.getHeight()/2));
			winEffect.draw(sb,deltatime);
		}
		// Draw Card ID and Status. 
		sb.setColor(1, 1, 1, 1);
	}
	
	private TextureRegion CreateCardBaseImage(int x, int y, int w, int h){
		// Add a new Frame Buffer above before you do this. call frame bufferend and then pixmap it.
		
		Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);

        final Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
        final ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
       
        final int numBytes = w * h * 4;
        byte[] lines = new byte[numBytes];
        
        final int numBytesPerLine = w * 4; // Flip the Y
        for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
        }

        pixels.rewind();
        Texture t = new Texture(512, 512, Format.RGBA8888);
        t.draw(pixmap, 0, 0);
        
        pixmap.dispose();
        
        return new TextureRegion(t,x,y,w,h);
        
	}

}
