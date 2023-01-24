package com.vondrastic.gdxgui;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GuiObject {
	public boolean visible;
	public boolean moving = false;
	public Sprite sprite;
	
	private double moveAng = 0;
	private float traveled = 0;
	private float dist = 0;
	private float mSpeed = 1;
	
	private float movetox;
	private float movetoy;
	
	private float origX;
	private float origY;
	
	private static float drag = 1000; //per second
	private float dragX = 0;
	private float dragY = 0;
	
	private float velocityX = 0;
	private float velocityY = 0;
	private boolean fling = false;
	
	private Timer T;
	private boolean tRunning;
	float stateTime;
	float Alpha = 1.0f;
	
	public GuiObject(float x, float y, float w, float h, boolean visable, TextureRegion t){
		
		sprite = new Sprite(t);
		sprite.setBounds(x, y, w, h);
		setPosition(x, y);
		moving = false;
		visible = visable;
		stateTime = 0;
	}
	
	public GuiObject(float x, float y, boolean visable, TextureRegion t){
		
		sprite = new Sprite(t);
		sprite.setBounds(x, y, t.getRegionWidth(), t.getRegionHeight());
		setPosition(x, y);
		moving = false;
		visible = visable;
		stateTime = 0;
	}
	
	public void moveto(float x, float y, long interval, float speed){
		
		long delay = 10;
		if(tRunning == false){
			T = new Timer();
			tRunning = true;
			mSpeed = speed;
			// Find distance to move
			movetox = x;
			movetoy = y;
			float a = x - sprite.getX();
			float b = y - sprite.getY();
			dist = (float) Math.sqrt((a * a) + (b * b)); // Pythagoras, Thank you.
			
			moveAng = Utils.getAngle(sprite.getX(),sprite.getY(),x,y);
			traveled = 0;
			
			T.scheduleAtFixedRate(new TimerTask(){ public void run(){
				if(traveled + mSpeed >= dist){
					sprite.setBounds(movetox, movetoy, sprite.getWidth(), sprite.getHeight());// Final Resting Place
					T.cancel();
					tRunning = false;
				}else{
					Vector2 position = new Vector2(sprite.getX(),sprite.getY());
					Vector2 pos = Utils.getPoint(moveAng + 180, mSpeed, position);
					traveled += mSpeed;
					sprite.setBounds(pos.x, pos.y, sprite.getWidth(), sprite.getHeight());
				}
				}}, delay, interval);
		}
	}
	
	// New Move to using delta time
	public void updatePosition(){
		int speed = (int)(mSpeed * stateTime);
		
		if(traveled >= dist){
			sprite.setBounds(movetox, movetoy, getWidth(), getHeight());// Final Resting Place
			moving = false;
		}else{
			Vector2 position = new Vector2(origX,origY);
			Vector2 pos = Utils.getPoint(moveAng + 180, speed, position);
			traveled = speed;
			sprite.setBounds(pos.x, pos.y, sprite.getWidth(), sprite.getHeight());
		}
	}
	
	// Used for fling objects
	public void updatePosition(float deltaTime){
		
		boolean doneX = false;
		boolean doneY = false;
		float posX=0;
		float posY=0;

		if(velocityX > -20 && velocityX < 20){// Clamp
			posX = getX();
			doneX = true;
		}else{
			velocityX += dragX * deltaTime;
			posX = getX() + (velocityX * deltaTime);
		}
		
		if(velocityY > -20 && velocityY < 20){// Clamp
			posY = getY();
			doneY = true;
		}else{
			velocityY += dragY * deltaTime;
			posY = getY() + (velocityY * deltaTime);
		}
		
		if(doneX && doneY){
			fling = false;
		}else{
			setPosition(posX, posY );
		}
	}
	
	public void moveto(float x, float y, float speed){
		
		if(moving){ return;}
			moving = true;
			mSpeed = speed;
			movetox = x;
			movetoy = y;
			origX = sprite.getX();
			origY = sprite.getY();
			
			float a = x - sprite.getX();
			float b = y - sprite.getY();
			
			dist = Utils.getDistance(a, b);
			moveAng = Utils.getAngle(getX(),getY(),x,y);
			traveled = 0;
			reset();
			
	}

	public void reset(){
		stateTime = 0;
	}
	
	public void update(float deltaTime){
		stateTime += deltaTime;
		if(moving){
			updatePosition();
		}else if(fling){
			updatePosition(deltaTime);
		}
	}	
	// ToDo: Finish this function
	public void Fling(float velocityX, float velocityY){
		if(velocityX > 0){dragX = -drag;}else{dragX = drag;}
		if(velocityY > 0){dragY = -drag;}else{dragY = drag;}
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		fling = true;
		
		//double Angle = Utils.getAngle(sprite.getX(),sprite.getY(),velocityX,velocityY);
		//if(Angle >0 && Angle < 45){
			
		//}
	}
	
	public void updatePosition(float x, float y){
		sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
	}
	
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
	}
	
	public float getX(){
		return sprite.getX();
	}
	
	public float getY(){
		return sprite.getY();
	}
	
	public float getWidth(){
		return sprite.getWidth();
	}
	
	public float getHeight(){
		return sprite.getHeight();
	}
	
	public float getPosCenterX(){
		return sprite.getX() - (sprite.getWidth() / 2);
	}
	
	public float getPosCenterY(){
		return sprite.getY() - (sprite.getHeight() / 2);
	}
	
	public void SetScale(float amount){
		sprite.scale(amount);
	}
	
	public void setSize(float w, float h){
		sprite.setSize(w, h);
	}
	
	public Rectangle getRectangle(){
		return sprite.getBoundingRectangle();
	}
	
	public void draw(SpriteBatch SB, float alpha){
		sprite.draw(SB, alpha);
	}
	
	public void draw(SpriteBatch SB){
		sprite.draw(SB, Alpha);
	}
	
	public boolean touched(Vector3 point){
		return OverlapTester.pointInRectangle(sprite.getBoundingRectangle() , point.x, point.y );
	}
	
	public void hide(){
		visible = false;
	}
	
	public void show(){
		visible = true;
	}
	
}
