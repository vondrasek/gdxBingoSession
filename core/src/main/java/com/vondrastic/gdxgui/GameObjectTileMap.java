package com.vondrastic.gdxgui;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameObjectTileMap extends GameObject{
	
	public final int rows;// Number of rows in tilemap
	public final int col; // number of columns in tilemap
	public final int filler; // Space between the rows and objects
	public final int cellwidth;
	public final int cellheight;
	public final int mapwidth;
	public final int mapheight;
	public float xOffset = 0;
	public float yOffset = 0;
	public final Rectangle rMap;
	public final List<Rectangle> cells = new ArrayList<Rectangle>();
	
	public GameObjectTileMap (float x, float y, float width, float height, int rows, int col, int cellwidth, int cellheight, int filler, float tileoffset_x, float tileoffset_y, float layer) {
		// ---------------------------------------
		// Note Cells are relative to the tilemap.
		// ---------------------------------------
		super(x,y,width,height, layer);
		
		this.rows = rows;
		this.col = col;
		this.filler = filler; // Space between each cell
		this.cellwidth = cellwidth;
		this.cellheight = cellheight;
		this.mapwidth = (int)width;
		this.mapheight = (int)height;
		this.rMap = new Rectangle(x, y, mapwidth, mapheight);
		
		// Create all the rectangles for the tile map
		int xPos = 0;
		int yPos = 0;
		// Note:: Y axis is reversed in Java:: Thank you APPLE!. Lower Left is 0,0 not upper left.
		for(int r=rows-1; r > -1; r--){
			for(int c=0; c < col; c++){
				yPos = (r * cellheight) + (r * filler) + (int)(y + tileoffset_y);
				xPos = (c * cellwidth) + (c * filler) + (int)(x + tileoffset_x);
				cells.add(new Rectangle(xPos, yPos, cellwidth, cellheight));
			}
		}
	}
	
	@Override
	public boolean move(float x, float y){
		float setX = x - xOffset;
		float setY = y - yOffset;
		// Calc distance moved

		float dist_x = setX - rMap.x;
		float dist_y = setY - rMap.y;
		
		rMap.x = setX;
		rMap.y = setY;
		
		super.bounds.x = setX;
		super.bounds.y = setY;
		
		super.position.x = setX;
		super.position.y = setY;
		
		for(int r=0; r < (rows * col); r++){
			cells.get(r).x += dist_x;
			cells.get(r).y += dist_y;
		}
		return true;
	}
	
	public void touchDown(float x, float y){
		// Set the relative location between touched point and screen
		xOffset = x - rMap.x;
		yOffset = y - rMap.y;
	}
	
	// Schedule moving the object to the location at the speed indicated
	@Override
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
			dist = (float) Math.sqrt((a * a) + (b * b)); // Pythagoras, Thank you.
			
			moveAng = getAngle(position.x,position.y,x,y);
			traveled = 0;
			
			t.scheduleAtFixedRate(new TimerTask(){ public void run(){
				if(traveled + mSpeed >= dist){
					move(movetox, movetoy);
					t.cancel();
					tRunning = false;
				}else{
					Vector2 pos = getPoint(moveAng + 180, mSpeed, position);
					traveled += mSpeed;
					move(pos.x,pos.y);
				}
				}}, delay, interval);
		}
	}
	
	private Vector2 getPoint(double angle, float distance, Vector2 center) {
        // Angles in java are measured clockwise from 3 o'clock.
        double theta = Math.toRadians(angle);
        Vector2 p = new Vector2();
        
        p.x = (float) (center.x + distance*Math.cos(theta));
        p.y = (float) (center.y + distance*Math.sin(theta));
        
        return p;
    }
	
	private double getAngle(double Sx, double Sy, double Ex, double Ey )
	{
		double Rad2Deg = 180.0 / Math.PI;
		//double Deg2Rad = Math.PI / 180.0;
		return Math.atan2(Sy - Ey , Sx - Ex) * Rad2Deg;
	}
	
	public byte touchedCell(Vector3 touchpoint){
		float x = touchpoint.x;
		float y = touchpoint.y;
		
		if(OverlapTester.pointInRectangle(this.rMap, x, y)){
			for(int r=0; r < (rows * col); r++){
				if(OverlapTester.pointInRectangle(cells.get(r), touchpoint.x, touchpoint.y)){
					return (byte) (r + 1);
				}
			}
			return 0; // this means the card was touched but not a cell
		}else{
			return -1;
		}
	}
	
	public Rectangle getTimeMapObjectRect(){
		return super.bounds;
	}
	
	public Rectangle getCellRect(int idx){
		return this.cells.get(idx);
	}
	
	public float getCellx(int idx){
		return this.cells.get(idx).x;
	}
	public float getCelly(int idx){
		return this.cells.get(idx).y;
	}
	
	public int getTileMapObjectCnt(){
		return this.cells.size();
	}
	
	public int getTimeMapWidth(){
		return this.mapwidth;
	}
	
	public int getTimeMapHeight(){
		return this.mapheight;
	}
}
