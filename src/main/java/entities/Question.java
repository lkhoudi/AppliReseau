package entities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Question {

	private int id;
	private String reponse;
	private String question;
	private List<String> propositions;
	private String anecdote;
	
	public Question(int id, String reponse, String question, List<String> propositions) {
		setter(id, reponse,question);
		this.propositions=propositions;
	}

	public Question(int id, String reponse, String question) {
		setter(id, reponse,question);
		propositions=new ArrayList<String>();
	}
	
	private void setter(int id, String reponse, String question) {
		this.id=id;
		this.reponse=reponse;
		this.question=question;
	}
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getReponse() {
		return reponse;
	}


	public void setReponse(String reponse) {
		this.reponse = reponse;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public List<String> getPropositions() {
		return propositions;
	}

	public void addProposition(String prop) {
		propositions.add(prop);
	}
	public void setPropositions(List<String> propositions) {
		this.propositions = propositions;
	}

	public String getAnecdote() {
		return anecdote;
	}

	public void setAnecdode(String anectode) {
		this.anecdote = anectode;
	}
	
	
	
	public String toJSon() {
		JSONObject object2=new JSONObject();
		
		
		object2.put("question", getQuestion());
		JSONArray array= new JSONArray();
		List<String> propositions=getPropositions();
		
		for(String str: propositions) {
			//JSONObject ob =new JSONObject();
			//ob.put("proposition", str);
			array.put(str);
		}
		object2.put("réponse",getReponse());
		object2.put("anecdote",getAnecdote());
		object2.put("propositions", array);
		object2.put("id",getId());
		return object2.toString();
		
	}
}
