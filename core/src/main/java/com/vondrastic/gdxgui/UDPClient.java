package com.vondrastic.gdxgui;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient implements Runnable {
	
	@Override
	public void run() {
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();		
			/* Prepare some data to be sent. */
			byte[] buf = ("Hello from Client").getBytes();
			/* Create UDP-packet with 
			 * data & destination(url+port) */
			DatagramPacket send_packet = new DatagramPacket(buf, buf.length, serverAddr, Settings.serverport);
			/* Send out the packet */
			socket.send(send_packet);
			
		} catch (Exception e) {
			
		}
	}
}