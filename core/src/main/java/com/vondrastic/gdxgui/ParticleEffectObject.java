package com.vondrastic.gdxgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParticleEffectObject extends GuiObject{
	private ParticleEffect particleEffect;
	
	public ParticleEffectObject(float x, float y, boolean visable, TextureRegion t, String effectFile, String effectImageDir) {
		super(x, y, visable, t);
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal(effectFile), Gdx.files.internal(effectImageDir));
	    particleEffect.setPosition(super.getX() + (super.getWidth()/2), super.getY() + (super.getHeight()/2));
	    
	}
	
	public void start(){
		particleEffect.reset();
		particleEffect.start();
	}
	
	public void stop(){
		particleEffect.setDuration(500);
	}
	
	public void update(float deltaTime){
		
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		if(super.visible){
			update(deltaTime);
			super.update(deltaTime);
			particleEffect.setPosition(super.getX() + (super.getWidth()/2), super.getY() + (super.getHeight()/2));
			particleEffect.draw(sb, deltaTime);
		//super.draw(sb);
		}
	}
	

}
