package com.vondrastic.gdxgui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;

	private List<TextureRegion> keyFrames;
	
	final float frameDuration;
	
	float stateTime;

	public Animation (float frameDuration, List<TextureRegion> keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
		stateTime = 0;
	}
	
	public TextureRegion getKeyFrame (int idx) {
		return this.keyFrames.get(idx);
	}

	public TextureRegion getKeyFrame (float deltaTime, int mode) {
		
		// Had to remove this and do it after all drawing. 
		// Else need seperate Animation after each object. 
		// update(deltaTime);
		
		int frameNumber = (int)(stateTime / frameDuration);

		if (mode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(keyFrames.size() - 1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.size();
		}
		return keyFrames.get(frameNumber);
	}
	
	public void reset(){
		stateTime = 0;
	}
	
	public void update(float deltaTime){
		stateTime += deltaTime;
	}
}
