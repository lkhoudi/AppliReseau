package PartieClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

import Serveur.parser.ParserJson;
import entities.Question;
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
			System.out.println(" probl�me : le serveur n'est pas disponible");
			test=false;
		} 
		
		return test;
	}
	
	public boolean communication() {
		
		Thread connexionThread = new Thread( new Runnable() {
			
			public void run() {
				String reponse="";
				while(!reponse.equals("fin")) {
						reponse=treat();
					
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
	
	public String treat() {
		try {
			if(reader.ready()) {
				String message=reader.readLine();
				treadMSG(message);
				System.out.println(message);
				return message;
			}
		} catch (IOException e) {
				System.out.println(" pas de message ");
		}
		return "";
	}
	
	
	public void treadMSG(String message) {
	
		if((message!=null)&&(!message.equals(""))){
			System.out.println(message);
			JSONObject object= new JSONObject(message);
			String type=object.getString("type");
			
			if(type.equals("question")) {
				String objectQuestion =object.getString("data");
				System.out.println(objectQuestion);
				afficherQuestion(objectQuestion);
			}
			else
			if(type.equals("creerGroupe")) {
				System.out.println(object.getString("data"));
			}
			else
			if(type.equals("joindreGroupe")) {
				System.out.println(object.getString("data"));
			}
			else
			if(type.equals("reponseGroupe")) {
				System.out.println(object.getString("data"));
			}
			else
				if(type.equals("users")) {
					
				}
				else
				System.out.println(message);
			
		}
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
	
	public void afficherQuestion(String question) {
		System.out.println(question);
		Question quest=ParserJson.parserQuestion(question);
		
		System.out.println(" Question "+quest.getQuestion());
		Object proposition[]=quest.getPropositions().toArray();
		
		for(int i=0; i<proposition.length;i++) {
			System.out.println(" "+i+ " :"+proposition[i].toString());
			// id to answer
		}
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

		Client client= new Client("njebzub","Rania1","avatar");
		
		
		String add="192.168.43.65";
		String add2="172.17.2.206";
		
		client.setServerLocation(add2, 8990);
		if(client.connectionServer()) {
			client.communication();
			client.creerGroupe("team","animals");
			client.commencer();
		}
		Client client2= new Client("4","Rania2","avatar");
		
		client2.setServerLocation(add2, 8990);
		if(client2.connectionServer()) {
			client2.communication();
			client2.creerGroupe("team2","animals");
			Thread.sleep(5000);
			client2.commencer();
		}
		
		Thread.sleep(60000);
		client.deconnecter();
		client2.deconnecter();
	}
}
