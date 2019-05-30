package game;


import entities.EtatGame;
import entities.Group;
import entities.Question;
import entities.Quizz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import Serveur.parser.ParserJson;


public class Jeu extends Thread{
	
	Map<Group,Map<Integer,String>> groups;
	private String themeGame;
	private String niveau;
	Quizz questions;
	private EtatGame etat= new EtatGame();
	
	/**
	 * Constructor of the Game
	 * @param theme
	 * @param niveau
	 */
	public Jeu(String theme,String niveau) {
		themeGame=theme;
		this.niveau=niveau;
		initialize();
		initializeData();
	}
	/**
	 * Constructor of the Game
	 * @param group
	 * @param theme
	 * @param niveau
	 */
	public Jeu(List<Group> group,String theme,String niveau) {
		this.themeGame=theme;
		this.niveau=niveau;
		initialize();
		addAllGroup(group);
		initializeData();
	}
	/**
	 * That method add one team in the game
	 * @param group
	 */
	public void addGroup(Group group) {
		groups.put(group,new HashMap<Integer,String>());
	}
	/**
	 * This method add a list of team
	 * @param groupes
	 */
	public void addAllGroup(List<Group> groupes) {
		for(Group group :groupes) {
			if(!groups.containsKey(group))
				addGroup(group);
		}
		
	}
	
	/**
	 * This method add a Quizz the game (all requests)
	 * @param questions
	 */
	public void addQuizz(Quizz questions) {
		this.questions=questions;
	}
	
	/**
	 * To add one question in the game
	 * @param question
	 */
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	
	/**
	 * To initialize the groups
	 */
	private void initialize() {
		
		groups=new HashMap<Group,Map<Integer,String>>();
	}
	
	public void sendAll(String type, String message) {
		JSONObject object= new JSONObject();
		
		object.put("type",type);
		object.put("data",message);
		envoyerSMS(object.toString());
	}
	
	private void envoyerSMS(String message) {
		for(Entry<Group,Map<Integer,String>> group: groups.entrySet()) 
			group.getKey().envoyerAll(message);
	}
	
	/**
	 * This method test if all team have given there answer of a request
	 * @param idQuestion
	 * @return
	 */
	public boolean allResponseGetted(int idQuestion) {
		int i=0;
		
		for(Entry<Group,Map<Integer,String>> group: groups.entrySet()) {
			group.getKey().envoyerAll("vous avez r�pondu");
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

	/**
	 * This method send a question for all team
	 */
	public void sendQuestion() {
		Question question=questions.getQuestion();
		
		for(Group group: groups.keySet()) {
			group.sendQuestion(question);
		}
		
	}
	/**
	 * This method controls the game
	 */
	@Override
	public void run() {
		sendAll("welcome", "sms");
		if(allGroupPret())
			startAllGroup();
		sendQuestion();
		while(!etat.estFini()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(allResponseGetted(questions.getQuestion().getId())) {
				//envoyerResultat si pas fini
				// envoyer Resultat final si fini et changer etat
				//envoyer Next Question 
				if(!questions.finish()) {
					questions.next();
					sendQuestion();
				}
				else {
					etat.setEtat("fini");
				}
				
			}
		}
		// Envoyer resultat final
	}
	
	/**
	 * This method modifies the state of all team members 
	 */
	public void startAllGroup() {
		for(Group group: groups.keySet()) {
			group.demarrer();
			etat.setEtat("encours");
		}
	}
	
	/**
	 * Testing if all team members are prepared for the start
	 * @return
	 */
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
	
	/**
	 * This method parses the data into an object
	 */
	private void initializeData() {
		String chemin="./src/main/resources/";
		questions=ParserJson.jsonparser(chemin+themeGame+".json",niveau);
	}
}
