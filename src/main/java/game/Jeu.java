package game;


import entities.EtatGame;
import entities.Group;
import entities.Question;
import entities.Quizz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Jeu implements Runnable{
	
	Map<Group,Map<Integer,String>> groups;
	Quizz questions;
	private EtatGame etat= new EtatGame();
	
	public Jeu() {
		initialize();
	}
	public Jeu(List<Group> group) {
		initialize();
		addAllGroup(group);
	}
	
	public void addGroup(Group group) {
		groups.put(group,new HashMap<Integer,String>());
	}
	
	public void addAllGroup(List<Group> groupes) {
		for(Group group :groupes) {
			if(!groups.containsKey(group))
				addGroup(group);
		}
		
	}
	
	public void addQuizz(Quizz questions) {
		this.questions=questions;
	}
	public void addQuestion(Question question) {
		questions.add(question);
	}
	private void initialize() {
		groups=new HashMap<Group,Map<Integer,String>>();
	}
	
	
	public boolean allResponseGetted(int idQuestion) {
		int i=0;
		
		for(Entry<Group,Map<Integer,String>> group: groups.entrySet()) {
			if(group.getValue().containsKey(idQuestion)) {
				i++;
			}
			else {
				if(group.getKey().allResponseGetted(idQuestion)) {
					String reponse=group.getKey().responseGroup(idQuestion);
					if(reponse!=null) {
						group.getValue().put(idQuestion, reponse);
					}
					i++;
				}
			}
		}
		
		return i==groups.size();
	}

	public void sendQuestion() {
		Question question=questions.getQuestion();
		
		for(Group group: groups.keySet()) {
			group.sendQuestion(question);
		}
		
	}
	@Override
	public void run() {
		if(allGroupPret())
			startAllGroup();
		sendQuestion();
		while(!etat.estFini()) {
			if(allResponseGetted(questions.getQuestion().getId())) {
				//envoyerResultat si pas fini
				// envoyer Resultat final si fini et changer etat
				//envoyer Next Question 
			}
		}
	}
	
	public void startAllGroup() {
		for(Group group: groups.keySet()) {
			group.demarrer();
			etat.setEtat("encours");
		}
	}
	
	public boolean allGroupPret() {
		boolean test=true;
		for(Group group: groups.keySet()) {
			if(!group.allMemberPret()) {
				test=false;
				break;
			}
		}
		return test;
	}
}
