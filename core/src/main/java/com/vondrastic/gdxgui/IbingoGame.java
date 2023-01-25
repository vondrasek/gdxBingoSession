package com.vondrastic.gdxgui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.vondrastic.gdxgui.BingoCardManager.VIEWLAYOUT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class IbingoGame implements ApplicationListener, GameInterface {
	public NativeFunctions mNativeFunctions; // ADDED to support Desktop and Android as they are different for each
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private String debugLog = "";

	private CameraMan CM;
	private ParticleEffectObject PEO;
	
	private List <BingoCard> BCD = new ArrayList<BingoCard>();
	private PatternGroup PG = new PatternGroup("TEST", 1);
	private FlashBoardObject FBO = null;
	private Flashboard75 FB = new Flashboard75();
	private KeyPadObject KPO = null;
	private ActionBarObject ABO = null;
	private PatternInfoObject PIO = null;
	private MenuItemSelectObject DMO = null;
	private BatteryMonitorObject BMO = null;
	//private BingoBallObject BBO = null;
	private BingoCardManager BCM = null;
	private GuiObjectInfoBar BIB = null;
	private BackgroundObject BG = null;
	
	private SessionDataLoader SDL = null;
	private AdManager AM = null;
	private TriviaManager TM = null;
	private AssetDownloadManager ADM = null; // THread to download packages. 
	private float ADM_Timer = 0; // Keep track of how long thread is running
	private float ServerSendTimer = 0; // Matinance Send Packets. 
	private float ServerSendTime = 0.10f; 
	private SessionGameListObject SGO = null;
	private GuiObjectMsgBox MsgBox = null;
	
	private int dauberSelected = 0;
	
	private static UDPListener udpClient; // THread to listen to ball call. 
	private int dataPacketId = 0; // increment every different packet sent. Matainence packets will have the same id. 
	
	private int currentSessionGameIdx = -1;
	private String Login = "";
	private enum GAMEMODE{SPLASH, MAINMENU, IBINGO, DEUCEFOUR, LOGIN} // Manages what screen we are on. 
	private GAMEMODE GAMESTATE = GAMEMODE.SPLASH;
	private long appCrc32 = 0;
	
	private InputMultiplexer inputmultiplexer = new InputMultiplexer();

	// Added SQLInterface to support database on desktop and android versions
	public IbingoGame(NativeFunctions nativefunctions){
		mNativeFunctions = nativefunctions;
	}
	
	private void exitGame(){
		AssetDownloader ADL = new AssetDownloader();
		if(ADL.OpenServerConn(Settings.FtpSalesServer, Settings.FtpSalesUsername, Settings.FtpSalesPassword)){
			if(ADL.RenameFile(Login + ".zip_" + Settings.MACADDRESS, Login + ".zip")){
				ADL.CloseServerConn();
				ADL = null;
				GAMESTATE = GAMEMODE.SPLASH;
			}
		}
		//Unlock file
		
	}
	// TODO: Add NBC and checksum so we can just add one ball at a time instead of doing the whole thing to maintain state
	private boolean ServerSendGameInfo(){

		byte[] encodedPacket = UDPGamePacket.encodeBingoPacket(UDPGamePacket.recType_Session_Broadcast, dataPacketId, 2001, SDL.GameRecord.get(currentSessionGameIdx).gamenbr, FB);

		try {
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			DatagramPacket send_packet = new DatagramPacket(encodedPacket, encodedPacket.length, mNativeFunctions.getWifiBroadcastAddress(), Settings.udpPort);
			socket.send(send_packet);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void ClientGetUDPPacket(){
		UDPGameRecord rec;
		if(udpClient == null){return;}
		if(Settings.server == false ){
			if(udpClient.isNewMessage()){
				rec = UDPGamePacket.decodeBingoPacket(udpClient.getMessage());
				if(rec.packetID != dataPacketId ){
					if(SDL.GameRecord.get(currentSessionGameIdx).gamenbr != rec.gameID){
						if(SessionLoadGameByNbr(rec.gameID) == false){
							BIB.addMessage("Game Not Found " + rec.gameID, 3, 1);
							return;
						}
					}
					
					//if(SDL.SessionHeader.DaubModeAllowed == SessionBingoConfig.daubmode_auto){
						if(rec.lastball >= 0 && rec.lastball <= 75){
							List<Byte> newBalls = FB.setMask(rec.fb_b, rec.fb_i, rec.fb_n, rec.fb_g, rec.fb_o, rec.lastball);
							for(int i = 0; i<newBalls.size(); i++){
								GameAddNbr(newBalls.get(i));
							}
							 
							if(rec.lastball > 0){
								GameAddNbr((byte)rec.lastball);
							}
						}
					//}
					dataPacketId = rec.packetID;
				}
			}
		}
	}
	
	private void SessionNextGame(){
		if(currentSessionGameIdx+1 >= SDL.GameRecord.size()){
			return;
		}else{
			currentSessionGameIdx++;
		}
		SessionLoadGame(currentSessionGameIdx);
	}
	
	private void SessionBackGame(){
		if(currentSessionGameIdx-1 < 0){
			return;
		}else{
			currentSessionGameIdx--;
		}
		SessionLoadGame(currentSessionGameIdx);
	}
	
	private boolean SessionLoadGameByNbr(int nbr){
		for(int i=0; i<SDL.GameRecord.size();i++){
			if(SDL.GameRecord.get(i).gamenbr == nbr){
				SessionLoadGame(i);
				return true;
			}
		}
		return false;
	}
	// Session has been loaded properly. Now setup the Assets 
	// TODO: Add Offsets to load cards HV
	// Currently only works on sequential loads. Need to Fix to look at HVO
	private void SessionLoadGame(int idx){
		//Validate Game Change;
		if(SDL.GameRecord.size() <= idx || idx < 0 ){
			return;
		}
		
		if(idx == currentSessionGameIdx){
			return;
		}
		
		currentSessionGameIdx = idx;
		
		BCD.clear();
		int totalCards = SDL.GameRecord.get(idx).l1permqty + SDL.GameRecord.get(idx).l2permqty + SDL.GameRecord.get(idx).l3permqty + SDL.GameRecord.get(idx).l4permqty;
		// Current Cards
		String permfile = SDL.GameRecord.get(idx).permfile;
		permfile = permfile.trim();		  
		 int permtype = 0;
		 if(SDL.GameRecord.get(idx).GameType == 4){
			 permtype = 1;
		 }
		 int cardColor = SDL.GameRecord.get(idx).cardColor;
		 
		for(int i = 0; i < SDL.GameRecord.get(idx).l1permqty; i++){
			int id = SDL.GameRecord.get(idx).l1permstart + i;
			BCD.add(new BingoCard(id, 1, permfile,permtype));	
		}
		for(int i = 0; i < SDL.GameRecord.get(idx).l2permqty; i++){
			int id = SDL.GameRecord.get(idx).l2permstart + i;
			BCD.add(new BingoCard(id, 2, permfile,permtype));	
		}
		for(int i = 0; i < SDL.GameRecord.get(idx).l3permqty; i++){
			int id = SDL.GameRecord.get(idx).l3permstart + i;
			BCD.add(new BingoCard(id, 3, permfile,permtype));	
		}
		for(int i = 0; i < SDL.GameRecord.get(idx).l4permqty; i++){
			int id = SDL.GameRecord.get(idx).l4permstart + i;
			BCD.add(new BingoCard(id, 4, permfile,permtype));	
		}
		// Current Pattern
		// Find Pattern by name
		PG.clearPatterns();
		String gamePatternName = SDL.GameRecord.get(idx).Pattern1Name;
		PG.rename(gamePatternName);
		for(int i=0; i<SDL.PatternRecord.size(); i++){
			String PatternName = SDL.PatternRecord.get(i).name;
			if(gamePatternName.compareTo(PatternName) == 0){
				Pattern p = new Pattern( SDL.PatternRecord.get(i).name, 0, 0, (byte)0, (byte)0);
				for(int n=0; n<SDL.PatternRecord.get(i).cnt; n++){
					p.AddMatrix(SDL.PatternRecord.get(i).data[n]);
				}
				PG.addPattern(p);
				PIO.setPattern(PG);
				break;
			}
		}
		
		FBO.Reset();
		CreateCardManager(cardColor);
		
		//BIB.clearMessages();
		BIB.addMessage("Now Playing Game " + SDL.GameRecord.get(idx).gamedesc, 3, 1);
		BIB.addMessage("Pattern in Play is " + SDL.GameRecord.get(idx).Pattern1Name , 3, 1);
		BIB.addMessage("You have " + totalCards + " Cards in Play" , 3, 1);
		// UPDATE packet id that this is different. 
		dataPacketId++;
		if(AM!=null){
			AM.LoadAd(SDL.GameRecord.get(idx).gamenbr);
		}
		if(TM!=null){
			TM.LoadTrivia(SDL.GameRecord.get(idx).gamenbr);
		}
		
		if(Settings.server){
			ServerSendGameInfo();
		}
	}
	
	@Override
	public void create() {
		
		camera = new OrthographicCamera(Assets.screenwidth, Assets.screenheight);
		camera.position.set(0, 0, 0);
		
		camera.update();
		CM = new CameraMan(camera);
		batch = new SpriteBatch();
		// Set up Input
		Gdx.input.setCatchBackKey( true );
		Gdx.input.setCatchMenuKey(true);
		
		inputmultiplexer.addProcessor(new GameInputProcessor());
		inputmultiplexer.addProcessor(new GestureDetector(new GameGestureProcessor()));
		
		Gdx.input.setInputProcessor(inputmultiplexer);
		loadAssets();
		CreateBackground();
		CreateSplash();
		CreateFlashboard();
		CreatePatternInfoBox();
		
		CreateCardData();
		CreateCardManager(0);
		
		CreateKeyPad();
		CreateActionBar();
		CreateBatteryMonitor();
		CreateGuiDauberMenu();
		CreateBingoInfobar();
		
		CreateGameInfoMenu();
		
		PEO = new ParticleEffectObject(0, 0, false, Assets.colorballs_region.get(0), "data/effects/sparkle.p", "data/effects");
		PEO.start();
		
		if(Settings.server == false){
			//Launch UDPClient Listener. Has to be on a seperate thread as it blocks until packet received
			if(Settings.wifiBallCall){
				udpClient = new UDPListener();
				udpClient.start();
			}
		}else{
			BIB.addMessage("SERVER MODE", 1, 0);
		}
		try {
			appCrc32 = mNativeFunctions.CrcCheck();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Settings.DEVICEID = mNativeFunctions.getDeviceID();
		Settings.MACADDRESS = mNativeFunctions.getWifiState().replace(":", "-"); // added replace to comply with windows machines.
		Settings.HOSTNAME = mNativeFunctions.getHostName();
		Settings.IPADDRESS = mNativeFunctions.getWifiIpaddress();
		
	}
	@Override
	public void dispose() {
		batch.dispose();
		
		if(Settings.server == false){
			udpClient.stopRunning();
		}
		//TODO: Test below
		if(ADM!=null){
			if(ADM.isAlive()){
				ADM.interrupt();
			}
		}
		//TODO Destroy all the OBJECTS
		
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#render()
	 */
	@Override
	public void render() {	
		// Time from last from in seconds
		
		float deltatime = Gdx.graphics.getDeltaTime();
		//int fps = Gdx.graphics.getFramesPerSecond();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		// Draw all the objects on the screen
		if(GAMESTATE == GAMEMODE.SPLASH){
			//
			BG.draw(batch, deltatime);
			
			Assets.font32.draw(batch, " Touch To Enter Login:", -500, 50);
			Assets.font24.draw(batch, "crc= " + appCrc32 , -Assets.screenwidth/2, Assets.screenheight/2);
			BMO.draw(batch, deltatime); // Battery
			if(MsgBox != null){
				if(MsgBox.visible){
					MsgBox.draw(batch, deltatime);
				}
			}
		}else if(GAMESTATE == GAMEMODE.LOGIN){
			BG.draw(batch, deltatime);
			BMO.draw(batch, deltatime); // Battery
			
			Assets.font24.draw(batch, "crc= " + appCrc32 , -Assets.screenwidth/2, Assets.screenheight/2 - 100);
			if(ADM!=null){
				if(ADM.success){
					ADM = null;
					ADM_Timer = 0;
					LoadContent();
				}else if(ADM.failed){
					KPO.visible = true;
					ADM_Timer = 0;
					ADM = null; // set null and try again
				}else{ // Check Thread state.. Need to fix this.
					ADM_Timer += deltatime;
					if(ADM_Timer > 60){
						ADM_Timer = 0;
						ADM.interrupt();
						ADM = null;
					}else{
						Assets.font32.draw(batch, "Please wait. " + ADM.status + " : " + ADM.getState() , -Assets.screenwidth/2, Assets.screenheight/2);
						KPO.visible = false;
					}
				}
			}else{
				Assets.font32.draw(batch, " Enter Account Number From Receipt." , -Assets.screenwidth/2, Assets.screenheight/2);
				if(KPO!=null){
					KPO.visible = true;
				}
			}
			
			if(KPO.visible){
				KPO.draw(batch, deltatime);
			}
			
			if(MsgBox != null){
				if(MsgBox.visible){
					MsgBox.draw(batch, deltatime);
				}
			}
			
		}else if(GAMESTATE == GAMEMODE.MAINMENU){
			
			if(Settings.server == false){
				ClientGetUDPPacket();
			}else{
				ServerSendTimer += deltatime;
				if(ServerSendTimer>=ServerSendTime){
					ServerSendTimer = 0;
					ServerSendGameInfo();
					if(Settings.DEBUG){ // Update Send Packet Count
						Settings.debugWifiPacketCount++;
					}
				}
			}
			
			if(BG.visible){
				BG.draw(batch, deltatime);
			}
			
			if(BIB.visible){
				BIB.draw(batch, deltatime);
			}
			
			BCM.Draw(batch, deltatime);
			
			if(FBO.visible){
				FBO.Draw(batch, deltatime);
			}
			if(PIO.VISABLE){
				PIO.draw(batch, deltatime);
			}
			if(KPO.visible){
				KPO.draw(batch, deltatime);
			}
			//if(DMO.visible){
				DMO.draw(batch, deltatime);
			//}
			if(ABO.VISABLE){
				ABO.draw(batch, deltatime);
			}
			if(BMO.visible){
				BMO.draw(batch, deltatime); // Battery
			}
			if(SGO.visible){
				SGO.draw(batch, deltatime);
			}
			
			CM.update(deltatime); // CameraMan
			
			if(Settings.DEBUG){
				Assets.font24.draw(batch, "WIFI: " + Settings.debugWifiPacketCount , -(Assets.screenwidth/2), +(Assets.screenheight/2));
				//Assets.font24.draw(batch, "FPS:" + fps , -(Assets.screenwidth/2), +(Assets.screenheight/2));
				//Assets.font24.draw(batch, debugLog , 0,0);
			}
			
			if(AM != null && AM.visible()){
				AM.draw(batch, deltatime);
			}
			
			if(TM!=null && TM.visible()){
				TM.draw(batch, deltatime);	
			}
			if(MsgBox != null){
				if(MsgBox.visible){
					MsgBox.draw(batch, deltatime);
				}
			}
			PEO.draw(batch,deltatime);
			
		}else if(GAMESTATE == GAMEMODE.IBINGO){
			
		}
		
		batch.end();
		// Done drawing
	}

	@Override
	public void resize(int width, int height) {
		// Called by the application to set screen size
		if(Settings.DEBUG){
			String s = "resize:w=" + width + " h=" + height + "/n";
			debugLog = debugLog + s;
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	private void loadAssets(){
		
		Assets.load(); 
		
		//mNativeFunctions.openDatabase("bingodata");
	}
	
	private void CreateCardData(){
		// Create the Test Data
		BCD.clear();
		for(int i = 0; i < 3; i++){
			BCD.add(new BingoCard(i + 1, 1,"markall.prm",0));	
		}
	}
	private void CreateCardManager(int cardColor){
		FBO.visible = true;
		PIO.VISABLE = true;
		BCM = new BingoCardManager(-(Assets.screenwidth/2), -(Assets.screenheight/2) +80, Assets.screenwidth, Assets.screenheight/2, CM, PG,Assets.backgroundRegion,Assets.card_region.get(cardColor),Assets.dauber_region.get(dauberSelected),BCD );
		BCM.setCardDauber(Assets.dauber_region.get(dauberSelected));
	}
	// Create all the GUI elements
	private void CreateGuiDauberMenu(){
		//TODO: Check this area for a BUG Assets maybe doubling in size on resume
		float w = (Assets.screenwidth * 0.75f);
		float h = (Assets.screenheight * 0.75f);
		DMO = new MenuItemSelectObject(this, -(w/2), -(h/2), w, h, Assets.menu_bg, Assets.menu_accept.get(1), Assets.menu_cancel.get(1), MENU_CMD.DAUBER_ACCEPT, MENU_CMD.DAUBER_CANCEL, MENU_CMD.DAUBERS_DATA, "Dauber Selected ", false);

		for(int i=0; i<Assets.dauber_region.size(); i++){
			DMO.AddItem(Assets.dauber_region.get(i));
		}
		
		DMO.AddDone();
	}
	
	private void CreateGameInfoMenu(){
		
		float w = (Assets.screenwidth / 2);
		float h = (Assets.screenheight / 2);
		SGO = new SessionGameListObject(this, -(w/2), -(h/2), w, h, false, Assets.menu_bg, 48, Assets.menu_accept.get(1), Assets.menu_cancel.get(1), MENU_CMD.GAMES_SELECTED, MENU_CMD.GAMES_CANCEL, Assets.menu_nav_down.get(1), Assets.menu_nav_up.get(1));
		
	}

	private void CreateGuiOptionsMenu(){
		// Pass a options class to help with the GUIobjects
	}
	
	private void CreateBingoInfobar(){
		BIB = new GuiObjectInfoBar(-Assets.screenwidth / 2, 110, Assets.screenwidth, 64, true, Assets.guiBarRegion, "Game Information Bar", 1, 1);
	}
	
	private void CreateLoginSignUp(){
		
	}
	// To configure Option and local file server
	private void CreateAdminScreen(){
		
	}
	// Create a GUI tutorial
	private void CreateTutorial(){
		
	}
	
	private void CreateBackground(){
		BG = new BackgroundObject(-Assets.screenwidth/2, -Assets.screenheight/2, true, Assets.backgroundRegion);
	}
	
	//TODO Add Splash Screen Image. 
	private void CreateSplash(){
		BG = new BackgroundObject(-Assets.screenwidth/2, -Assets.screenheight/2, true, Assets.backgroundRegion);
	}
	
	
	
	private void GameAddNbr(byte nbr){
		int oneAwayCount = 0;
		boolean undaub = false;
		boolean winnerFound = false;
		if(nbr <= 0){return;}
		
		BIB.clearMessages();
		// Hack to remove dauber from card. Need to fix later
		if(FB.isCalled(nbr)){
			undaub = true;
		}
		FB.addBall(nbr);
		// Assets.playSound(Assets.ballcallSound[nbr -1]);
		for(int i = 0; i < BCD.size(); i++){
			if(undaub){
				BCD.get(i).undaub(nbr);
			}else{
				BCD.get(i).daub(nbr);
			}
			BCD.get(i).ScoreCard();
			if(BCD.get(i).IsOneAway()){
				oneAwayCount++;
				for(int n=0; n<BCD.get(i).GetOneAwayList().size(); n++){
					if(FBO.AddOneAway(BCD.get(i).GetOneAwayList().get(n))){
						Assets.playSound(Assets.oneawaySound);		
					}
				}
			}
			if(Settings.winnerAlert){
				if(BCD.get(i).IsWinner()){
					winnerFound = true;
					BIB.addMessage("Winning Card Found! " + BCD.get(i).getID() , 5, 0);
				}
			}
			
		}
		BIB.addMessage("You Entered # " + nbr , 3, 1);
		if(oneAwayCount > 0){
			BIB.addMessage("You have " + oneAwayCount + " Cards Set!", 5, 1);
		}
		if(winnerFound){
			Assets.playSound(Assets.winnerSound);
		}
		BCM.CardDataSort();
		
		if(Settings.server){
			dataPacketId++;
			ServerSendGameInfo();
		}
	}
	
	private void CreatePatternInfoBox(){
		float w = Assets.pattern_region.get(0).getRegionWidth();
		float h = Assets.pattern_region.get(0).getRegionHeight();
		w = 200; // was 164
		h = 200; // was 164
		float x = (Assets.screenwidth / 2) - w;
		float y = (Assets.screenheight / 2) - h;

		PIO = new PatternInfoObject(x, y, w, h, Assets.pattern_region.get(0),Assets.pattern_region.get(1), PG );
		
		Pattern p = new Pattern("line", 0, 0, (byte)0, (byte)0);
		p.AddMatrix("1111100000000000000000000");
		p.AddMatrix("0000011111000000000000000");
		p.AddMatrix("0000000000111110000000000");
		p.AddMatrix("0000000000000001111100000");
		p.AddMatrix("0000000000000000000011111");
		p.AddMatrix("1000010000100001000010000");
		p.AddMatrix("0100001000010000100001000");
		p.AddMatrix("0010000100001000010000100");
		p.AddMatrix("0001000010000100001000010");
		p.AddMatrix("0000100001000010000100001");
		PG.addPattern(p);
		
		PIO.setPattern(PG);
	}
	
	private void CreateActionBar(){
		int x = -(Assets.screenwidth/2);
		//int y = +(Assets.screenheight/2) -100 ;
		int y = -(Assets.screenheight/2) ;
		ABO = new ActionBarObject(this, ActionBarObject.ACTIONBAR_POSITION.BOTTOM, x, y, 80, 80, Assets.menu_cardview_less.get(1), Assets.menu_cardview_less.get(0));
		ABO.ShowParrent();
		ABO.CreateChildButton(Assets.menu_schedulelist.get(1), Assets.menu_schedulelist.get(1), MENU_CMD.SCHED_OPEN, MENU_CMD.SCHED_CLOSE);
		ABO.CreateChildButton(Assets.menu_dauberlist.get(1), Assets.menu_dauberlist.get(1), MENU_CMD.DAUBERS_CLOSE, MENU_CMD.DAUBERS_OPEN );
		ABO.CreateChildButton(Assets.menu_keypad.get(1), Assets.menu_keypad.get(1), MENU_CMD.KEYPAD_OPEN, MENU_CMD.KEYPAD_CLOSE);
		ABO.CreateChildButton(Assets.menu_volume_on.get(1), Assets.menu_volume_off.get(1), MENU_CMD.VOL_ON, MENU_CMD.VOL_MUTE);
		ABO.CreateChildButton(Assets.menu_zoom_out.get(1), Assets.menu_zoom_in.get(1), MENU_CMD.FULLSCREEN_ENTER, MENU_CMD.FULLSCREEN_EXIT);
		ABO.CreateChildButton(Assets.menu_exit, Assets.menu_exit, MENU_CMD.EXIT_MSGBOX, MENU_CMD.EXIT_MSGBOX);
		
	}
	
	private void CreateBatteryMonitor(){
		BMO = new BatteryMonitorObject(mNativeFunctions, Assets.screenwidth/2 -Assets.battery_info.get(0).getRegionWidth() , Assets.screenheight/2 - Assets.battery_info.get(0).getRegionHeight() ,true, Assets.battery_info.get(0), Assets.battery_info.get(1), Assets.battery_info.get(2));
	}
	
	private void CreateKeyPad(){
		
		KPO = new KeyPadObject((Assets.screenwidth / 2) - (128*3), -(Assets.screenheight / 2), 128 * 3, 128 * 4, true, Assets.keybtnUP_region, Assets.keybtnUP_region, Assets.keybtnDN_region, 0.8f);
		KPO.visible = false;
	}
	
	private void CreateFlashboard(){
		float x; float y; float w; float h;
		
		w = Assets.flashboard_region.get(0).getRegionWidth() * 1.6f;
		h = Assets.flashboard_region.get(0).getRegionHeight() * 1.6f;
		// x = -(w / 2); // Center
		x = -(Assets.screenwidth / 2) + 250; // Was 428
		y =  (Assets.screenheight / 2)- h;
		
		//FBO = new FlashBoardObject( -(Assets.flashboard_region.get(0).getRegionWidth() / 2), y, true, Assets.flashboard_region.get(0), FB);
		FBO = new FlashBoardObject(x, y, w, h, true, Assets.flashboard_region.get(0), FB);
	}
	
	private void CreateExitMsgBox(){
		MsgBox = new GuiObjectMsgBox(this, -300, -300, 600, 300, true, Assets.menu_bg, "CONFIRM EXIT",  "Are you sure you want to Exit!", MENU_CMD.EXIT_YES, MENU_CMD.EXIT_CANCEL);
	}
	private void UpdateAppMsgBox(){
		MsgBox = new GuiObjectMsgBox(this, -300, -300, 600, 300, true, Assets.menu_bg, "iBingo Update",  "Are you sure you want to Update?", MENU_CMD.UPDATE_YES, MENU_CMD.UPDATE_CANCEL);
	}
	private void Fullscreen_Toggle(){
		
		if(Settings.cardFullscreen){
			Settings.cardFullscreen = false;
			PIO.VISABLE = true;
			FBO.visible = true;
			BCM.SetCardView(VIEWLAYOUT.LARGE);
			BIB.setPosition(-Assets.screenwidth/2, 110);
		}else{
			Settings.cardFullscreen = true;
			PIO.VISABLE = false;
			FBO.visible = false;
			BCM.SetCardView(VIEWLAYOUT.MEDIUM);
			BIB.setPosition(-Assets.screenwidth/2, Assets.screenheight/2 - 64);
		}
		
	}
	@Override
	public void GameMessage(MENU_CMD MC) {
		if(MC == MENU_CMD.VOL_ON){ Settings.soundEnabled = true;}
		if(MC == MENU_CMD.VOL_MUTE){ Settings.soundEnabled = false;}
		if(MC == MENU_CMD.DAUBERS_OPEN){  
			if(DMO.visible){
				DMO.visible=false;
			}else{ 
				DMO.OpenAnimation(); 
				KPO.visible = false; 
				SGO.visible = false;
			}
		}
		if(MC == MENU_CMD.DAUBERS_CLOSE){ if(DMO.visible){ DMO.visible=false;}else{ DMO.OpenAnimation(); KPO.visible = false; SGO.visible = false;}}
		if(MC == MENU_CMD.FULLSCREEN_ENTER){Fullscreen_Toggle();}
		if(MC == MENU_CMD.FULLSCREEN_EXIT){Fullscreen_Toggle(); }
		if(MC == MENU_CMD.KEYPAD_OPEN){ if(KPO.visible){KPO.visible = false;}else{KPO.visible = true; SGO.visible = false; DMO.visible=false;}}
		if(MC == MENU_CMD.KEYPAD_CLOSE){if(KPO.visible){KPO.visible = false;}else{KPO.visible = true; SGO.visible = false; DMO.visible=false;}}
		if(MC == MENU_CMD.SCHED_OPEN){ if(SGO.visible){SGO.visible = false;}else{SGO.visible = true; KPO.visible = false; DMO.visible=false;}}
		if(MC == MENU_CMD.SCHED_CLOSE){if(SGO.visible){SGO.visible = false;}else{SGO.visible = true; KPO.visible = false; DMO.visible=false;}}
		if(MC == MENU_CMD.GAMES_NEXT){ SGO.scrollUp(); SessionNextGame();}
		if(MC == MENU_CMD.GAMES_BACK){SGO.scrollDn(); SessionBackGame();}
		if(MC == MENU_CMD.GAMES_SELECTED){SessionLoadGame(SGO.getItemSelected());  }
		if(MC == MENU_CMD.GAMES_CANCEL){ SGO.visible = false;}
		if(MC == MENU_CMD.EXIT_MSGBOX){ CreateExitMsgBox();}
		if(MC == MENU_CMD.EXIT_YES){ MsgBox.hide(); exitGame();}
		if(MC == MENU_CMD.EXIT_CANCEL){ MsgBox.hide(); MsgBox = null;}
		if(MC == MENU_CMD.QUIT_YES){ MsgBox.hide(); Gdx.app.exit();}
		if(MC == MENU_CMD.QUIT_CANCEL){ MsgBox.hide(); MsgBox = null;}
		if(MC == MENU_CMD.UPDATE_YES){ MsgBox.hide(); mNativeFunctions.UpdateApplication();}
		if(MC == MENU_CMD.UPDATE_CANCEL){ MsgBox.hide(); MsgBox = null;}
		
	}
	@Override
	public void GameMessage(MENU_CMD MC, int data) {
		if(MC == MENU_CMD.DAUBERS_DATA){ 
			dauberSelected = data;
			BCM.setCardDauber(Assets.dauber_region.get(dauberSelected));
			Assets.playSound(Assets.clickSound);
		}
	}
	@Override
	public void GameMessage(MENU_CMD MC, TextureRegion data) {
		if(MC == MENU_CMD.DAUBER_ACCEPT){
			
		}
	}
	
	@Override
	public void GameMessage(MENU_CMD MC, String data) {
		
	}
	@Override
	public void GameMessage(MENU_CMD MC, byte[] data) {
		
	}
	
	/**  Nested Class for Gesture Processing 
	*/
	private class GameGestureProcessor implements GestureListener  {

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			Vector3 vec = new Vector3();
			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			
			if(count == 1){	
				if(KPO.visible && KPO.touched(vec) == false){
					//KPO.visible = false;
					return true;
				}
			}
			
			if(count == 2){	
				if(KPO.visible == false ){
					KPO.MoveToTouch(vec.x, vec.y);
					KPO.visible = true;
					Assets.playSound(Assets.clickSound);
					return true;
				}
			}
			
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
//			
//			Vector3 vec = new Vector3();
//			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
//			if(CM.zoomedin){
//				CM.zoomOut();
//			}else{
//				CM.moveTo(vec.x, vec.y, 0, 10);
//			}
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			Vector3 vec = new Vector3();
			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			
			if(DMO.visible){
				//if(DMO.touched(vec)){
					if(velocityX > 0){
						DMO.PageLeft();
						Assets.playSound(Assets.swipeSound);
					}else{
						DMO.PageRight();
						Assets.playSound(Assets.swipeSound);
					}
					return true;
				//}
			}
			
			// Normalize the velocity
			if(velocityX < 2000 && velocityX > -2000){
				return false;
			}
			if(velocityX < 0){
				velocityX = -3225;
			}else{
				velocityX = 3225;
			}
			BCM.Fling(velocityX, 0);
			
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			
			return false;
		}

		@Override
		public void pinchStop() {

		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	/**  Nested Class for Input Processing */
	private class GameInputProcessor implements InputProcessor {
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {

			Vector3 vec = new Vector3();
			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			
			// Finger Sparkle
			PEO.visible = true;
			PEO.setPosition(vec.x - (PEO.getWidth()/2), vec.y- PEO.getHeight()/2);
			PEO.start();
			
			if(GAMESTATE == GAMEMODE.SPLASH){
				
				if(MsgBox != null){
					if(MsgBox.visible){
						if(MsgBox.touched(vec)){
							return true;
						}
					}
				}
				
				GAMESTATE = GAMEMODE.LOGIN;
				KPO.SetLoginMode();
				Assets.playSound(Assets.alert1Sound);
				
				return true;
			}else if(GAMESTATE == GAMEMODE.LOGIN){
				
				if(MsgBox != null){
					if(MsgBox.visible){
						if(MsgBox.touched(vec)){
							return true;
						}
					}
				}
				
				if(KPO.visible){
					if(KPO.touched(vec)){
						mNativeFunctions.vibrate(25);
						Assets.playSound(Assets.clickSound);
						return true;
					}
				}
				
				return false;
			}else if(GAMESTATE == GAMEMODE.MAINMENU){
				//if(TM.touched(vec)){
				//	return true;
				//}
				if(MsgBox != null){
					if(MsgBox.visible){
						if(MsgBox.touched(vec)){
							return true;
						}
					}
				}
				
				if(AM.visible()){
					if(AM.touched(vec)){
						return true;
					}
				}
				
				if(SGO.visible){
					if(SGO.touched(vec)){
						return true;
					}
				}
				
				if(DMO.visible){
					if(DMO.touched(vec)){
						return true;
					}
				}
				
				if(ABO.VISABLE){
					if(ABO.touched(vec)){
						return true;
					}
				}
				if(KPO.visible){
					if(KPO.touched(vec)){
						mNativeFunctions.vibrate(25);
						Assets.playSound(Assets.clickSound);
						return true;
					}
				}

				if(TM!=null){
					if(TM.touched(vec)){
						return true;
					}
				}
				
				//TODO Create Function for OnScreen ActionBar Support
				if(ABO.VISABLE){
					//ABO.HideParrent(); // Hack to Keep Buttons on screen
				}else{
					ABO.ShowParrent();
				}
			}
			
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			Vector3 vec = new Vector3();
			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			
			//Finger Sparks
			PEO.stop();
			
			if(GAMESTATE == GAMEMODE.MAINMENU){
				
				if(KPO.visible){
					if(KPO.Enter(vec)){
						byte nbr =(byte)KPO.GetKeypadNbr();
						if(nbr >0 && nbr <76){
							GameAddNbr(nbr);
						}
						KPO.reset(); // Clear the text;
						return true;
					}else if(KPO.touchedUP(vec)){
						return true;
					}
				}
				
				if(TM!=null){
					if(TM.touchUp(vec)){
						//Get Answer
						return true;
					}
				}
				
				if(DMO.visible){
					if(DMO.touchedUP(vec)){
						return true;
					}
				}
				
			}else if(GAMESTATE == GAMEMODE.LOGIN){
				
				if(ADM != null){ // Whit till thread is done
					return true;
				}
				
				if(KPO.Enter(vec)){
					int login = KPO.GetKeypadNbr();
					if(ADM == null){
						if(Integer.toString(login).length() >= 3){
							ADM = new AssetDownloadManager(Integer.toString(login));
							ADM.setPriority(Thread.MAX_PRIORITY);
							ADM.start();
							Login = String.valueOf(login);
							return true;
						}
					}
										
					KPO.reset();
					return true;
				}
				
				if(KPO.visible){
					if(KPO.touchedUP(vec)){
						return true;
					}
				}
			}
			
			return false;
		}
		@Override
		public boolean keyDown (int keycode) {
			// BackKey = 4
			// Menu Key = 82
        
			switch (keycode){
				case Input.Keys.MENU:  // MenuKey: Does not work on 4.0 android
					break;
				case Input.Keys.BACK: // BackKey
					if(GAMESTATE == GAMEMODE.LOGIN || GAMESTATE == GAMEMODE.SPLASH){
						UpdateAppMsgBox();
					}else{
						CreateExitMsgBox();
					}
					break;
			}
			
			return true;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {

			Vector3 vec = new Vector3();
			camera.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));// Have to get Different Cords as the function gives native java screen stuff. 
			// Finger Touch
			PEO.setPosition(vec.x - (PEO.getWidth()/2), vec.y- PEO.getHeight()/2);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}

	}
	
	private void LoadContent(){
		
		CreateGameInfoMenu();
		SDL = new SessionDataLoader(Gdx.files.getExternalStoragePath() + "/.iBingoData/");
		if(SDL.LoadSessionData()){
			AM = new AdManager(Gdx.files.getExternalStoragePath() + "/.iBingoData/iBingoAds");
			TM = new TriviaManager(Gdx.files.getExternalStoragePath() + "/.iBingoData/iBingoTrivia");
			SessionLoadGame(currentSessionGameIdx);
			KPO.SetBallEnterMode();
			KPO.reset(); // Clear the text;
			KPO.visible = false;
			BIB.addMessage("Session Game Information.", 5, 1);
			for(int i=0; i<SDL.GameRecord.size(); i++){
				SGO.AddGameInfo(SDL.GameRecord.get(i).gamenbr, SDL.GameRecord.get(i).gamedesc,SDL.GameRecord.get(i).Pattern1Name, SDL.GameRecord.get(i).GetCardCountTotal());
			}
			dataPacketId++;
			SessionLoadGame(0); // Load up the First Game
			GAMESTATE = GAMEMODE.MAINMENU;
		}
	}
}
