package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class Multitouch {
	List<Vector3> fingers = new ArrayList<Vector3>();; 
	// MultiTouch
	int fingerOnePointer;
	int fingerTwoPointer;
	float lastDistance = 0;
	
	public Multitouch(){
		
	}
	
	public int addPoint(Vector3 point){
		if(fingers.add(point)){
			return fingers.size() - 1;
		}else{
			return -1;
		}
	}
	
	public void removePoint(int id){
		fingers.remove(id);
	}
	
	public float getDistanceTwoFingers(int id1, int id2){
		if(fingers.size() > 1){
			return fingers.get(id1).dst(fingers.get(id2));
		}else{
			return 0f;
		}
	}
	
	public void clearall(){
		fingers.clear();
		lastDistance = 0;
	}
	
}
