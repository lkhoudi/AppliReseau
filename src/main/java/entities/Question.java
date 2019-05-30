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

	public String getAnecdode() {
		return anecdote;
	}

	public void setAnecdode(String anectode) {
		this.anecdote = anectode;
	}
	
	public String toJson() {
		JSONObject object=new JSONObject();
		
		object.put("question",question);
		object.put("reponse", reponse);
		object.put("anecdote", anecdote);
		JSONArray jsonArray=new JSONArray();
		
	
		for(int i=0; i<propositions.size(); i++)
			jsonArray.put(propositions.get(i));
		//System.out.println(propositions.toString());
		object.put("propositions",jsonArray);
		
		return object.toString();
	}
	
}
