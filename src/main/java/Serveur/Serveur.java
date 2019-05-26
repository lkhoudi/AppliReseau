package Serveur;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import entities.Group;
import entities.User;
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
		listesUsersSocket= new ArrayList<ThreadUserForServer>();
		System.out.println(" lancement du serveur");
		users=new ArrayList<User>();
		listesGroupes= new ArrayList<Group>();
		listesUsersSocket= new ArrayList<ThreadUserForServer>();
		try {
			server = new ServerSocket(port);
			//server.bind(sAddr);
		} catch (IOException e) {
			System.out.println("Port already used.");
			System.exit(0);
		}
		System.out.println(" Serveur created ...");
	}
	
	/**
	 * 
	 */
	public void run() {
		int i=1;
		System.out.println(" Serveur listening ...");
		try {
			while(i<6) {
				
				Socket socket =  server.accept();
				System.out.println(" Serveur accepted on connexion ...");
				ThreadUserForServer userSocket=new ThreadUserForServer(socket,this);
				userSocket.start();
				listesUsersSocket.add(userSocket);
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
	private void closeConnexion() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" le serveur est éteint bye ...");
	}
	
	/**
	 * @param usersocket
	 */
	public void addUserSocket(ThreadUserForServer usersocket) {
		listesUsersSocket.add(usersocket);
	}
	
	/**
	 * @param user
	 */
	public void addUser(User user) {
		users.add(user);
	}
	
	/**
	 * @return
	 */
	public List<User> getUsers(){
		return users;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsersJSON() {
		JSONObject object=new JSONObject();
		JSONArray array= new JSONArray();
		
		for(User user: users) {
			array.put(user.toJson());
		}
		
		return array.toString();
	}
	
	public String getUsersJSONWithout(User uti) {
		JSONObject object=new JSONObject();
		JSONArray array= new JSONArray();
		
		for(User user: users) {
			if(!user.equals(uti))
				array.put(user.toJson());
		}
		
		return array.toString();
	}
	/**
	 * 
	 * @return
	 */
	public List<ThreadUserForServer> getListesUsersSocket(){
		return listesUsersSocket;
	}
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public boolean ajouterDansGroupe(String label,ThreadUserForServer user) {
		boolean testAdd=false;
		
		if(!userEstDansGroup(user)) {
			for(Group group : listesGroupes) {
				if(group.getLabel().equals(label)) {
					if(!group.contains(user)) {
						group.addUser(user);
						user.setGroup(group);
						testAdd=true;
					}
					break;
				}
			}
		}
		return testAdd;
	}
	
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public boolean creerGroupe(String label, ThreadUserForServer user) {
		boolean testAdd=false;
		Group groupe= new Group(label, this);
		System.out.println(" Vous etes sur le point de créer un groupe");
		
		if((!(listesGroupes.contains(groupe))) &&(!userEstDansGroup(user))) {
			groupe.addUser(user);
			user.setGroup(groupe);
			listesGroupes.add(groupe);
			testAdd=true;
			user.sendMessage("creerGroupe", "Le groupe "+label+" a été bien créé");
		}
		else
			if(userEstDansGroup(user)) {
				user.sendMessage("creerGroupe", "vous ete dèjà dans un groupe");
			}
			else {
				user.sendMessage("creerGroupe", "Veillez changer le nom de cet groupe car l existe dèjà");
			}
		return testAdd;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	
	public boolean userEstDansGroup(ThreadUserForServer user) {
		boolean test=false;
		for(Group groupe: listesGroupes) {
			if(groupe.contains(user)) {
				test=true;
				break;
			}
		}
		return test;
	}
	
	
	public void deconnecterUser(ThreadUserForServer element) {
		
		
		System.out.println("deconnection de "+element.getUser().getEmail());
		users.remove(element.getUser());
		
		if(element.getGroupe()!=null) {
			element.getGroupe().removeUser(element);
			element.sendMessage("deconnecter","Vous venez de vous déconnecter  bye");
			if(element.getGroupe().isEmpty()) {
				listesGroupes.remove(element.getGroupe());
				
			}
			
		}
		listesUsersSocket.remove(element);
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
