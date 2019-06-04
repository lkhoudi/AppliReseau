package PartieClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

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
	private boolean deconnecter=false;
	
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
	
	public boolean connectionServer()  {
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
				while(!deconnecter) {
						treat();
				}
			}
		});
		
		connexionThread.start();
		
		return true;
	}
	

	
	public void repondre(int idQuestion, String response) {
			JSONObject object= new JSONObject();
			object.put("type", "réponse");
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
				return message;
			}
		} catch (IOException e) {
				System.out.println(" pas de message ");
		}
		return "";
	}
	
	
	public synchronized void treadMSG(String message) {
	
		if((message!=null)&&(!message.equals(""))){
			System.out.println(message);
			JSONObject object= new JSONObject(message);
			String type=object.getString("type");
			
			if(type.equals("question")) {
				String objectQuestion =object.getString("data");
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
				afficherUsers(object.getString("data"));
			}
			else
			if(type.equals("user")) {
				afficherUser(object.getString("data"));
			}
			else
			if(type.equals("fin")) {
				System.out.println(object.getString("data"));
				deconnecter=true;
			}
			else
			if(type.equals("message")) {
				System.out.println(" mesasge receive");
				System.out.println("voici le message qque vous avez recu "+ object.toString());
			}
			else
				System.out.println(message);
			
		}
	}
	
	public void afficherUsers(String message) {
		
		List<User> users=ParserJson.parserListUser(message);
		if(users.isEmpty()) {
			System.out.println("vous etes le premier a vous connecté");
		}
		else
			System.out.println("voici la liste des utilisateurs qui sont en ligne");
		for(User user: users) {
			afficher(user);
		}
		
	}
	
	public void afficherUser(String message) {
		System.out.println(message);
		User user=ParserJson.parserUser(message);
		afficher(user);
	}
	
	public void afficher(User user) {
		if(user!=null) {
			System.out.println("email :"+user.getEmail()+" lastName "+user.getLastname()+" s'est connecté");
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

		Client client= new Client("mail","Rania1","avatar");
		
		
		String add="192.168.43.65";
		String add2="192.168.43.53";
		
		client.setServerLocation(add2, 8990);
		if(client.connectionServer()) {
			client.communication();
			client.creerGroupe("team","animals");
			//client.commencer();
		}
		Client client2= new Client("2","Rania2","avatar");
		
		client2.setServerLocation(add2, 8990);
		if(client2.connectionServer()) {
			client2.communication();
			client2.creerGroupe("team2","animals");
			
			//client2.commencer();
		}
		
		Client client3= new Client("3","Rania3","avatar");
		
		client3.setServerLocation(add2, 8990);
		if(client3.connectionServer()) {
			client3.communication();
			client3.rejoindreGroupe("team2" );
			
			//client3.commencer();
		}
		
		Client client4= new Client("4","Rania4","avatar");
		
		client4.setServerLocation(add2, 8990);
		if(client4.connectionServer()) {
			client4.communication();
			client4.rejoindreGroupe("team2" );
			
			Thread.sleep(5000);
			//client4.commencer();
		}
		
		Client client5= new Client("5","Rania5","avatar");
		Thread.sleep(10000);
		
		client5.setServerLocation(add2, 8990);
		if(client5.connectionServer()) {
			client5.communication();
			client5.creerGroupe("team3","animals");
			
			//client5.commencer();
		}
		
		
		Client client6= new Client("6","Rania5","avatar");
		//Thread.sleep(5000);
		
		client6.setServerLocation(add2, 8990);
		if(client6.connectionServer()) {
			client6.communication();
			client6.creerGroupe("team4","animals");
			
			//client5.commencer();
		}
		
		client3.envoyer("message", "{\"data\":comment vas}");
		client6.envoyer("message", "{\"data\":comment vas}");
		client5.envoyer("message", "{\"data\":comment vas}");
		client2.envoyer("message", "{\"data\":comment vas}");
		client4.envoyer("message", "{\"data\":comment vas}");
		//Thread.sleep(60000);
		//client.deconnecter();
		//client2.deconnecter();
	}
}
