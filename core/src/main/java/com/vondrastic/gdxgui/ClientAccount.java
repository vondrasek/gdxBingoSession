package com.vondrastic.gdxgui;
// Manage players account. Credits
public class ClientAccount {
	private String LOGIN; // Email address
	private String PASSWORD; // Minimum 8 alpha and numeric
	private int CREDITS;
	private String GAMERTAG;
	
	private ClientAccount(String login, String password){
		// Get account for player and update credits. 
		this.LOGIN = login;
		this.PASSWORD = password;
		// Site IPAddress to connect to HTTPS
		
		
	}
	
	public boolean tryLogin(){
		
		return false;
	}
	
	public boolean createAccount(){
		
		return false;
	}
	
	public boolean getAccountData(){
		
		return false;
	}
	
	public boolean saveAccouuntData(){
		
		return false;
	}

}
