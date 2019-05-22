package Serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Quizz {
			

	private String theme;
	private int id;
	private String question;
	private Iterator<Object> propositions;
	private String anecdote;
	private String reponse;
	
	
	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public Iterator<Object> getPropositions() {
		return propositions;
	}


	public void setPropositions(Iterator<Object> propositions) {
		this.propositions = propositions;
	}


	public String getAnecdote() {
		return anecdote;
	}


	public void setAnecdote(String anecdote) {
		this.anecdote = anecdote;
	}


	public String getReponse() {
		return reponse;
	}


	public void setReponse(String reponse) {
		this.reponse = reponse;
	}


	public Quizz(String theme, int id, String question, Iterator<Object> propositions, String reponse, String anecdote
			) {
		this.theme = theme;
		this.id = id;
		this.question = question;
		this.propositions = propositions;
		this.anecdote = anecdote;
		this.reponse = reponse;
	}


	public Quizz jsonparser(String filePath) {
		
		JSONParser parser = new JSONParser();
		Quizz quizz = null;
		try {
			
			Object obj = parser.parse(new FileReader(filePath));
			JSONObject jsonObject = (JSONObject) obj;
			String theme = (String) jsonObject.get("thème");
			Integer id = (Integer) jsonObject.getJSONObject("quizz").get("id");
			String question = (String) jsonObject.getJSONObject("quizz").get("question");
			JSONArray propositionsArray = (JSONArray)jsonObject.getJSONArray("propositions");
			Iterator<Object> propositions = propositionsArray.iterator();

			String reponse = (String) jsonObject.get("réponse");
			String anecdote = (String) jsonObject.get("anecdote");
			quizz = new Quizz(theme,id, question, propositions, reponse, anecdote );
			return quizz;
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		catch (ParseException e) { e.printStackTrace(); }
		catch (Exception e) {e.printStackTrace();}
		return quizz;
}
	
}
	
