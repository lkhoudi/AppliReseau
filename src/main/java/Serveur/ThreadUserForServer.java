package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class ThreadUserForServer extends Thread{
	private Serveur serveur;
	private Socket socketUser;
	private BufferedReader reader;
	private PrintWriter writer;
	private User user;
	public ThreadUserForServer(Socket clientServerSocket, Serveur serveur) {
		socketUser=clientServerSocket;
		this.serveur=serveur;
		try{
			reader=new BufferedReader(new InputStreamReader(socketUser.getInputStream()));
			writer= new PrintWriter(socketUser.getOutputStream(),true);
			String reponse=reader.readLine();
			JSONObject object= new  JSONObject(reponse);
			user= new User(object.getString("number"),object.getString("name"),object.getString("avatar"));
			serveur.addUser(user);
			informerAutre();
			writer.println("bonjour vous venez de vous connecter sur le port "+socketUser.getLocalPort());
		}catch(IOException e) {}
	}

	public void run() {
		boolean ok=true;
		int i=0;
		while(i<15){
			try{
				JSONObject message= new JSONObject();
				message.put("type", "salutation");
				JSONObject salutation = new JSONObject();
				salutation.put("number", user.getNumber());
				salutation.put("name", user.getName());
				
				message.put("salutation", salutation);
				message.put("message",i);
				writer.println(message.toString());
				Thread.sleep(500);
				i++;
				if(message.equals("ok"))
					ok=false;
				
			}
			catch(Exception e){}
			
		}
		writer.println("fin");
		try{
			socketUser.close();
		}
		catch(IOException e){}
		
	}
	
	public void envoyerAllUsers() {
		writer.println(serveur.getUsersJSON());
	}
	
	public void envoyerAllUser(User user) {
		writer.println(user.toJson());
	}
	
	public void envoyer(String message) {
		writer.println(message);
	}
	
	@Override
	public boolean equals(Object object ) {
		if(!(object instanceof ThreadUserForServer)) {
			return false;
		}
		
		ThreadUserForServer threadUser= (ThreadUserForServer) object;
		
		return user.equals(threadUser.user);
	}
	
	public void informerAutre() {
		for(ThreadUserForServer threadUser : serveur.getListesUsersSocket()) {
			if(!threadUser.equals(this)) {
				threadUser.envoyer(threadUser.getUser().toJson());
			}
		}
	}
	
	public User getUser() {
		return user;
	}
	
	
	
}
