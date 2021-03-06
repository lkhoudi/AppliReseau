package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.EtatGame;
import entities.Group;
import entities.User;
import org.json.JSONObject;

import Serveur.parser.ConnectionBuilder;

public class ThreadUserForServer extends Thread{
	private Serveur serveur;
	private Socket socketUser;
	private BufferedReader reader;
	private PrintWriter writer;
	private User user;
	private Group groupe;
	private EtatGame etatOfUser=new EtatGame();
	private boolean deconnexion=false;
	
	public ThreadUserForServer(Socket clientServerSocket, Serveur serveur) {
		socketUser=clientServerSocket;
		this.serveur=serveur;
		groupe=null;
		try{
			reader=new BufferedReader(new InputStreamReader(socketUser.getInputStream()));
			writer= new PrintWriter(socketUser.getOutputStream(),true);
			treatMessageIfReceive();
			//sendMessage("information", "bonjour vous venez de vous connecter sur le port "+socketUser.getLocalPort());
		}catch(IOException e) {}
	}

	public void run() {

		while(!deconnexion){
			try{
				treatMessageIfReceive();
			}
			catch(Exception e){}
			
		}
		try{
			deconnecter();
			socketUser.close();
		}
		catch(IOException e){}
		
	}
	
	private void treatMessageIfReceive() {
		String str="";
		
		try {
			
			if(reader.ready()) {
				str=reader.readLine();
				
			}
			
			if((str!=null) && (! str.equals(""))) {
				treatMessage(str);
				
			}
		} catch (IOException e) {
			System.out.println(" le serveur n'arrive pas à recevoir le message du client ");
		}
		
	}

	/**
	 * 
	 */
	public void envoyerAllUsers() {
		sendMessage("users",serveur.getUsersJSON());
	}
	
	/**
	 * 
	 * @param user
	 */
	public void envoyerUser(User user) {
		sendMessage("user",user.toJsonString());
	}
	
	public void sendCoequipiers() {
		
		String usersString=groupe.getUsersJSONWithout(this);
		sendMessage("coequipiers",usersString);
		
		for(ThreadUserForServer threadUser: groupe.getUsers()) {
			if(!threadUser.equals(this)) {
				sendMessage("coequipier",user.toJsonString());
			}
		}
	}
	
	public void sendGroupes() {
		String usersString=serveur.getAllGroupeJson();
		sendMessage("groupes",usersString);
	}
	public Group getGroupe() {
		return groupe;
	}
	/**
	 * 
	 * @param message
	 */
	public void envoyer(String message) {
		writer.println(message);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object object ) {
		if(!(object instanceof ThreadUserForServer)) {
			return false;
		}
		
		ThreadUserForServer threadUser= (ThreadUserForServer) object;
		
		return user.equals(threadUser.user);
	}
	
	/**
	 * 
	 */
	public  void informerAutre() {
		if(serveur.getSizeUser()>1)
			sendMessage("users",serveur.getUsersJSONWithout(user));
		for(ThreadUserForServer threadUser : serveur.getListesUsersSocket()) {
			if(!threadUser.equals(this)) {
				threadUser.sendMessage("user",user.toJsonString());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public boolean estUser(String email) {
		return user.getEmail().equals(email);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void salutation(String info) {
		JSONObject message= new JSONObject();
		message.put("type", "salutation");
		JSONObject salutation = new JSONObject();
		salutation.put("email", user.getEmail());
		salutation.put("firstname", user.getFirstname());
		salutation.put("lastname", user.getLastname());
		salutation.put("avatar", user.getAvatar());
		message.put("salutation", salutation);
		message.put("message",info);
		envoyer(message.toString());
	}
	
	
	public void sendMessage(String type, String message) {
		JSONObject object=new JSONObject();
		object.put("type",type);
		object.put("data", message);
		envoyer(object.toString());
	}

	
	/**
	 * 
	 * @param
	 * @return
	 */
	public  boolean joindreUnGroupe(String label) {
		
		boolean test=false;
		
		if(groupe==null) {
			test=serveur.ajouterDansGroupe(label,this);
			sendMessage("tonGroupe",groupe.toJsonDescription());
			sendCoequipiers();
		}
		else {
			sendMessage("joindreGroupe","Vous avez déjà un groupe : "+groupe.getLabel());
			
		}

		return test;
	}
	
	/**
	 * 
	 * @param
	 * @return
	 */
	public  boolean creerGroupe(String label,String theme) {
		
		 
		
		boolean test=false;
		System.out.println("vous tentez de créer un groupe ");
		if(groupe==null) {
			
			test=serveur.creerGroupe(label,theme,this);
			if(test)
				System.out.println(" le groupe est créé");
			serveur.sendAll("groupe",groupe.toJsonDescription());
			sendMessage("tonGroupe",groupe.toJsonDescription());
		}
		else {
			sendMessage("creerGroupe","Vous avez déjà un groupe : "+groupe.getLabel());
			System.out.println("vous ne pouvez pas créer groupe "+groupe.toJsonDescription());
		}

		return test;
	}
	
	public  boolean quitterGroupe(String label) {
			
		
		boolean test=false;
		
		if(groupe!=null&&(groupe.getLabel().toLowerCase().equals(label.toLowerCase()))) {
			test=serveur.quitterGroupe(label,this);
			if(test==true) {
				groupe=null;
				sendMessage("quitterGroupe","Vous venez de quitter le groupe :"+label);
			}
		}
		else {
			sendMessage("quitterGroupe","Vous n'etes dans un groupe");
		}

		return test;
	}
	/**
	 * 
	 * @param message
	 */
	public  void treatMessage(String message) {
		
		if((message!=null)&&(!message.equals(""))){

			JSONObject object= new JSONObject(message);
			String type=object.getString("type");
			
			if(type.equals("réponse")) {
				
				System.out.println(object.getJSONObject("data"));
				groupe.setResponse(this, object.getJSONObject("data").toString());
				
			}
			else
			if(type.equals("creerGroupe")) {

				creerGroupe(object.getString("data"),object.getString("theme"));
			}
			else
			if(type.equals("joindreGroupe")) {
				 joindreUnGroupe(object.getString("data"));
			}
			else
			if(type.equals("commencer")) {
				etatOfUser.setEtat("pret");
				sendMessage("etat","vous venez de changer d'état maintenant tu es pret ");
			}
			else
			if(type.equals("etatConnexion")) {
				
			}
			else
			if(type.equals("inscrire")) {
				//System.out.println(object.getJSONObject("data").toString());
				inscrire(object.getJSONObject("data").toString());
				sendGroupes();
			}
			else
			if(type.equals("connexion")) {
				System.out.println(object.getJSONObject("data").toString());
				connexion(object.getJSONObject("data").toString());
				sendGroupes();
			}
			else
			if(type.equals("quitterGroupe")) {
				quitterGroupe(object.getString("data"));
			}
			else
			if(type.equals("deconnecter")) {
				deconnecter();
			}
			else
			if(type.equals("coequipiers")) {
				sendCoequipiers();
			}
			else
			if(type.equals("groupes")) {
				sendGroupes();
			}
			else
			if(type.equals("message")) {
				System.out.println(object.getString("data"));
				//System.out.println(object.getJSONObject("data").toString());
				retransmettreMessage(object.getString("data"));
			}
			

			
		}
		
	}
	
	public void retransmettreMessage(String message) {
		for(ThreadUserForServer threadUser: groupe.getUsers()) {
			if(!threadUser.equals(this)) {
				threadUser.sendMessage("message",message);
			}
		}
	}
	
	public  void deconnecter() {
		serveur.deconnecterUser(this);
		deconnexion=true;
		sendMessage("fin","vous n'etes plus connecté");
	}
	public  void setEtatOfUser(String etat) {
		etatOfUser.setEtat(etat);
	}
	
	public boolean estPret() {
		return etatOfUser.estPret();
	}
	
	public void setGroup(Group group) {
		groupe=group;
	}
	
	public boolean hasGroup() {
		return groupe!=null;
	}
	
	
	public   boolean connexion(String jsonString) {
		boolean test=false;
		JSONObject objectUser=new JSONObject(jsonString);
		String email=objectUser.getString("email");
		String password=objectUser.getString("password");
		
		//verification de la db
		//instanciation de user et serveur puis informerautre
		user= new User(email," "," ",password);
		user.setAvatar("");
		serveur.addUser(user);
		serveur.addUserSocket(this);
		informerAutre();
		return test;
	}
	
	public  EtatGame getEtatUser() {
		return etatOfUser;
	}
	
	public   void inscrire(String jsonString) {
		JSONObject objectUser=new JSONObject(jsonString);
		String firstname=objectUser.getString("firstname");
		String lastname=objectUser.getString("lastname");
		String email=objectUser.getString("email");
		String avatar=objectUser.getString("avatar");
		user= new User(email,firstname, lastname,avatar);
		
		if(objectUser.has("password")) {
			user.setPassword(objectUser.getString("password"));
			insertionDB();
		}
		serveur.addUser(user);
		serveur.addUserSocket(this);
		informerAutre();
		//inserer l'utilisateur dans la base de donn�es
		
	}
	
	
	public boolean insertionDB()  {
		boolean test=false;
		ConnectionBuilder.setInfo("RESEAU", "root", "");
		try {
			ConnectionBuilder.insertData(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return test;
	}
}
