package com.vondrastic.gdxgui;


import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	public Vector2 position;
	public Rectangle bounds;
	
	// timer stuff
	Timer t;
	boolean tRunning = false;
	float movetox = 0;
	float movetoy = 0;
	double moveAng = 0;
	float traveled = 0;
	float dist = 0;
	float mSpeed = 1;
	float layer = 0; // 0 is top most layer. 
	
	public GameObject (float x, float y, float width, float height, float layer) {
		this.position = new Vector2(x, y);
		// this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
		this.bounds = new Rectangle(x , y, width, height);
		this.layer = layer;
	}
	
	public boolean touched(Vector3 touchpoint){
		return OverlapTester.pointInRectangle(this.bounds, touchpoint.x, touchpoint.y);
	}
	
	public boolean move(float x, float y){
		
		if(tRunning == false){
			this.position.x = x;
			this.position.y = y;
		
			this.bounds.x = x - (this.bounds.width / 2);
			this.bounds.y = y - (this.bounds.height / 2);
			return true;
		}else{
			return false;
		}
		
	}
	// Schedule moving the object to the location at the speed indicated
	public void moveto(float x, float y, long interval, float speed){
		
		long delay = 10;
		
		if(tRunning == false){
			t = new Timer();
			tRunning = true;
			mSpeed = speed;
			// Find distance to move
			movetox = x;
			movetoy = y;
			float a = x - position.x;
			float b = y - position.y;
			dist = (float) Math.sqrt((a * a) + (b * b)); // Pathagourous, Thank you.
			
			moveAng = getAngle(position.x,position.y,x,y);
			traveled = 0;
			
			t.scheduleAtFixedRate(new TimerTask(){ public void run(){
				if(traveled + mSpeed >= dist){
					position.x = movetox; // Final Resting Place
					position.y = movetoy; // -------------------
					t.cancel();
					tRunning = false;
				}else{
					Vector2 pos = getPoint(moveAng + 180, mSpeed, position);
					traveled += mSpeed;
					position.x = pos.x; 
					position.y = pos.y;
					// Also update bounds box
					bounds.x = pos.x ;//- (bounds.width / 2);
					bounds.y = pos.y ;//- (bounds.height / 2);
				}
				}}, delay, interval);
		}
	}
	
	private double getAngle(double Sx, double Sy, double Ex, double Ey )
	{
		double Rad2Deg = 180.0 / Math.PI;
		//double Deg2Rad = Math.PI / 180.0;
		return Math.atan2(Sy - Ey , Sx - Ex) * Rad2Deg;
	}
	
	private Vector2 getPoint(double angle, float distance, Vector2 center) {
        // Angles in java are measured clockwise from 3 o'clock.
        double theta = Math.toRadians(angle);
        Vector2 p = new Vector2();
        
        p.x = (float) (center.x + distance*Math.cos(theta));
        p.y = (float) (center.y + distance*Math.sin(theta));
        
        return p;
    }
	
	public float getPosCenterX(){
		return this.position.x - (this.bounds.width / 2);
	}
	
	public float getPosCenterY(){
		return this.position.y - (this.bounds.height / 2);
	}
	
	private float getVelocityX(double AngleRadian, double speed){
		return (float)(Math.cos(AngleRadian) * speed);
	}
	
	private float getVelocityY(double AngleRadian, double speed){
		return (float)(Math.sin(AngleRadian) * speed);
	}
}