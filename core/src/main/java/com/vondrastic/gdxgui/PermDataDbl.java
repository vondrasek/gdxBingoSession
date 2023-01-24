package com.vondrastic.gdxgui;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
//TODO: SUppert Double Action Perms. 
public class PermDataDbl {
	private static int headerlen = 0x24;
	private static int reclen = 26;
	private static String permpath = "data/bingodata";
	private static String permext = "prm";
	
	public static CardDataDbl GetCardData(String permfile, int cardidx){
		return formatCardData(GetBytesFromFileGDX(permpath + "/" + permfile, headerlen + ((cardidx-1) * reclen), reclen));
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
	
	private static CardDataDbl formatCardData(byte[] data){
		CardDataDbl formatted = new CardDataDbl();
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
						formatted.DATA_1[x][y] = (byte) (tmp + (x*15));
						z++;
					}else{
						tmp = (byte)(tmp & low);
						formatted.DATA_1[x][y] = (byte) (tmp + (x*15));
						i++; // Next byte when low
						z++;
					}
					on =! on; // flip the switch
				}
			}
			formatted.DATA_1[2][2] = 0; // Free Space
			for(int y=0; y < 5; y++){
				for(int x=0; x < 5; x++){
					tmp = (byte)data[i];
					if(tmp==0 && x!= 2 && y!=2){
						//error
					}
					if(on){
						tmp = (byte)((tmp & high) >> 4);
						formatted.DATA_2[x][y] = (byte) (tmp + (x*15));
						z++;
					}else{
						tmp = (byte)(tmp & low);
						formatted.DATA_2[x][y] = (byte) (tmp + (x*15));
						i++; // Next byte when low
						z++;
					}
					on =! on; // flip the switch
				}
			}
			formatted.DATA_2[2][2] = 0; // Free Space
			
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
