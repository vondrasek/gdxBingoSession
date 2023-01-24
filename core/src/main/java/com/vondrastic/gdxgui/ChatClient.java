package com.vondrastic.gdxgui;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatClient implements Runnable {

	// The client socket
	  private static Socket clientSocket = null;
	  // The output stream
	  private static PrintStream os = null;
	  // The input stream
	  private static DataInputStream is = null;
	  private static BufferedReader ins = null;
	  private static BufferedReader inputLine = null;
	  private static boolean closed = true;
	  private static List<String> messageList = new ArrayList<String>();
	  
	  public static boolean Connect() {

	    // The default port.
	    int portNumber = 2222;
	    // The default host.
	    String host = "192.168.1.108";
	    /*
	     * Open a socket on a given host and port. Open input and output streams.
	     */
	    try {
	      clientSocket = new Socket(host, portNumber);
	      inputLine = new BufferedReader(new InputStreamReader(System.in));
	      os = new PrintStream(clientSocket.getOutputStream());
	      //is = new DataInputStream(clientSocket.getInputStream());
	      ins = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    } catch (UnknownHostException e) {
	      System.err.println("Don't know about host " + host);
	      return false;
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O for the connection to the host "
	          + host);
	      return false;
	    }

	    /*
	     * If everything has been initialized then we want to write some data to the
	     * socket we have opened a connection to on the port portNumber.
	     */
	    if (clientSocket != null && os != null && ins != null) {
	        /* Create a thread to read from the server. */
	        new Thread(new ChatClient()).start();
	        closed = false;
	        return true;
	    }
	    return false;
	    
	  }
	  
	  public static boolean isConnected(){
		  return !closed;
	  }
	  
	  public static boolean SendMessageToSvr(String msg){
		  if(!closed) {
	          os.println(msg);
	          return true;
		  }
		  return false;
	  }
	  public static String GetNextMessage(){
		  String s = null;
		  if( messageList.size() >=1){
			  s = messageList.get(0);
			  messageList.remove(0);
		  }
		  return s;
	  }
	  public static void AddMessage(String msg){
		  messageList.add(msg);
	  }
	  
	  public static void Close(){
		 
	    try {
	    	os.close();
			ins.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	  }

	  /*
	   * Create a thread to read from the server. (non-Javadoc)
	   * 
	   * @see java.lang.Runnable#run()
	   */
	  public void run() {
	    /*
	     * Keep on reading from the socket till we receive "Bye" from the
	     * server. Once we received that then we want to break.
	     */
	    String responseLine;
	    try {
	      while ((responseLine = ins.readLine()) != null) {
	        //Add message to que;
	    	AddMessage(responseLine);
	        if (responseLine.indexOf("*** Bye") != -1) break;
	        
	      }
	      closed = true;
	      Close();
	    } catch (IOException e) {
	      System.err.println("IOException:  " + e);
	    }
	  }
	}