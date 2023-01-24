package com.vondrastic.gdxgui;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPListener extends Thread {
	
	public static volatile byte[] recv = new byte[48];
	public static boolean newPacket = false;
	private static boolean running = false;
	
	public static boolean isNewMessage(){
		return newPacket;
	}
	
	public static byte[] getMessage(){
		newPacket = false;
		return recv.clone();
	}
	
	public static void stopRunning(){
		running = false;
	}
	
	@Override
	public void run() {
		try {
			//InetAddress serverAddr = InetAddress.getByName("0.0.0.0"); // WildCard Address
			DatagramSocket socket = new DatagramSocket(Settings.udpPort);
			byte[] buf = new byte[48];
			/* Prepare a UDP-Packet that can 
			 * contain the data we want to receive */
			DatagramPacket recv_packet = new DatagramPacket(buf, buf.length);
			/* Receive the UDP-Packet */
			running = true;
			while(running){
				socket.receive(recv_packet); // Blocks until Received
				if(recv_packet.getLength() == 48){// Check to see if we got the full packet
					recv = buf.clone();
					
					newPacket = true;
					if (Settings.DEBUG){
						Settings.debugWifiPacketCount ++;
					}
				}
			}
		} catch (Exception e) {

		}
	}
}
