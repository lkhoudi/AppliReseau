package Serveur;

import java.util.ArrayList;
import java.util.List;

public class Quizz {
	
	private List<Question> questions;
	private int idQuestion;
	
	public Quizz(List<Question> questions) {
		this.questions=questions;
	}
	
	public Quizz() {
		questions= new ArrayList<Question>();
	}
	public Question getQuestion() {
		
		return questions.get(idQuestion);
	}
	
	public void next() {
		idQuestion=!finish()?idQuestion+1:idQuestion;
	}
	
	public boolean finish() {
	
		return idQuestion >=questions.size();
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
}
