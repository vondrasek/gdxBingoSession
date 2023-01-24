package com.vondrastic.gdxgui;

import java.io.File;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Utils {
	
	//Helper function to draw text.. Expand to Have more than one line. 
	// Also support left, center, right
	public static float getTextBoundsWidth(GlyphLayout layout){
		//GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
		//layout.setText(" "); //
		float width = layout.width;// contains the width of the current set text
		return width;
	}
	public static void DrawText(SpriteBatch sb, BitmapFont font, float x, float y,float w, float h, String text){
		
		float width = font.getBounds(text).width;
		float height = font.getBounds(text).height;

		if(width > w){// Split into parts
			int start = text.length()/2;
			int t1 = text.indexOf(" ", start);
			String T1 = text.substring(0, t1);
			String T2 = text.substring(t1);
			float msgWidthT1 = Assets.font32.getBounds(T1).width;
			float msgWidthT2 = Assets.font32.getBounds(T2).width;
			font.draw(sb, T1, x + (w / 2) - (msgWidthT1 / 2)  , y + (h - height));
			font.draw(sb, T2, x + (w / 2) - (msgWidthT2 / 2)  , y + (h - (h*2)));
		}else{
			font.draw(sb, text , x + (w / 2) - (width / 2), y + (h - height));
		}
	}
	
	public static void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	public static int[] bytesToInts(byte[] src) {
	    int srcLength = src.length;
	    int[] dst = new int[srcLength / 4];
	    
	    for (int i = 0; i < srcLength; i+=4) {
	        int j = i / 4;
	        int temp = ((src[i] & 0xff) << 24 ) + ((src[i+1] & 0xff) << 16 ) + ((src[i+2] & 0xff) << 8 ) + ((src[i+3] & 0xff) ); 
	       
	        dst[j] = temp;
	    }
	    return dst;
	}
	
	public static byte[] intsToBytes(int[] src) {
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
	
	public static byte[] shortToBytes(short src, boolean littleEndian){
		byte[] dst = new byte[2];
		// Little Endian
		if(littleEndian){
			dst[0] = (byte) src;
			dst[1] = (byte) (src >> 8);
		}else{
	    // Big Endian
			dst[0] = (byte) (src >> 8);
			dst[1] = (byte) src;
		}
		return dst;
	}
	
	public static double getAngle(double Sx, double Sy, double Ex, double Ey )
	{
		double Rad2Deg = 180.0 / Math.PI;
		//double Deg2Rad = Math.PI / 180.0;
		return Math.atan2(Sy - Ey , Sx - Ex) * Rad2Deg;
	}
	
	public static Vector2 getPoint(double angle, float distance, Vector2 center) {
        // Angles in java are measured clockwise from 3 o'clock.
        double theta = Math.toRadians(angle);
        Vector2 p = new Vector2();
        
        p.x = (float) (center.x + distance*Math.cos(theta));
        p.y = (float) (center.y + distance*Math.sin(theta));
        
        return p;
    }
	
	public static float getDistance(float a, float b){
		return (float) Math.sqrt((a * a) + (b * b));
	}
	
	public static float getVelocityX(double AngleRadian, double speed){
		return (float)(Math.cos(AngleRadian) * speed);
	}
	
	public static float getVelocityY(double AngleRadian, double speed){
		return (float)(Math.sin(AngleRadian) * speed);
	}
	
	public static int NbrOfBitsSet(int i){
		
	    i = i - ((i >> 1) & 0x55555555);
	    i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
	    return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
	
	}
	
	public static int getPosByBit(int n){
		for(int i=0; i<25; i++){
			if((n >> i) == 1){
				return i;
			}
		}
		return -1;
	}
	
	public static boolean overlapRectangles (Rectangle r1, Rectangle r2) {
		if (r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y)
			return true;
		else
			return false;
	}

	public static boolean pointInRectangle (Rectangle r, Vector2 p) {
		return r.x <= p.x && r.x + r.width >= p.x && r.y <= p.y && r.y + r.height >= p.y;
	}

	public static boolean pointInRectangle (Rectangle r, float x, float y) {
		return r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y;
	}

}
