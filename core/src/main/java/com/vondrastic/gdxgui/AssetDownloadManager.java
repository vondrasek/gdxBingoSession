package com.vondrastic.gdxgui;

import java.io.File;

import com.badlogic.gdx.Gdx;

public class AssetDownloadManager extends Thread{

	private String Login = "";
	public boolean success = false;
	public boolean failed = false;
	public String status = "";
	
	public AssetDownloadManager(String login){
		Login = login;
	}
	
	@Override
	public void run() {
		
		if(DownloadContent(Login)){
			success = true;
		}else{
			failed = true;
		}
	}
	private void updateStatus(String s){
		status = s;
	}
	private boolean DownloadContent(String login){
	
		int cnt = 0;
		
		// Check for Demo Login and Load
		if(login.equals("1234567") && Settings.DEBUG){
			return true;
		}
		
		File d = new File(Gdx.files.getExternalStoragePath()+"/.iBingoData");
		//File o = new File(Gdx.files.getExternalStoragePath()+"/.iBingoDelete");
		if(d.exists()){
			//d.renameTo(o);
			Utils.DeleteRecursive(d);
		}
		
		d.mkdir();
		
		String localRootPath = Gdx.files.getExternalStoragePath() + "/.iBingoData/";
	
		AssetDownloader ADL = new AssetDownloader();
		updateStatus("Connecting to server");
		if(ADL.OpenServerConn(Settings.FtpSalesServer, Settings.FtpSalesUsername, Settings.FtpSalesPassword)){
			updateStatus("Downloading Session Data");
			if(ADL.DownloadData(localRootPath + ".session.zip", login + ".zip")){
				updateStatus("Extracting Sesison Data");
				if(ADL.unpackZip(localRootPath + ".session.zip", localRootPath)){
					updateStatus("Locking Session Load");
					if(ADL.RenameFile(login + ".zip", login + ".zip_" + Settings.MACADDRESS)){
						cnt++;
					}
					//cnt++;
				}
			}else if(ADL.DownloadData(localRootPath + ".session.zip", login + ".zip_"+ Settings.MACADDRESS)){
				updateStatus("Extracting Sesison Data");
				if(ADL.unpackZip(localRootPath + ".session.zip", localRootPath)){
					cnt++;
				}
			} else{
				return false;
			}
			updateStatus("Downloading Ad Data");
			if(ADL.DownloadData(localRootPath + ".ads.zip", "ads.zip")){
				updateStatus("Extracting Ads Data");
				if(ADL.unpackZip(localRootPath + ".ads.zip", localRootPath)){
					cnt++;
				}
			}
			updateStatus("Downloading Trivia Data");
			if(ADL.DownloadData(localRootPath + ".trivia.zip", "trivia.zip")){
				updateStatus("Extracting Trivia Data");
				if(ADL.unpackZip(localRootPath + ".trivia.zip", localRootPath)){
					cnt++;
				}
			}
		}
		
		if(cnt == 3){
			updateStatus("Done!");
			ADL.CloseServerConn();
			return true;
		}else{
			ADL.CloseServerConn();
			return false;
		}
	}
	
	private boolean DownloadContentDemo(String login){
		
		//Gdx.files.internal("data/1234567.zip");
		//Gdx.files.internal("data/ads.zip");
		Gdx.files.getLocalStoragePath();
		
		int cnt = 0;
		File d = new File(Gdx.files.getExternalStoragePath()+"/.iBingoData");
		
		if(d.exists()){
			Utils.DeleteRecursive(d);
		}
		
		d.mkdir();
		
		String localRootPath = Gdx.files.getExternalStoragePath() + "/.iBingoData/";
	
		AssetDownloader ADL = new AssetDownloader();
	
		updateStatus("Extracting Sesison Data");
		if(ADL.unpackZip("data/1234567.zip", localRootPath)){	
				cnt++;
		}
	
		updateStatus("Extracting Ads Data");
		if(ADL.unpackZip("data/ads.zip", localRootPath)){
			cnt++;
		}
	
		updateStatus("Extracting Trivia Data");
		if(ADL.unpackZip("data/trivia.zip", localRootPath)){
			cnt++;
		}
		
		if(cnt == 3){
			updateStatus("Done!");
			ADL.CloseServerConn();
			return true;
		}else{
			ADL.CloseServerConn();
			return false;
		}
	}

}
