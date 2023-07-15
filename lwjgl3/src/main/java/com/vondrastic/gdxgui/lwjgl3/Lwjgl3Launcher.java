package com.vondrastic.gdxgui.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.vondrastic.gdxgui.NativeFunctions;
import com.vondrastic.gdxgui.gdxGuiGame;

import java.io.IOException;
import java.net.InetAddress;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher implements NativeFunctions {
	public static void main(String[] args) {
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		Lwjgl3Launcher RUNME = new Lwjgl3Launcher();
		return new Lwjgl3Application(new gdxGuiGame(RUNME), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("GdxGuiGame");
		configuration.useVsync(true);
		//// Limits FPS to the refresh rate of the currently active monitor.
		configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
		//// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
		//// useful for testing performance, but can also be very stressful to some hardware.
		//// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
		configuration.setWindowedMode(1600, 900);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

		return configuration;
	}

	@Override
	public boolean openDatabase(String databasename) {
		return false;
	}

	@Override
	public boolean createDatabase(String sql) {
		return false;
	}

	@Override
	public void dbSQLSelect(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {

	}

	@Override
	public void dbSQLUpdate(String table, String field, String where) {

	}

	@Override
	public float getBatteryLevel() {
		return 0;
	}

	@Override
	public boolean getBatteryCharging() {
		return false;
	}

	@Override
	public String getDeviceID() {
		return null;
	}

	@Override
	public String getHostName() {
		return null;
	}

	@Override
	public String getWifiState() {
		return null;
	}

	@Override
	public String getWifiIpaddress() {
		return null;
	}

	@Override
	public boolean vibrate(long durration) {
		return false;
	}

	@Override
	public void getVideoPlayer() {

	}

	@Override
	public InetAddress getWifiBroadcastAddress() throws IOException {
		return null;
	}

	@Override
	public long CrcCheck() throws IOException {
		return 0;
	}

	@Override
	public void LaunchDingoApp() {

	}

	@Override
	public float setBrightnessLevel() {
		return 0;
	}

	@Override
	public float turnScreenOn() {
		return 0;
	}

	@Override
	public float turnScreenOff() {
		return 0;
	}

	@Override
	public void UpdateApplication() {

	}

	@Override
	public boolean getWifiConnected() {
		return false;
	}

	@Override
	public void setWifiState(boolean state) {

	}

	@Override
	public boolean getWifiLock() {
		return false;
	}

	@Override
	public boolean releaseWifiLock() {
		return false;
	}
}