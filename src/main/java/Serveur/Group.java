package Serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
	private List<User> users;
	private Map<String,ThreadUserForServer> userSockets;
	private String label;
	
	/**
	 * 
	 */
	public Group(String label) {
		this.label=label;
		users=new ArrayList<User>();
		userSockets= new HashMap<String, ThreadUserForServer>();
	}
	/**
	 * 
	 * @param users
	 */
	public Group(List<User> users) {
		this.users=users;
	}
	/**
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		users.add(user);
	}
	/**
	 * 
	 * @param user
	 */
	public void removeUser(User user) {
		users.remove(user);
	}
	/**
	 * 
	 * @param name
	 * @param userSoket
	 */
	public void addUserSocket(String name, ThreadUserForServer userSoket) {
		this.userSockets.put(name, (ThreadUserForServer) userSockets);
	}
	/**
	 * 
	 * @param message
	 */
	public void envoyerAll(String message) {
		for(Map.Entry<String, ThreadUserForServer> elm: userSockets.entrySet()) {
			elm.getValue().envoyer(message);
		}
	}
	
	/**
	 * 
	 * @param number
	 * @param message
	 */
	public void envoyerSauf(String number, String message) {
		for(Map.Entry<String, ThreadUserForServer> elm: userSockets.entrySet()) {
			if(!elm.getKey().equals(number))
				elm.getValue().envoyer(message);
		}
	}
	
	public String getLabel() {
		return label;
	}
	public String responseGroup() {
		return null;
	}
}
