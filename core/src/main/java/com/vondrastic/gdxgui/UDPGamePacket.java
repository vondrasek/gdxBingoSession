package com.vondrastic.gdxgui;

public class UDPGamePacket {

	public static final int recType_Session_Broadcast = 1001;
	public static final int recType_Trivia_Broadcast = 2001;
	
	public static byte[] encodeBingoPacket(int recType, int packetID, int hallID, int gameID, Flashboard75 fb){
		// Get flashboard to ints
		int[] data = new int[12];
		byte[] bdata;
		data[0] = recType;
		data[1] = packetID;
		data[2] = hallID;
		data[3] = gameID;
		// Flashboard
		for(int n=0; n<5; n++){
			for(int i=0; i<15; i++){
				if(fb.BOARDMASK[i+(n*15)] == 1){
					data[n+4]|= (1<<i); 
				}
			}
		}
		data[9] = fb.LASTBALL;
		data[10] = 0; // patternMask
		data[11] = 0; // CRC16 rolling 
		
		bdata = Utils.intsToBytes(data);
		return bdata;
	}
	
	public static UDPGameRecord decodeBingoPacket(byte[] packet){
		
		UDPGameRecord rec = new UDPGameRecord();
	
		if(packet.length == 48){
			int[] unPacked = Utils.bytesToInts(packet);
			rec.recType = unPacked[0];
			rec.packetID = unPacked[1];
			rec.hallID = unPacked[2];
			rec.gameID = unPacked[3];
			rec.fb_b = unPacked[4];
			rec.fb_i = unPacked[5];
			rec.fb_n = unPacked[6];
			rec.fb_g = unPacked[7];
			rec.fb_o = unPacked[8];
			rec.lastball = unPacked[9];
			rec.patternActive = unPacked[10];
			rec.crc16 = unPacked[11];
		}
		return rec;
	}
}
