package entities;

import org.json.JSONObject;

public class User {
	
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private boolean active;
	private String avatar="inconnu";
	
	public User(String email, String firstname, String lastname, String password, boolean active, String avatar) {
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.active = active;
		this.avatar=avatar;
	}

	public User(String email, String firstname, String lastname, String password) {
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
	}
	
	public User(String email, String firstname, String lastname) {
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.avatar="";
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean equals(Object anObject) {
		if( !(anObject instanceof User)) {
			return false;
		}
		User user=(User) anObject;
		
		return user.email.equals(email);
	}
	
	
	public String toJsonString() {
		JSONObject userJson= new JSONObject();
		
		userJson.put("firstname", firstname);
		userJson.put("lastname", lastname);
		userJson.put("email", email);
		userJson.put("avatar", avatar);
		return userJson.toString();
	}
	
	public JSONObject toJsonObject() {
		JSONObject userJson= new JSONObject();
		
		userJson.put("firstname", firstname);
		userJson.put("lastname", lastname);
		userJson.put("email", email);
		userJson.put("avatar", avatar);
		return userJson;
	}

}
