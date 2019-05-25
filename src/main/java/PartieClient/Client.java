package PartieClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import entities.User;
import org.json.JSONObject;


public class Client {
	
	private User user;
	private InetAddress serveurAdresse;
	private int portServeur;
	private Socket socketClient;
	private BufferedReader reader; 
	private PrintWriter writer; 
	
	public Client(String email, String firstname, String lastname, String avatar,int port, InetAddress adresse) {
		user= new User( email, firstname, lastname, avatar);
		setServerLocation(adresse,port);
	}
	
	public Client(String email, String firstname, String lastname, String avatar) {
		user= new User( email, firstname, lastname, avatar);
	}
	
	public void setServerLocation(InetAddress adresse, int port ) {
		this.serveurAdresse=adresse;
		this.portServeur=port;
	}
	
	public void setServerLocation(String adresse, int port ) {
		try {
			this.serveurAdresse=InetAddress.getByName(adresse);
			this.portServeur=port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private boolean connectionServer()  {
		try {
			socketClient = new Socket(serveurAdresse, portServeur);
			reader= new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			writer = new PrintWriter(socketClient.getOutputStream(),true);
			JSONObject object = new JSONObject();
	        object.put("number", user.getEmail());
	        object.put("name", user.getFirstname());
	        object.put("avatar", user.getAvatar());
			writer.println(object.toString());
			return true;
		} catch (IOException e) {
			System.out.println(" probl�me : le serveur n'est pas disponible");
			return false;
		} 
	}
	
	public boolean communication() {
		
		Thread connexionThread = new Thread( new Runnable() {
			
			public void run() {
				String reponse="";
				while(!reponse.equals("fin")) {
					try {
						reponse=reader.readLine();
						System.out.println(reponse);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		connexionThread.start();
		
		return true;
	}
	
	
	public static void main(String[] agrs) {
		Client client= new Client("soume@gmail.com","ibrahima","soume","avatar");
		
		client.setServerLocation("192.168.56.1", 8990);
		
		if(client.connectionServer()) {
			client.communication();
		}
		
	}
}
