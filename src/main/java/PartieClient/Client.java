package PartieClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

import entities.User;




public class Client {
	
	private User user;
	private InetAddress serveurAdresse;
	private int portServeur;
	private Socket socketClient;
	private BufferedReader reader; 
	private PrintWriter writer; 
	
	public Client(String email, String firstname, String lastname,int port, InetAddress adresse) {
		user= new User( email,firstname,  lastname);
		setServerLocation(adresse,port);
	}
	
	
	public Client(String email, String firstname, String lastname) {
		user= new User( email,firstname,  lastname);
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
						reponse=treatMessage();
					
				}
			}
		});
		
		connexionThread.start();
		
		return true;
	}
	
	public String treatMessage() {
		String str="";
		try {
			if(reader.ready()) {
				
				str=reader.readLine();
				if(!str.equals(""))
					System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public void repondre(int idQuestion, String response) {
			JSONObject object= new JSONObject();
			object.put("type", "reponse");
			object.put("data", response);
			envoyerSMS(object.toString());
	}
	
	private void envoyer(String type, String message) {
		JSONObject object= new JSONObject();
		JSONObject objectUser=new JSONObject(message);
		object.put("type", type);
		object.put("data", objectUser);
		
		envoyerSMS(object.toString());
	}
	public void envoyerSMS(String str) {
		writer.println(str);
	}
	
	public void creerGroupe(String label,String theme) {
		
		JSONObject object= new JSONObject();
		object.put("type", "creerGroupe");
		object.put("data", label);
		object.put("theme", theme);
		envoyerSMS(object.toString());
	}
	
	public void rejoindreGroupe(String label) {
		JSONObject object =new JSONObject();
		object.put("type", "joindreGroupe");
		object.put("data", label);
		
		envoyerSMS(object.toString());
	}
	
	public void quitterGroupe(String label) {
		JSONObject object =new JSONObject();
		object.put("type", "quitterGroupe");
		object.put("data", label);
		
		envoyerSMS(object.toString());
	}
	public void inscrire() {
		
		JSONObject objectUser = new JSONObject();
        objectUser.put("email", user.getEmail());
        objectUser.put("firstname", user.getFirstname());
        objectUser.put("lastname", user.getLastname());
        objectUser.put("avatar", user.getAvatar());
        objectUser.put("password", user.getPassword());
        envoyer("inscrire",objectUser.toString());
        
	}
	
	public void deconnecter() {
		JSONObject object =new JSONObject();
		object.put("type", "deconnecter");
		object.put("data", "deconnecter");
		
		envoyerSMS(object.toString());
	}
	
	public void commencer() {
		JSONObject object=new JSONObject();
		object.put("type","commencer");
		envoyerSMS(object.toString());
	}
	
	public static void main(String[] agrs) throws InterruptedException {
		Client client= new Client("4deZZZZ","soume","avatar");
		
		client.setServerLocation("192.168.56.1", 8990);
		
		if(client.connectionServer()) {
			client.communication();
			client.creerGroupe("team","animals");
			client.commencer();
		}
		
		Client client2= new Client("4","soume","avatar");
		
		client2.setServerLocation("192.168.56.1", 8990);
		if(client2.connectionServer()) {
			client2.communication();
			client2.creerGroupe("team2","animals");
			client2.commencer();
		}
		
		Thread.sleep(60000);
		client.deconnecter();
		client2.deconnecter();
	}
}
