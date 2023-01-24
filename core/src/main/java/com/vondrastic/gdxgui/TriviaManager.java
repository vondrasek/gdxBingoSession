package com.vondrastic.gdxgui;

// Manages the trivia questions and score for the Session 
// Questions , Answers, Score

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class TriviaManager {
	List<GuiObjectTrivia> TriviaObject;
	int currentTriviaIdx = -1;
	
	public TriviaManager(String triviaFolderExtracted){
	
		TriviaObject = getTriviaObjects(triviaFolderExtracted, Assets.menu_bg);
		
	}
	
	public List<GuiObjectTrivia> getTriviaObjects(String triviaFolderExtracted, TextureRegion tr){
		BufferedReader in = null;
		FileInputStream fis = null;
		
		List<GuiObjectTrivia> TriviaObject = new ArrayList<GuiObjectTrivia>();
		TriviaObject.clear(); // Remove all Ads
		
		File d = new File(triviaFolderExtracted);
		String[] fileList = d.list();
		
		String question = "";
		String answerA = "";
		String answerB = "";
		String answerC = "";
		String answerD = "";
		
		int answerIdx = 1;
		
		for(int i=0; i<fileList.length; i++){
			try {
				String path = triviaFolderExtracted + "/" + fileList[i];
				fis = new FileInputStream(triviaFolderExtracted + "/" + fileList[i]);
				in = new BufferedReader(new InputStreamReader(fis));
				question = (String)in.readLine();
				answerA = (String)in.readLine();
				answerB = (String)in.readLine();
				answerC = (String)in.readLine();
				answerD = (String)in.readLine();
				
				// find the answer
				if(answerA.startsWith("*")){
					answerIdx = 1;
					answerA = answerA.substring(1);
				} else if(answerB.startsWith("*")){
					answerIdx = 2;
					answerB = answerB.substring(1);
				} else if(answerC.startsWith("*")){
					answerIdx = 3;
					answerC = answerC.substring(1);
				} else if(answerD.startsWith("*")){
					answerIdx = 4;
					answerD = answerD.substring(1);
				}
				
				int gameidx = Integer.parseInt(fileList[i].substring(0, fileList[i].indexOf(".")));
				TriviaObject.add(new GuiObjectTrivia(-Assets.screenwidth/2 + 50, -Assets.screenheight/2 + 50, Assets.screenwidth - 100, Assets.screenheight/2, false, tr, question, answerA, answerB, answerC, answerD, answerIdx, gameidx));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return TriviaObject;
	}
	
	public boolean touched(Vector3 point){
		if(currentTriviaIdx >= 0){
			if(TriviaObject.get(currentTriviaIdx).visible){
				if(TriviaObject.get(currentTriviaIdx).touched(point)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean touchUp(Vector3 point){
		if(currentTriviaIdx >= 0){
			if(TriviaObject.get(currentTriviaIdx).visible){
				if(TriviaObject.get(currentTriviaIdx).touchedUp(point)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean visible(){
		if(currentTriviaIdx >=0){
			return TriviaObject.get(currentTriviaIdx).visible;
		}
		return false;
	}
	
	public boolean LoadTrivia(int gameIdx){
		for(int i=0; i<TriviaObject.size(); i++){
			if(TriviaObject.get(i).gameIdx == gameIdx){
				currentTriviaIdx = i;
				TriviaObject.get(currentTriviaIdx).visible = true;
				return true;
			}
		}
		currentTriviaIdx = -1;
		return false;
	}
	
	public void CloseAd(){
		if(currentTriviaIdx >= 0){
			TriviaObject.get(currentTriviaIdx).visible = false;
		}
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		if(currentTriviaIdx >=0){
			TriviaObject.get(currentTriviaIdx).draw(sb, deltaTime);
		}
	}
	
}

