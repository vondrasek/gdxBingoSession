package com.vondrastic.gdxgui;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class SessionDataLoader {
	
	private static  String configPath;
	private static  String patternConfigPath;
	private static  String patternDataPath;
	
	//One header for session
	public SessionBingoConfig SessionHeader = new SessionBingoConfig();
	// Multiple Game Records
	public List<BingoGameConfig> GameRecord = new ArrayList<BingoGameConfig>();
	// Multiple pattern records
	public List<BingoPatternConfig> PatternRecord = new ArrayList<BingoPatternConfig>();
	
	public SessionDataLoader(String rootPath){
		configPath = rootPath + "var/xdl/data/config.tmp";
		patternConfigPath = rootPath + "var/xdl/data/patidx.pat";
		patternDataPath = rootPath + "var/xdl/data/patdata.pat";
	}
	
	// -------------------------------------------------------------------
	// Now read the config to load the data into the session structures. 
	// -------------------------------------------------------------------
	public boolean LoadSessionData(){
		RandomAccessFile in;
		RandomAccessFile in2;
		
		byte[] tempId = new byte[24];
		
		try {
			in = new RandomAccessFile(configPath , "r");
			
			if(in.length() > 1){
				SessionHeader.TotalGameRecord = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				in.read(tempId, 0, 24);
				SessionHeader.FormatName = new String(tempId);
				SessionHeader.ReceiptID = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.SaleType = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.PlayerNbr = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.PlayerCredits = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.ValidSessionNbr = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.BingoSound = (byte)in.readUnsignedByte();
				SessionHeader.DaubModeAllowed = (byte)in.readUnsignedByte();
				SessionHeader.GamesAllowed = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				SessionHeader.hours = (byte)in.readUnsignedByte();
				SessionHeader.volume = (byte)in.readUnsignedByte();
				// Load game headers in loop
				for(int i=0; i<SessionHeader.TotalGameRecord; i++){
					GameRecord.add(new BingoGameConfig());
					GameRecord.get(i).gamenbr = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
						in.read(tempId, 0, 24);
					GameRecord.get(i).gamedesc = new String(tempId);
						in.read(tempId, 0, 24);
					GameRecord.get(i).prizedesc = new String(tempId);
					GameRecord.get(i).cardColor = (byte)in.readUnsignedByte();
					GameRecord.get(i).GameType = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).NbrOfDiffernPatMatchReq = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
						in.read(tempId, 0, 24);
					GameRecord.get(i).permfile = new String(tempId);
					GameRecord.get(i).ExtraPurchaseAllowed = (byte)in.readUnsignedByte();
					GameRecord.get(i).MaxCardsAllowed =(int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					
					GameRecord.get(i).l1permqty = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l1permstart = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l2permqty = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l2permstart = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l3permqty = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l3permstart = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l4permqty = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).l4permstart = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
				
					GameRecord.get(i).NbrOfPatternPacks = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					
					in.read(tempId, 0, 24);
					GameRecord.get(i).Pattern1Name = new String(tempId);
					in.read(tempId, 0, 24);
					GameRecord.get(i).Pattern2Name = new String(tempId);
					in.read(tempId, 0, 24);
					GameRecord.get(i).Pattern3Name = new String(tempId);
					in.read(tempId, 0, 24);
					GameRecord.get(i).Pattern4Name = new String(tempId);
					
					GameRecord.get(i).page_col = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).page_row = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).page_drop = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).page_ho = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					GameRecord.get(i).page_vo = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					
					GameRecord.get(i).res1 = (byte)in.readUnsignedByte();
					GameRecord.get(i).res2 =(byte)in.readUnsignedByte();
				}
				in.close();
				in = new RandomAccessFile(patternConfigPath , "r");
				in2 = new RandomAccessFile(patternDataPath , "r");
				long filelen = in.length();
				long reccnt = filelen / 36;
				for(int i = 0; i< reccnt; i++){
					PatternRecord.add(new BingoPatternConfig());
					PatternRecord.get(i).idx = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					PatternRecord.get(i).cnt = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 );
						in.read(tempId, 0, 24);
					PatternRecord.get(i).name = new String(tempId);
					PatternRecord.get(i).dataOffset = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 | in.readUnsignedByte() << 16 | in.readUnsignedByte() << 24) ;
					PatternRecord.get(i).res = (int)(in.readUnsignedByte() | in.readUnsignedByte() << 8 );
					// Reset data file to beginning
					in2.seek(PatternRecord.get(i).dataOffset - 1);
					
					PatternRecord.get(i).data = new int[PatternRecord.get(i).cnt];
					for(int n = 0; n< PatternRecord.get(i).data.length; n++){
						PatternRecord.get(i).data[n] = (int)(in2.readUnsignedByte() | in2.readUnsignedByte() << 8 | in2.readUnsignedByte() << 16 | in2.readUnsignedByte() << 24) ;
					}
				}
				
				in.close();
				in2.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
