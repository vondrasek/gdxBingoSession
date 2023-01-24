package com.vondrastic.gdxgui;

import java.io.IOException;
import java.net.InetAddress;

public interface NativeFunctions {
	
	public boolean openDatabase(String databasename);
	
	public boolean createDatabase(String sql);
	
	public void dbSQLSelect(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
	
	public void dbSQLUpdate(String table, String field, String where);
	
	public float getBatteryLevel();
	
	public boolean getBatteryCharging();
	
	public String getDeviceID();
	
	public String getHostName();
	
	public String getWifiState();
	
	public String getWifiIpaddress();
	
	public boolean vibrate(long durration);
	
	public void getVideoPlayer();
	
	public InetAddress getWifiBroadcastAddress() throws IOException;
	
	public long CrcCheck() throws IOException;
	
	public void LaunchDingoApp();

	float setBrightnessLevel();

	float turnScreenOn();

	float turnScreenOff();

	void UpdateApplication();

	boolean getWifiConnected();

	void setWifiState(boolean state);

	boolean getWifiLock();

	boolean releaseWifiLock();
}
