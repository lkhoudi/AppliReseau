package PartieClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;


public class Client {
	
	private User user;
	private InetAddress serveurAdresse;
	private int portServeur;
	private Socket socketClient;
	private BufferedReader reader; 
	private PrintWriter writer; 
	
	public Client(String number, String name, String avatar,int port, InetAddress adresse) {
		user= new User( number,name,  avatar);
		setServerLocation(adresse,port);
	}
	
	public Client(String number, String name, String avatar) {
		user= new User( number,name,  avatar);
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
			System.out.println("probleme pour prendre en charge l'adresse");
		}
	}
	
	private boolean connectionServer()  {
		boolean test=false;
		try {
			socketClient = new Socket(serveurAdresse, portServeur);
			reader= new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			writer = new PrintWriter(socketClient.getOutputStream(),true);
			inscrire();
			test= true;
		} catch (IOException e) {
			System.out.println(" problème : le serveur n'est pas disponible");
			test=false;
		} 
		
		return test;
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
						//System.out.println("le client n'arrive pas à recevoir la réponse");
					}
				}
			}
		});
		
		connexionThread.start();
		
		return true;
	}
	
	public void repondre(int idQuestion, String response) {
			JSONObject object= new JSONObject();
			object.put("type", "reponse");
			object.put("data", response);
			envoyerSMS(object.toString());
	}
	
	public void envoyerSMS(String str) {
		writer.println(str);
	}
	
	public void creerGroupe(String label) {
		JSONObject object= new JSONObject();
		object.put("type", "creerGroupe");
		object.put("data", label);
		envoyerSMS(object.toString());
	}
	
	public void rejoindreGroupe(String label) {
		JSONObject object =new JSONObject();
		object.put("type", "joindreGroupe");
		object.put("data", label);
		
		envoyerSMS(object.toString());
	}
	public void inscrire() {
		JSONObject object= new JSONObject();
		object.put("type", "inscrire");
		JSONObject objectUser = new JSONObject();
        objectUser.put("number", user.getNumber());
        objectUser.put("name", user.getName());
        objectUser.put("avatar", user.getAvatar());
        objectUser.put("mdp", "mdp");
        object.put("data", objectUser);
        envoyerSMS(object.toString());
	}
	
	public static void main(String[] agrs) {
		Client client= new Client("4de","soume","avatar");
		
		client.setServerLocation("192.168.56.1", 8990);
		
		if(client.connectionServer()) {
			client.communication();
		}
		
	}
}
