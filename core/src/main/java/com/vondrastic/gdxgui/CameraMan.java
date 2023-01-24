package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class CameraMan {
	private Vector3 homePosition = new Vector3();
	private Vector3 gotoPosition = new Vector3();
	

	private float moveSpeed = 1.0f;
	public boolean moving = false;
	
	private float stateTime = 0;
	private OrthographicCamera cam;
	
	private float traveled = 0;
	private float dist = 0;
	private float moveAng = 0;
	public boolean zoomedin = false;
	public boolean zoomin = false;
	
	private float zoomInRatio = 0.70f;
	private float zoomOutRatio = 1.0f;
	
	public CameraMan(OrthographicCamera camera){
		cam = camera;
	}
	
	public Rectangle GetVisibleBounds(){
		Rectangle rect = new Rectangle(cam.position.x - (cam.viewportWidth*cam.zoom/2), cam.position.y - (cam.viewportHeight*cam.zoom/2), cam.viewportWidth*cam.zoom, cam.viewportHeight*cam.zoom);
		return rect;
	}
	
	private void updatePosition(){
		int speed = (int)(moveSpeed * stateTime);
		float a = 0;
		float b = 0;
		
		if(traveled >= dist){
			moving = false;
			 if(zoomin){
				 cam.zoom=zoomInRatio;
			 }else{
				 cam.zoom=zoomOutRatio;
				 cam.position.set(0,0,0);
			 }
			 cam.update();
		}else{
			Vector2 position = new Vector2(0,0);
			Vector2 pos = Utils.getPoint(moveAng + 180, speed, position);
			traveled+= speed;
			if(zoomin){
				cam.zoom = zoomOutRatio - (((traveled /dist) * (zoomOutRatio - zoomInRatio)));
			}else{
				cam.zoom = zoomInRatio + ((traveled /dist) *(zoomOutRatio - zoomInRatio));
			}
			
			cam.translate(pos.x, pos.y, 0);
			cam.update();
		}
	}
	
	public void zoomOut(){
		if(zoomedin){
			moveTo(homePosition.x, homePosition.y, 1, 5);
		}
	}
	
	public void update(float deltaTime){
		stateTime+= deltaTime;
		if(moving){
			updatePosition();
		}
	}
	
	public void moveTo(float x, float y, float z, float speed){
	
		gotoPosition.x = x;
		gotoPosition.y = y;
		gotoPosition.z = z;
		
		if(moving){ return;}
	
		if(cam.zoom > gotoPosition.z){
			zoomin = true;
			zoomedin = true;
		}else{
			zoomin = false;
			zoomedin = false;
		}
		moving = true;
		homePosition.x = cam.position.x;
		homePosition.y = cam.position.y;
		
		float a = gotoPosition.x + homePosition.x;
		float b = gotoPosition.y + homePosition.y;
		
		dist = (float) Math.sqrt((a * a) + (b * b)); // Pythagoras, Thank you.
		moveAng = (float) Utils.getAngle(homePosition.x ,homePosition.y ,gotoPosition.x ,gotoPosition.y);
		moveSpeed = (dist / speed);
		traveled = 0;
		stateTime = 0;
	}
}
