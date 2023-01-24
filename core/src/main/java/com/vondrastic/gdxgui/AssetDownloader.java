package com.vondrastic.gdxgui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.badlogic.gdx.Gdx;

// Responsible for downloading all the content required for Playing the game
public class AssetDownloader {
	OutputStream output = null;
	FTPClient ftpclient = new FTPClient();
	
	public boolean RenameFile(String from, String to){
		if(ftpclient.isConnected()){
			try {
				if(ftpclient.rename(from, to)){
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean unpackZip(String zipname, String localPath)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = new FileInputStream(zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             filename = ze.getName();
	          // Need to create directories if not exists, or
	             // it will generate an Exception...
	             File destinationFilePath = new File(localPath, ze.getName());
	             destinationFilePath.getParentFile().mkdirs();
	             
	             if (ze.isDirectory()) {
	                File fmd = new File(localPath + filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(localPath + filename);
	            
	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();
	             zis.closeEntry();
	         }
	         
	         is.close();
	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }
	    
	    return true;
	}
	
	public boolean isConnected(){
		return ftpclient.isConnected();
	}
	
	public boolean OpenServerConn(String server, String username, String password){
		
		try {
			ftpclient.connect(server);
			ftpclient.login(username, password);
			ftpclient.setFileType( FTP.BINARY_FILE_TYPE);
			ftpclient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			ftpclient.enterLocalPassiveMode();
			if(ftpclient.isConnected()){
				ftpclient.setBufferSize(1024*1024);
				return true;
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	
	}
	
	public void CloseServerConn(){
		
		if(ftpclient.isConnected()){
			try {
				ftpclient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean DownloadData(String localPath, String downloadPath ){
		boolean dl_success = false;
		try {
			// Remove old data
			File oldFile = new File(localPath);
			if(oldFile.exists()){
				oldFile.delete();
			}
			
			output = new FileOutputStream(localPath);
			String modTime = ftpclient.getModificationTime(downloadPath);
			
			if(ftpclient.retrieveFile(downloadPath, output) == false){
				return false;
			}else{
				dl_success = true;
			}
	
			output.close();
			
			if(dl_success){
				return true;
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
