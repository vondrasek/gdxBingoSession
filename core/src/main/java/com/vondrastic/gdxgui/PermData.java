package com.vondrastic.gdxgui;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
//TODO: Support Double Action Perms. 
public class PermData {
	private static int headerlen = 0x24;
	private static int reclen = 13;
	
	private static String permpath = "data/bingodata";
	private static String permext = "prm";
	
	public static byte[][] GetCardData(String permfile, int cardidx, int permtype){
		if(permtype == 0){
			return formatCardData(GetBytesFromFileGDX(permpath + "/" + permfile, headerlen + ((cardidx-1) * reclen), reclen));
		}else{
			return formatCardData(GetBytesFromFileGDX(permpath + "/" + permfile, headerlen + ((cardidx-1) * (reclen * 2)), reclen*2));
		}
	}
	
	public static String[] GetPermList(){
		FileHandle fh = Gdx.files.local(permpath);
		String[] s = null;
		if(fh.exists()){
			if(fh.isDirectory()){
				FileHandle[] list = fh.list(permext);
				if(list.length > 0){
					s = new String[list.length];
					for(int i=0; i < list.length; i++){
						s[i] = list[i].name();
					}
				}
			}
		}	
		return s;
	}
	
	private static byte[][] formatCardData(byte[] data){
		byte[][] formatted;
		if(data.length == reclen){
			formatted = new byte[5][5];
		}else{
			formatted = new byte[5][10];
		}
		int i = 0;
		byte z = 1;
		short tmp = 0;
		short high = (short)0xF0;
		short low = (short)0x0F;
		boolean on = true;
		try {
			for(int y=0; y < 5; y++){
				for(int x=0; x < 5; x++){
					tmp = (byte)data[i];
					if(tmp==0 && x!= 2 && y!=2){
						//error
					}
					if(on){
						tmp = (byte)((tmp & high) >> 4);
						formatted[x][y] = (byte) (tmp + (x*15));
						z++;
					}else{
						tmp = (byte)(tmp & low);
						formatted[x][y] = (byte) (tmp + (x*15));
						i++; // Next byte when low
						z++;
					}
					on =! on; // flip the switch
				}
			}
			formatted[2][2] = 0; // Free Space
			if(data.length==reclen*2){
				i=13;
				on = true;
				for(int y=5; y < 10; y++){
					for(int x=0; x < 5; x++){
						tmp = (byte)data[i];
						if(tmp==0 && x!= 2 && y!=2){
							//error
						}
						if(on){
							tmp = (byte)((tmp & high) >> 4);
							formatted[x][y] = (byte) (tmp + (x*15));
							z++;
						}else{
							tmp = (byte)(tmp & low);
							formatted[x][y] = (byte) (tmp + (x*15));
							i++; // Next byte when low
							z++;
						}
						on =! on; // flip the switch
					}
				}
				formatted[2][7] = 0; // Free Space
			}
			
		} catch (Throwable e) {
			
		}
		return formatted;
	}
	
	private static byte[] GetBytesFromFileGDX(String filename, long start, int nbrofbytestoread){
		
		byte[] fileContent = new byte[nbrofbytestoread];
		
		FileHandle fh = Gdx.files.internal(filename);
		InputStream is = fh.read();
		
		try {
			is.skip(start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			is.read(fileContent, 0, nbrofbytestoread);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}
}
