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
						 runGame("animals","d�butant");
					}
				}
			}
			
		});
		lanceurGame.start();
	}
	
	/**
	 * 
	 */
	public  void run() {
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
	private synchronized void closeConnexion() {
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
	public synchronized void addUserSocket(ThreadUserForServer usersocket) {
		listesUsersSocket.add(usersocket);
	}
	
	/**
	 * @param user
	 */
	public synchronized void addUser(User user) {
		users.add(user);
	}
	
	/**
	 * @return
	 */
	public synchronized List<User> getUsers(){
		return users;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized String getUsersJSON() {
		JSONObject object=new JSONObject();
		JSONArray array= new JSONArray();
		
		for(User user: users) {
			array.put(user.toJson());
		}
		
		return array.toString();
	}
	
	public synchronized String getUsersJSONWithout(User uti) {
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
	public synchronized List<ThreadUserForServer> getListesUsersSocket(){
		return listesUsersSocket;
	}
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public synchronized boolean ajouterDansGroupe(String label,ThreadUserForServer user) {
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
			user.sendMessage("rejoudreGroupe","tu as d�j� un groupe ");
		}
		return testAdd;
	}
	
	
	/**
	 * 
	 * @param label
	 * @param user
	 * @return
	 */
	public synchronized boolean creerGroupe(String label,String theme, ThreadUserForServer user) {
		boolean testAdd=false;
		Group groupe= new Group(label, this);
		groupe.setTheme(theme);
		System.out.println(" Vous etes sur le point de cr�er un groupe");
		
		if((!(listesGroupes.contains(groupe))) &&(!userEstDansGroup(user))) {
			groupe.addUser(user);
			user.setGroup(groupe);
			listesGroupes.add(groupe);
			testAdd=true;
			user.sendMessage("creerGroupe", "Le groupe "+label+" avec le theme :"+theme+" a �t� bien cr��");
		}
		else
			if(userEstDansGroup(user)) {
				user.sendMessage("creerGroupe", "vous ete d�j� dans un groupe");
			}
			else {
				user.sendMessage("creerGroupe", "Veillez changer le nom de cet groupe car l existe d�j�");
			}
		return testAdd;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	
	public synchronized boolean userEstDansGroup(ThreadUserForServer user) {
		boolean test=false;
		for(Group groupe: listesGroupes) {
			if(groupe.contains(user)) {
				test=true;
				break;
			}
		}
		return test;
	}
	
	
	public synchronized void deconnecterUser(ThreadUserForServer element) {
		
		
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
	
	
	public synchronized boolean quitterGroupe(String label,ThreadUserForServer user) {
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
	
	public synchronized boolean canRunGame(String theme) {
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
	
	public synchronized int nbGroupe(String theme) {
		int i=0;
		
		for(Group groupe: listesGroupes) {
			if(groupe.getTheme().toLowerCase().equals(theme.toLowerCase()))
				i++;
		}
		return i;
	}
	
	public synchronized void runGame(String theme, String niveau) {
		
		if(!canRunGame(theme)) {
			System.out.println("can not run the game");
			return;
		}
			
		Jeu jeu=new Jeu(theme,niveau);
		for(Group groupe : listesGroupes) {
			
			if(theme.toLowerCase().equals(groupe.getTheme().toLowerCase())) {
				groupe.envoyerAll(" sur le point de lancer la partie le th�me du ");
				groupe.demarrer();
				jeu.addGroup(groupe);
				
			}
		}
		
		jeu.start();
			
		
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
