package com.vondrastic.gdxgui.lwjgl3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main implements NativeFunctions{
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "iBingo";
		//cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 736;
		//cfg.height = 704;
		//cfg.width = 1024;
		Main game = new Main();
		new LwjglApplication(new IbingoGame(game), cfg);
	}

	@Override
	public boolean openDatabase(String databasename) {
		// TODO Auto-generated method stub
		// If the db does not exist then create it with the scripts
		return false;
	}

	@Override
	public boolean createDatabase(String sql) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dbSQLSelect(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dbSQLUpdate(String table, String field, String where) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBatteryCharging() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDeviceID() {
		// TODO Auto-generated method stub
		return "RUNNING WINDOWS";
	}

	@Override
	public String getWifiState() {
		// TODO Auto-generated method stub
		InetAddress ip;
		try {
	 
			ip = InetAddress.getLocalHost(); 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress();
	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
			}
			return sb.toString();
	 
		} catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		} catch (SocketException e){
	 
			e.printStackTrace();
	 
		}
		return "NONE";
	}

	@Override
	public boolean vibrate(long durration) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getHostName() {
		// TODO Auto-generated method stub
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getWifiIpaddress() {
		// TODO Auto-generated method stub
		
		return "TODO";
	}

	@Override
	public void getVideoPlayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InetAddress getWifiBroadcastAddress() throws IOException {
		InetAddress i;
		i = InetAddress.getByName(getHostName());
		//String ipString = i.getHostAddress();
		byte[] address;
		address = i.getAddress();
		address[3] = (byte)255; // set to broadcast
		i = InetAddress.getByAddress(address);
		//ipString = String.format( "%d.%d.%d.%d",(address[0] & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		return i;
		//return InetAddress.getByName("192.168.5.255");
	}

	@Override
	public long CrcCheck() throws IOException { // NOTE: Add iBingo.jar to end if running in debug
		JarFile jf = new JarFile(getClass().getProtectionDomain().getCodeSource().getLocation().getFile() ); //+ "iBingo.jar");
	    long aCrc = 0;
		Enumeration e = jf.entries();
	    while (e.hasMoreElements()) {
	      JarEntry je = (JarEntry) e.nextElement();
	      String name = je.getName();
	      if(name.endsWith(".class")){
	    	  long crc = je.getCrc();
	    	  aCrc = aCrc + crc;
	      }
	    }
		return aCrc;
	}

	@Override
	public void LaunchDingoApp() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public float setBrightnessLevel(){
		return 0;
	}

	public float turnScreenOn(){
		return 0;
	}

	public float turnScreenOff(){
		return 0;
	}

	@Override
	public void UpdateApplication() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getWifiConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWifiState(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getWifiLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean releaseWifiLock() {
		// TODO Auto-generated method stub
		return false;
	}
}
