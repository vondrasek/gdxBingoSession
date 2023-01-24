package com.vondrastic.gdxgui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.GameInterface.MENU_CMD;
import com.vondrastic.gdxgui.GuiObjectButton.TYPE;

public class GuiObjectTrivia extends GuiObject{
	String Question;
	GuiObjectButton answerA;
	GuiObjectButton answerB;
	GuiObjectButton answerC;
	GuiObjectButton answerD;
	GuiObjectButton closeBtn;
	
	Color rightColor = new Color(0,1,0,1);
	Color wrongColor = new Color(1,0,0,1);
	String title = "Trivia! Question "; 
	int correctAnswer = 0;
	int Guess = 0;
	int timeout = 15;
	float stateTime = 0;
	int score = 0;
	public int gameIdx = 0;
	
	public GuiObjectTrivia(float x, float y, float w, float h, boolean visable, TextureRegion t, String question, String a, String b, String c, String d, int answer, int gameidx) {
		super(x, y, w, h, visable, t);
		Question = question; 
		float b_width = w/2;
		float b_height = h/4;
		correctAnswer = answer;
		gameIdx = gameidx;
		
		closeBtn = new GuiObjectButton(x, y+h-b_height, 100, b_height, 0, "close", t, t, TYPE.ACTION, getMenuCmd(1), getMenuCmd(1));
		answerA = new GuiObjectButton(x, y+b_height, b_width, b_height, 0, a, t, t, TYPE.ACTION, getMenuCmd(1), getMenuCmd(1));
		answerB = new GuiObjectButton(x+b_width, y+b_height, b_width, b_height, 0, b, t, t, TYPE.ACTION, getMenuCmd(2), getMenuCmd(2));
		answerC = new GuiObjectButton(x, y, b_width, b_height, 0, c, t, t, TYPE.ACTION, getMenuCmd(3), getMenuCmd(3));
		answerD = new GuiObjectButton(x+b_width, y, b_width, b_height, 0, d, t, t, TYPE.ACTION, getMenuCmd(4), getMenuCmd(4));
		
	}
	
	private MENU_CMD getMenuCmd(int i){
		if(i == correctAnswer){
			return MENU_CMD.TRIVIA_CORRECT;
		}else{
			return MENU_CMD.TRIVIA_WRONG;
		}
	}
	
	public void update(float deltaTime){
		stateTime+=deltaTime;
		if(stateTime >= timeout){
			super.visible = false;
			stateTime = 0;
		}
	}
	
	public boolean touched(Vector3 point){
		
		if(super.visible){
			
			if(OverlapTester.pointInRectangle(closeBtn.getRectangle(), point.x, point.y)){
				super.visible = false;
				return true;
			}
			
			if(Guess > 0){
				return false;
			}
			
			if(OverlapTester.pointInRectangle(super.getRectangle(), point.x, point.y)){
				if(OverlapTester.pointInRectangle(answerA.getRectangle(), point.x, point.y)){
					answerA.touchDown();
				}
				if(OverlapTester.pointInRectangle(answerB.getRectangle(), point.x, point.y)){
					answerB.touchDown();
				}
				if(OverlapTester.pointInRectangle(answerC.getRectangle(), point.x, point.y)){
					answerC.touchDown();
				}
				if(OverlapTester.pointInRectangle(answerD.getRectangle(), point.x, point.y)){
					answerD.touchDown();
				}
			}
		}
		return false;
	}
	
	public boolean touchedUp(Vector3 point){
		int guess = 0;
		if(super.visible){
			
			if(Guess > 0){
				return false;
			}
			
			if(OverlapTester.pointInRectangle(super.getRectangle(), point.x, point.y)){
				if(answerA.clicked(point)){
					guess = 1;
					//GameEvent.GameMessage(answerA.GetCmdDn());
				}
				if(answerB.clicked(point)){
					guess = 2;
					//GameEvent.GameMessage(answerB.GetCmdDn());
				}
				if(answerC.clicked(point)){
					guess = 3;
					//GameEvent.GameMessage(answerC.GetCmdDn());
				}
				if(answerD.clicked(point)){
					guess = 4;
					//GameEvent.GameMessage(answerD.GetCmdDn());
				}
			}
		}
		
		if(guess > 0){
			if(validateAnswer(guess)){
			
			}
			Guess = guess;
			showAnswer();
			return true;
		}
		return false;
	}
	
	public boolean isCorrect(){
		if(validateAnswer(Guess)){
			return true;
		}
		return false;
	}
	
	private boolean validateAnswer(int answerIdx){
		
		if(answerIdx == correctAnswer){
			score = (int)(timeout - stateTime);
			return true;
		}
		
		return false;
	}
	//TODO: Change buttons to collection 
	private void showAnswer(){
		
		if(correctAnswer != 1){
			answerA.setTextColor(wrongColor);
		}else{
			answerA.setTextColor(rightColor);
		}
		if(correctAnswer != 2){
			answerB.setTextColor(wrongColor);
		}else{
			answerB.setTextColor(rightColor);
		}
		if(correctAnswer != 3){
			answerC.setTextColor(wrongColor);
		}else{
			answerC.setTextColor(rightColor);
		}
		if(correctAnswer != 4){
			answerD.setTextColor(wrongColor);
		}else{
			answerD.setTextColor(rightColor);
		}
	}
	
	public int getScore(){
		return score;
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		float msgWidth;
		if(super.visible){
			update(deltaTime);
			super.draw(sb);
			//Draw TIttle:
			int timeLeft = (int)(timeout - stateTime);
			msgWidth = Assets.font32.getBounds(title + " Time Left: " + timeLeft).width;
			Assets.font32.draw(sb, title + " Time Left: " + timeLeft, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight()));		
			if(Guess > 0){
				if(isCorrect()){
					Assets.font32.setColor(0, 1, 0, 1);
					msgWidth = Assets.font32.getBounds("Correct!").width;
					Assets.font32.draw(sb, "Correct", super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight() - 150));		
				}else{
					Assets.font32.setColor(1, 0, 0, 1);
					msgWidth = Assets.font32.getBounds("Wrong").width;
					Assets.font32.draw(sb, "Wrong", super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight() - 150));
				}
			}
			
			Assets.font32.setColor(1, 1, 1, 1);
			msgWidth = Assets.font32.getBounds(Question).width;
			if(msgWidth > super.getWidth()){// Split into two parts
				int start = Question.length()/2;
				int q1 = Question.indexOf(" ", start);
				String Q1 = Question.substring(0, q1);
				String Q2 = Question.substring(q1);
				float msgWidthQ1 = Assets.font32.getBounds(Q1).width;
				float msgWidthQ2 = Assets.font32.getBounds(Q2).width;
				Assets.font32.draw(sb, Q1, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidthQ1 / 2)  , super.sprite.getY() + (super.sprite.getHeight() - 50));
				Assets.font32.draw(sb, Q2, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidthQ2 / 2)  , super.sprite.getY() + (super.sprite.getHeight() - 100));
					
			}else{
				Assets.font32.draw(sb, Question, super.sprite.getX() + (super.sprite.getWidth() / 2) - (msgWidth / 2)  , super.sprite.getY() + (super.sprite.getHeight() - 100));
			}
			
			if(closeBtn.visible){
				closeBtn.draw(sb, deltaTime);
			}
			if(answerA.visible){
				answerA.draw(sb, deltaTime);
			}
			if(answerB.visible){
				answerB.draw(sb, deltaTime);
			}
			if(answerC.visible){
				answerC.draw(sb, deltaTime);
			}
			if(answerD.visible){
				answerD.draw(sb, deltaTime);
			}
		}
	}

}
