

package com.vondrastic.gdxgui;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings {
	// Game settings
	public static final boolean DEBUG = true;
	public static boolean server = false;
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
	public static String bingoSalesServer = "http://127.0.0.1/";
	
	// FTP Server Area
	public static String FtpSalesServer = "127.0.0.1";
	public static String FtpSalesPassword = "TheWormInTheCore";
	public static String FtpSalesUsername = "bingo";
	
	public static String FtpAdServer = "127.0.0.1";
	public static String FtpAdPassword = "TheWormInTheCore";
	public static String FtpAdUsername = "bingo";
	
	public static String FtpTriviaServer = "127.0.0.1";
	public static String FtpTriviaPassword = "TheWormInTheCore";
	public static String FtpTriviaUsername = "bingo";
	
	// Note %25 is = to % 
	public static boolean winnerAlert = true;
	
	public static boolean VIBRATEONTOUCH = true;
	
	// Admin settings
	public static String serverip = "127.0.0.1";
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
