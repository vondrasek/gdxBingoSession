package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

// Manage Bingo Card Objects
public class BingoCardManager extends GuiObject{
	
	private Rectangle bounds = new Rectangle();
	private Vector2 view = new Vector2(0,0);
	
	public enum VIEWLAYOUT{LARGE, MEDIUM, SMALL};
	private VIEWLAYOUT viewSelected = VIEWLAYOUT.LARGE;
	private int viewLayoutX = 3;
	private int viewLayoutY = 1;
	
	public int currentPage = 0;
	public int totalPages = 0;
	public int cardsPerPage = 0;
	public int totalCards = 0;
	public int virtualWidth = 0;
	public int virtualHeight = 0;
	public int virtualX = 0;
	public int virtualY = 0;
	
	private static float drag = 4000; //per second
	private float dragX = 0;
	private float dragY = 0;
	
	private float velocityX = 0;
	private float velocityY = 0;
	
	private boolean fling = false;
	
	private List <BingoCardObject> BCO = new ArrayList<BingoCardObject>(); // Create These and Manage
	private List <BingoCard> BCD = null; // Reference
	private PatternGroup PG = null; // Reference
	private CameraMan cm; // Reference
	private TextureRegion dauberTexture = null;
	private TextureRegion cardTexture = null;
	private float stateTime = 0;
	
	
	public BingoCardManager(float x, float y, float w, float h, CameraMan cm, PatternGroup pg, TextureRegion bg,  TextureRegion card, TextureRegion dauber, List <BingoCard> bcd ){
		
		super(x,y,w,h,true,bg);
		
		bounds.x = x;
		bounds.y = y;
		bounds.width = w;
		bounds.height = h;
		dauberTexture = dauber;
		cardTexture = card;
		BCD = bcd;
		PG = pg;
		CreateCardObjects();
	}
	//return view to home
	private void home(){
		view.x = 0;
		view.y = 0;
	}
	
	private void viewTranslate(float x, float y){
		view.x+= x;
		view.y+= y;
	}
	
	// Tag Card as a Winner and produce effect that they won
	private void UpdateWinningCardAnimation(){
		
	}
	
	private void ShowWinningCard(){
		
	}
	// STOP the animation of a winner
	private void CancelWinningCardAnimation(){
		
	}
	
	public boolean setCardDauber(TextureRegion dauber){
		dauberTexture = dauber;
		if(BCO.size()>0){
			for(int i=0; i<BCO.size(); i++){
				BCO.get(i).SetDauberTexture(dauberTexture);
			}
			return true;
		}
		return false;
	}
	
	public void SetCardView(VIEWLAYOUT view){
		viewSelected = view;
		if(viewSelected == VIEWLAYOUT.LARGE){
			viewLayoutX = 3;
			viewLayoutY = 1;
		}else if(viewSelected == VIEWLAYOUT.MEDIUM){
			viewLayoutX = 4;
			viewLayoutY = 2;
		}else if(viewSelected == VIEWLAYOUT.SMALL){
			viewLayoutX = 5;
			viewLayoutY = 3;
		}
		CreateCardObjects();
	}
	
	private void CreateCardObjects(){
		// TODO add check for if there is only one card
		BCO.clear();
		for(int i=0; i<((viewLayoutX+1) * viewLayoutY); i++){
			if(BCD.size() > i){
				BCO.add(new BingoCardObject(-400f, -240f, 320f, 320f, true, cardTexture, BCD.get(i), i, dauberTexture));
			}
		}
		if(BCD.size() > 0){
			BCO.get(0).SetPatternGroup(PG);
		}
		CardsLayoutStart();
	}
	
	// Used for fling objects
	public void updateFlingCardPosition(float deltaTime){
		
		boolean doneX = false;
		boolean doneY = true;
		float posX=0;
		float posY=0;

		if(velocityX > -100 && velocityX < 100){// Clamp
			doneX = true;
		}else{
			velocityX += dragX * deltaTime;
			posX = (velocityX * deltaTime);
		}
		
	/*	if(velocityY > -20 && velocityY < 20){// Clamp
			doneY = true;
		}else{
			velocityY += dragY * deltaTime;
			posY = (velocityY * deltaTime);
		}*/
		virtualX+= posX;
		virtualY+= posY;
		
		if(doneX && doneY){
			fling = false;
		}else{
			for(int i=0; i<BCO.size(); i++){
				BCO.get(i).updatePosition(posX, posY );
			}
		}
	}
	
	public void Fling(float velX, float velY){
		if(fling == false){
			// Check to see if we can flig cards
			if(velX > 0){dragX = -drag;}else{dragX = drag;}
			if(velY > 0){dragY = -drag;}else{dragY = drag;}
			velocityX = velX;
			velocityY = velY;
			fling = true;
			Assets.playSound(Assets.swipeSound);
		}
	}
	
	private void CardsMoveLoopCheck(){
		//TODO: change 4 to card view parameter
		int totalCards = ((viewLayoutX+1)*viewLayoutY);
		for(int i=0; i<BCO.size(); i++){
			if(BCO.get(i).getX() + BCO.get(i).getWidth()  < -bounds.width/2){// Move to other size
				if(BCO.get(i).id + totalCards >= BCD.size()){
					return;
					// Lock Cards from moving
				}else{
					BCO.get(i).UpdateData(BCD.get(BCO.get(i).id + totalCards), BCO.get(i).id + totalCards);
					BCO.get(i).setPosition( (BCO.get(i).getX() + BCO.get(i).getWidth() + bounds.width), BCO.get(i).getY());
				}
			}else if(BCO.get(i).getX()  > bounds.width/2){
				if(BCO.get(i).id - totalCards < 0){
					CardsLayoutStart();
					return;
				}else{
					BCO.get(i).UpdateData(BCD.get(BCO.get(i).id -totalCards), BCO.get(i).id - totalCards);
					BCO.get(i).setPosition((BCO.get(i).getX() -bounds.width - BCO.get(i).getWidth()), BCO.get(i).getY());
				}
			}else{
				BCO.get(i).setPosition( + BCO.get(i).getX(), BCO.get(i).getY());
			}
		}
	}
	
	private void CardsLayoutStart(){
		float spacer = 8;
		int CardsWidth = (int) (((bounds.width) - (spacer * viewLayoutX)) / viewLayoutX);
		int i = 0;
		virtualX= 0;
		virtualY= 0;
		fling = false;

		for(int col = 0; col< viewLayoutX+1 ; col++){
			for(int row =0; row < viewLayoutY; row++){
				if(BCD.size() > i){
					BCO.get(i).setSize(CardsWidth, CardsWidth);
					BCO.get(i).setPosition(bounds.x + (col * CardsWidth) + (col * spacer) ,bounds.y + (row * CardsWidth) + (row * spacer));
					i++;
				}else{
					return;
				}
			}
		}
	}
	
	public void CardDataSort(){
		Collections.sort(BCD);
		for(int i=0; i<BCO.size(); i++){
			BCO.get(i).UpdateData(BCD.get(i), i);
		}
		CardsLayoutStart();
		//Send cards back to home position;
	}
	
	public void Update(float deltaTime){
		stateTime+= deltaTime;
	}
	
	public boolean Touched(Vector3 point){
		if(OverlapTester.pointInRectangle(bounds, point.x, point.y)){
			return true;
		}
		return false;
	}
	
	public void Draw(SpriteBatch SB, float deltaTime){
		
		Update(deltaTime);
		if(fling){
			updateFlingCardPosition(deltaTime);
			CardsMoveLoopCheck();
		}
		super.draw(SB);
		for(int i = (BCO.size() - 1); i > -1; i--){
			BCO.get(i).draw(SB, deltaTime);
		}
	}
}
