package Serveur;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private List<ThreadUserForServer> users;
	private String label;
	
	/**
	 * 
	 */
	public Group(String label) {
		this.label=label;
		users=new ArrayList<ThreadUserForServer>();
	}
	/**
	 * 
	 * @param users
	 */
	public Group(List<ThreadUserForServer> users) {
		this.users=users;
	}
	/**
	 * 
	 * @param user
	 */
	public void addUser(ThreadUserForServer user) {
		users.add(user);
	}
	/**
	 * 
	 * @param user
	 */
	public void removeUser(ThreadUserForServer user) {
		users.remove(user);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void envoyerAll(String message) {
		for( ThreadUserForServer elm: users) {
			elm.envoyer(message);
		}
	}
	
	/**
	 * 
	 * @param number
	 * @param message
	 */
	public void envoyerSauf(String number, String message) {
		for(ThreadUserForServer elm: users) {
			if(!elm.getUser().getEmail().equals(number))
				elm.envoyer(message);
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public String responseGroup() {
		return null;
	}
}
