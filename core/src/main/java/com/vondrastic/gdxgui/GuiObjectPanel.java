package com.vondrastic.gdxgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class GuiObjectPanel extends GuiObject implements GuiObjectButtonEvent{
	// ToDo: Add methods to support children game objects
	private boolean scrollable;
	public enum DIRECTION{UP, DOWN, LEFT, RIGHT, STATIONARY}
	private DIRECTION mode = DIRECTION.UP;
	private boolean opened = false;
	private OrthographicCamera Camera;
	private List<GuiObjectButton> ButtonList = new ArrayList<GuiObjectButton>();
	
	public GuiObjectPanel(float x, float y, float width, float height, boolean visable, TextureRegion tr, DIRECTION direction, InputMultiplexer IM, OrthographicCamera camera ) {
		super(x, y, width, height, visable, tr);
		Camera = camera;
		if(IM != null){
			IM.addProcessor(new GameInputProcessor());
		}
	}
	
	public void open(){
	
		opened = true;
		super.visible = true;
		if(mode == DIRECTION.UP){
			super.moveto(super.sprite.getX(), super.sprite.getY() - super.sprite.getHeight(), 5 , 2);
		}else if(mode == DIRECTION.DOWN){
			super.moveto(super.sprite.getX(), super.sprite.getY() + super.sprite.getHeight(), 5 , 2);
		}else if(mode == DIRECTION.LEFT){
			super.moveto(super.sprite.getX() - super.sprite.getWidth(), super.sprite.getY() , 5 , 2);
		}else if(mode == DIRECTION.RIGHT){
			super.moveto(super.sprite.getX() + super.sprite.getWidth(), super.sprite.getY() , 5 , 2);
		}else if(mode == DIRECTION.STATIONARY){
			
		}
	}
	
	public void close(){
		opened = false;
		
		if(mode == DIRECTION.UP){
			super.moveto(super.sprite.getX(), super.sprite.getY() + super.sprite.getHeight(), 5 , 2);
		}else if(mode == DIRECTION.DOWN){
			super.moveto(super.sprite.getX(), super.sprite.getY() - super.sprite.getHeight(), 5 , 2);
		}else if(mode == DIRECTION.LEFT){
			super.moveto(super.sprite.getX() + super.sprite.getWidth(), super.sprite.getY() , 5 , 2);
		}else if(mode == DIRECTION.RIGHT){
			super.moveto(super.sprite.getX() - super.sprite.getWidth(), super.sprite.getY() , 5 , 2);
		}else if(mode == DIRECTION.STATIONARY){
			super.visible = false;
		}
	}
	
	public boolean touched(Vector3 point){
		if(super.touched(point)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addGuiObject(GuiObject obj){
		
	}
	// See if this works / if yes add to guiButton Object.
	public void addGuiObject(GuiObjectButton obj){
	
	}

	public void addGuiObject(GuiObjectAd obj){
		
	}
	public void exit(){
		
	}
	// Draw all objects relative to the pannel
	public void draw(SpriteBatch sb){
		
		super.sprite.draw(sb);
		// Check all buttons
		
	}
	
	/**  Nested Class for Input Processing */
	private class GameInputProcessor implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Vector3 vec = new Vector3();
			Camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			
			return false;
		}

		
		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}
		  
	}

	@Override
	public boolean clicked(String Cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean statedown(String Cmd) {
		// TODO Auto-generated method stub
		return false;
	}

}
