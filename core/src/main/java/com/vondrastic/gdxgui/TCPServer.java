package com.vondrastic.gdxgui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable{
	 String clientSentence;
     String capitalizedSentence;
     int portNbr = 1001;
     
     public TCPServer(int port){
    	 portNbr = port;
     }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(portNbr);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	     while(true)
	     {
	        Socket connectionSocket = null;
			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        BufferedReader inFromClient = null;
			try {
				inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        DataOutputStream outToClient = null;
			try {
				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				clientSentence = inFromClient.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Received: " + clientSentence);
	        capitalizedSentence = clientSentence.toUpperCase() + '\n';
	        try {
				outToClient.writeBytes(capitalizedSentence);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }	
	}
}

