package com.vondrastic.gdxgui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class AdManager {
	
	List<GuiObjectAd> AdObject;
	int currentAdIdx = -1;
	
	public AdManager(String adFolderPath){	
		AdObject = getAdObjects(adFolderPath);
		// Place Ad on Screen Where you want them
		for(int i=0; i<AdObject.size(); i++){
			AdObject.get(i).setPosition(Assets.screenwidth/2 - AdObject.get(i).getWidth(), Assets.screenheight/2 - AdObject.get(i).getHeight());
		}
	}
	
	public List<GuiObjectAd> getAdObjects(String adFolderExtracted){
		
		List<GuiObjectAd> AdObject = new ArrayList<GuiObjectAd>();
		AdObject.clear(); // Remove all Ads
		File d = new File(adFolderExtracted);
		String[] fileList = d.list();
		for(int i=0; i<fileList.length; i++){
			String path = adFolderExtracted + "/" + fileList[i];
			FileHandle fh = new FileHandle(new File(path));
			if(fh.exists()){
				TextureRegion tr = new TextureRegion(new Texture(fh));
				int gameidx = Integer.parseInt(fileList[i].substring(0, fileList[i].indexOf(".")));
				AdObject.add(new GuiObjectAd(0, 0, tr.getRegionWidth(), tr.getRegionHeight(), tr, gameidx));
			}
		}
		
		return AdObject;
	}
	
	public boolean visible(){
		
		if(currentAdIdx >= 0){
			return AdObject.get(currentAdIdx).visible;
		}
		
		return false;
	}
	
	public boolean touched(Vector3 point){
		if(currentAdIdx >= 0){
			if(AdObject.get(currentAdIdx).touched(point)){
				AdObject.get(currentAdIdx).visible = false;
				return true;
			}
		}
		return false;
	}

	public boolean LoadAd(int gameIdx){
		for(int i=0; i<AdObject.size(); i++){
			if(AdObject.get(i).gameIdx == gameIdx){
				currentAdIdx = i;
				AdObject.get(currentAdIdx).visible = true;
				return true;
			}
		}
		currentAdIdx = -1;
		return false;
	}
	
	public void CloseAd(){
		if(currentAdIdx >= 0){
			AdObject.get(currentAdIdx).visible = false;
		}
	}
	
	public void draw(SpriteBatch sb, float deltaTime){
		
		if(currentAdIdx >=0){
			AdObject.get(currentAdIdx).draw(sb, deltaTime);
		}
	}
	
}
