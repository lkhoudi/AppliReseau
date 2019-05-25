package Serveur;

import org.json.JSONObject;

public class User {
	
	private String number;
	private String name;
	private String avatar;
	
	public User(String number, String name, String avatar) {
		this.number=number;
		this.name=name;
		this.avatar=avatar;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		
		return user.name.equals(name)&&(user.avatar.equals(avatar))&&(user.number.equals(number));
	}
	
	
	public String toJson() {
		JSONObject object =new JSONObject();
		JSONObject userJson= new JSONObject();
		
		userJson.put("name", name);
		userJson.put("number", number);
		userJson.put("avatar", avatar);
		object.put("type", "user");
		object.put("user", userJson);
		return object.toString();
	}

}
