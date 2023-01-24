

package com.vondrastic.gdxgui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;

public class Settings {
	// Game settings
	public static final boolean DEBUG = true;
	public static boolean server = true;
	public static int debugWifiPacketCount = 0;
	
	public static boolean soundEnabled = true;
	public static int cardView = 3;
	public static boolean cardFullscreen = false;
	public static boolean keypadAutohide = false;
	public static boolean wifiBallCall = true;
	public static String DEVICEID = null;
	public static String MACADDRESS = null;
	public static String HOSTNAME = null;
	public static String IPADDRESS = null;
	public static String bingoSalesServer = "http://192.168.1.248/";
	
	// FTP Server Area
	public static String FtpSalesServer = "192.168.1.248";
	public static String FtpSalesPassword = "ibingo";
	public static String FtpSalesUsername = "ibingo";
	
	public static String FtpAdServer = "192.168.1.248";
	public static String FtpAdPassword = "ibingo";
	public static String FtpAdUsername = "ibingo";
	
	public static String FtpTriviaServer = "192.168.1.248";
	public static String FtpTriviaPassword = "ibingo";
	public static String FtpTriviaUsername = "ibingo";
	
	// Note %25 is = to % 
	public static boolean winnerAlert = true;
	
	public static boolean VIBRATEONTOUCH = true;
	
	// Admin settings
	public static String serverip = "win7macbook15";
	public static int serverport = 2222;
	public static int udpPort = 1414;
	
	public final static int[] highscores = new int[] {100, 80, 50, 30, 10};
	public final static String file = ".iBingoSettings";

	public static void load () {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(file).read()));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < 5; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
			}
		} catch (Throwable e) {
			// :( It's ok we have defaults
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
	}

	public static void save () {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(file).write(false)));
			out.write(Boolean.toString(soundEnabled));
			for (int i = 0; i < 5; i++) {
				out.write(Integer.toString(highscores[i]));
			}

		} catch (Throwable e) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
			}
		}
	}

	public static void addScore (int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}
}