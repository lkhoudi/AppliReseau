package entities;

import java.util.ArrayList;
import java.util.List;

public class Quizz {
			
/*
	private String theme;
	private int id;
	private String question;
	private Iterator<Object> propositions;
	private String anecdote;
	private String reponse;
	*/

	
	private List<Question> questions;
	private int idQuestion;
	private String theme;
	private String niveau;
	public Quizz(List<Question> questions) {
		this.questions=questions;
	}
	
	public Quizz(String theme,String niveau) {
		this.niveau=niveau;
		this.theme=theme;
		questions= new ArrayList<Question>();
	}
	public Question getQuestion() {
		
		return questions.get(idQuestion);
	}
	
	public void next() {
		idQuestion=!finish()?idQuestion+1:idQuestion;
	}
	
	public boolean finish() {
	
		return idQuestion+1 >=questions.size();
	}
	
	public void recommence() {
		idQuestion=0;
	}
	
	public void add(Question question) {
		questions.add(question);
	}
	
	public void remove(Question question) {
		questions.remove(question);
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	/*
	
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

	*/
	
	
}
	
