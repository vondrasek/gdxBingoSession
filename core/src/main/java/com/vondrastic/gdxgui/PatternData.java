package com.vondrastic.gdxgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class PatternData {
	private static String parrentDirectory = "ibingo/patterns";
	private static String ext = "pat";
	
	public static int[] GetPatternData(String patternfile){
		
		return bytesToInts(GetBytesFromFileGDX(parrentDirectory + "/" + patternfile));
	
	}
	// Write out the pattern data to a file
	public static void SavePatternData(String filename, int[] patdata){
		WriteBytesToFileGDX(parrentDirectory + "/" + filename + "." + ext, intsToBytes(patdata));
	}
	
	public static String[] GetPatternList(){
		FileHandle fh = Gdx.files.local(parrentDirectory);
		String[] s = null;
		if(fh.exists()){
			if(fh.isDirectory()){
				FileHandle[] list = fh.list(ext);
				if(list.length > 0){
					s = new String[list.length];
					for(int i=0; i < list.length; i++){
						s[i] = list[i].name();
					}
				}
			}
		}
		
		return s;
		// Get a list of pattern files
	}
	
	private static byte[] GetBytesFromFileGDX(String filename){
		
		FileHandle fh = Gdx.files.local(filename);
		byte[] fileContent = null;
		
		if(fh.exists()){
			fileContent = fh.readBytes(); // read whole file
		}
		
		return fileContent;
	}

	private static void WriteBytesToFileGDX(String filename, byte[] data){
		
		FileHandle fh = Gdx.files.local(filename);
		fh.writeBytes(data, false);
		
	}

	private static int[] bytesToInts(byte[] src) {
	    int srcLength = src.length;
	    int[] dst = new int[srcLength / 4];
	    
	    for (int i = 0; i < srcLength; i+=4) {
	        int j = i / 4;
	        int temp = ((src[i] & 0xff) << 24 ) + ((src[i+1] & 0xff) << 16 ) + ((src[i+2] & 0xff) << 8 ) + ((src[i+3] & 0xff) ); 
	        dst[j] = temp;
	    }
	    return dst;
	}
	
	private static byte[] intsToBytes(int[] src) {
	    int srcLength = src.length;
	    byte[] dst = new byte[srcLength << 2];

	    for (int i = 0; i < srcLength; i++) {
	        int x = src[i];
	        int j = i << 2;
	        dst[j++] = (byte) ((x >>> 24) & 0xff);
	        dst[j++] = (byte) ((x >>> 16) & 0xff);
	        dst[j++] = (byte) ((x >>> 8) & 0xff);
	        dst[j++] = (byte) ((x >>> 0) & 0xff);
	    }
	    return dst;
	}
}
