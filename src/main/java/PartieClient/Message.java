package PartieClient;

import org.json.JSONObject;

public class Message {
    private  String email;
    private String msg;

    public Message(String email, String msg){
        this.email=email;
        this.msg=msg;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEmail() {
        return email;
    }

    public String getMsg() {
        return msg;
    }

    public String toJson()  {
        JSONObject object =new JSONObject();
        object.put("email",email);
        object.put("message",msg);
        return object.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}
    
    
}
