package Serveur;

import java.util.List;

public class Question {

	private int id;
	private String reponse;
	private String question;
	private List<String> propositions;
	
	
	public Question(int id, String reponse, String question, List<String> propositions) {
		this.id=id;
		this.reponse=reponse;
		this.question=question;
		this.propositions=propositions;
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


	public void setPropositions(List<String> propositions) {
		this.propositions = propositions;
	}
	
	
}
