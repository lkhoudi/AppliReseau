package Serveur;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import entities.Group;
import entities.User;
import game.Jeu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;

/**
 * 
 *
 */
public class Serveur extends Thread{
	List<Group> listesGroupes;
	List<ThreadUserForServer> listesUsersSocket;
	List<User> users;
	ServerSocket server ;
	int port ;

	/**
	 * 
	 * @param port
	 */
	public Serveur(int port)  {
		//InetSocketAddress sAddr = new InetSocketAddress(host, port);
		listesUsersSocket= (List<ThreadUserForServer>) new ArrayList<ThreadUserForServer>();
		System.out.println(" lancement du serveur");
		users=new ArrayList<User>();
		listesGroupes= new ArrayList<Group>();
		listesUsersSocket= (List<ThreadUserForServer>) new ArrayList<ThreadUserForServer>();
		try {
			server = new ServerSocket(port);
			//server.bind(sAddr);
		} catch (IOException e) {
			System.out.println("Port already used.");
			System.exit(0);
		}
		System.out.println(" Serveur created ...");
		
		Thread lanceurGame=new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(7000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(canRunGame("animals")) {
						//System.out.println("#############################################################");
						 runGame("animals","débutant");
					}
				}
			}
			
		});
		lanceurGame.start();
	}
	
	/**
	 * 
	 */
	public void run() {
		int i=1;
		System.out.println(" Serveur listening ...");
		
		try {
			while(i<16) {
				
				Socket socket =  server.accept();
				System.out.println(" Serveur accepted on connexion ...");
				ThreadUserForServer userSocket=new ThreadUserForServer(socket,this);
				userSocket.start();
				//listesUsersSocket.add(userSocket);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeConnexion();
	}
	
	/**
	 * 
	 */
	private  void closeConnexion() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" le serveur est �teint bye ...");
	}
	
	/**
	 * @param usersocket
	 */
	public  void addUserSocket(ThreadUserForServer usersocket) {
		listesUsersSocket.add(usersocket);
	}
	
	/**
	 * @param user
	 */
	public  void addUser(User user) {
		if(user!=null)
		users.add(user);
	}
	
	/**
	 * @return
	 */
	public  List<User> getUsers(){
		return users;
	}
	
	/**
	 * 
	 * @return
	 */
	public  String getUsersJSON() {
		JSONObject object=new JSONObject();
		JSONArray array= new JSONArray();
		
		for(User user: users) {
			array.put(user.toJsonString());
		}
		
		return array.toString();
	}
	
	public  String getUsersJSONWithout(User uti) {
		JSONObject object=new JSONObject();
		JSONArray array= new JSONArray();
		
		for(User userr: users) {
			System.out.println(userr.toString());
			if(!userr.equals(uti))
				array.put(userr.toJsonObject());
		}
		
		return array.toString();
	}
	
	
	public  String getAllGroupeJson() {
		JSONArray array= new JSONArray();
		
		for(Group group : listesGroupes) {
			array.put(group.toJsonDescription());
		}
		return array.toString();
	}
	/**
	 * 
	 * @return
	 */
	public  List<ThreadUserForServer> getListesUsersSocket(){
		return listesUsersSocket;
	}
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public  boolean ajouterDansGroupe(String label,ThreadUserForServer user) {
		boolean testAdd=false;
		
		if(!userEstDansGroup(user)) {
			
			for(Group group : listesGroupes) {
				if(group.getLabel().equals(label)) {
					if(!group.contains(user)) {
						group.addUser(user);
						user.setGroup(group);
						user.sendMessage("rejoindreGroupe", "tu as �t� bien ajout� dans "+group.getLabel());
						testAdd=true;
					}
					break;
				}
			}
		}
		else {
			user.sendMessage("rejoudreGroupe","tu as déjà un groupe ");
		}
		return testAdd;
	}
	
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public  boolean creerGroupe(String label,String theme, ThreadUserForServer user) {
		boolean testAdd=false;
		Group groupe= new Group(label, this);
		groupe.setTheme(theme); 
		
		if((!(listesGroupes.contains(groupe))) &&(!userEstDansGroup(user))) {
			groupe.addUser(user);
			user.setGroup(groupe);
			listesGroupes.add(groupe);
			testAdd=true;
			JSONObject object =new JSONObject();
	        object.put("label",label);
	        object.put("thème",theme);
	        object.put("info", "groupe créé");
			user.sendMessage("creerGroupe", object.toString());
		}
		else
			if(userEstDansGroup(user)) {
				user.sendMessage("info", "vous ete déjà dans un groupe");
			}
			else {
				user.sendMessage("info", "Veillez changer le nom de cet groupe car l existe d�j�");
			}
		return testAdd;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	
	public  boolean userEstDansGroup(ThreadUserForServer user) {
		boolean test=false;
		for(Group groupe: listesGroupes) {
			if(groupe.contains(user)) {
				test=true;
				break;
			}
		}
		return test;
	}
	
	
	public  void deconnecterUser(ThreadUserForServer element) {
		
		
		System.out.println("deconnection de "+element.getUser().getEmail());
		users.remove(element.getUser());
		
		if(element.getGroupe()!=null) {
			element.getGroupe().removeUser(element);
			element.sendMessage("deconnecter","Vous venez de vous d�connecter  bye");
			if(element.getGroupe().isEmpty()) {
				
				listesGroupes.remove(element.getGroupe());
			}
			
		}
		listesUsersSocket.remove(element);
	}
	
	
	public  boolean quitterGroupe(String label,ThreadUserForServer user) {
		boolean test=false;
		
		for(Group groupe : listesGroupes) {
			if(groupe.getLabel().equals(label)) {
				groupe.removeUser(user);
				if(groupe.isEmpty()) {
					listesGroupes.remove(groupe);
					
				}
				test=true;
				break;
			}
		}
		
		return test;
	}
	
	public  boolean canRunGame(String theme) {
		boolean test=true;
		if(nbGroupe(theme)<2) {
			test=false;
		}
		else
			for(Group groupe: listesGroupes) {
				if(!groupe.allMemberPret()) {
					test=false;
					break;
				}
			}
		return test;
	}
	
	public int getSizeUser() {
		return listesUsersSocket.size();
	}
	public  int nbGroupe(String theme) {
		int i=0;
		
		for(Group groupe: listesGroupes) {
			if(groupe.getTheme().toLowerCase().equals(theme.toLowerCase()))
				i++;
		}
		return i;
	}
	
	public  void runGame(String theme, String niveau) {
		
		if(!canRunGame(theme)) {
			return;
		}
			
		Jeu jeu=new Jeu(theme,niveau);
		for(Group groupe : listesGroupes) {
			
			if(theme.toLowerCase().equals(groupe.getTheme().toLowerCase())) {
				groupe.envoyerAll("information"," sur le point de lancer la partie : "+theme);
				groupe.demarrer();
				jeu.addGroup(groupe);
				
			}
		}
		
		jeu.start();
			
		
	}
	public void sendAll(String type, String message) {
		for(ThreadUserForServer threadUser : listesUsersSocket) {
			threadUser.sendMessage(type,message);
		}
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Serveur serveur=new Serveur(8990);
		serveur.start();
	}
}
