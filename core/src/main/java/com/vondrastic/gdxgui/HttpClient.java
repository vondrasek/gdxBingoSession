package com.vondrastic.gdxgui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {
 
	public static String DownloadData(String URL)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
            e1.printStackTrace();
            return "";
        }
         
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];         
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                   
                //---convert the chars to a String---
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }   
        return str;       
    }
	
	private static InputStream OpenHttpConnection(String urlString) throws IOException {
	    InputStream in = null;
	    int response = -1;
	            
	    URL url = new URL(urlString);
	    URLConnection conn = url.openConnection();
	              
	    if (!(conn instanceof HttpURLConnection))                    
	        throw new IOException("Not an HTTP connection");
	     
	    try{
	        HttpURLConnection httpConn = (HttpURLConnection) conn;
	        httpConn.setAllowUserInteraction(false);
	        httpConn.setInstanceFollowRedirects(true);
	        httpConn.setRequestMethod("GET");
	        httpConn.connect();
	 
            response = httpConn.getResponseCode();                
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                
            }                    
        } catch (Exception ex) {
	            throw new IOException("Error connecting");           
	    }
	    
	    return in;    
	}
}
