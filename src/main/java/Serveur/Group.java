package Serveur;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Group {
	private Map<ThreadUserForServer,Map<Integer,String>> users;
	private Map<Integer, String> responseGroupe;
	private Serveur serveur;
	private String label;
	private EtatGame etat= new EtatGame();
	private float point;
	private Question question;
	/**
	 * 
	 */
	public Group(String label, Serveur serveur) {
		
		initailise(label,serveur);
		
	}
	/**
	 * 
	 * @param label
	 * @param serveur
	 * @param user
	 */
	public Group(String label, Serveur serveur,ThreadUserForServer user ) {
		
		initailise(label,serveur);
		users.put(user, new HashMap<Integer,String>());
	}
	
	/**
	 * 
	 * @param label
	 * @param serveur
	 */
	private void initailise(String label, Serveur serveur) {
		this.label=label;
		users=new HashMap<ThreadUserForServer,Map<Integer,String>>();
		this.serveur=serveur;
	}
	/**
	 * 
	 * @param users
	 */
	public Group(List<ThreadUserForServer> users) {
		
		for(ThreadUserForServer user:users) {
			this.users.put(user, new HashMap<Integer,String>());
		}
	}
	/**
	 * 
	 * @param user
	 */
	public void addUser(ThreadUserForServer user) {
		users.put(user,new HashMap<Integer,String>());
	}
	/**
	 * 
	 * @param user
	 */
	public void removeUser(ThreadUserForServer user) {
		users.remove(user);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void envoyerAll(String message) {
		for( Entry<ThreadUserForServer, Map<Integer, String>> elm: users.entrySet()) {
			elm.getKey().envoyer(message);
		}
	}
	
	/**
	 * 
	 * @param number
	 * @param message
	 */
	public void envoyerSauf(String number, String message) {
		for(Entry<ThreadUserForServer,Map<Integer,String>> elm: users.entrySet()) {
			if(!elm.getKey().getUser().getNumber().equals(number))
				elm.getKey().envoyer(message);
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public String responseGroup(int idQuestion) {
		String response=null;
		if(allResponseGetted(idQuestion)) {
			Map<String,Integer> resp=new HashMap<>();
			
			for(Map.Entry<ThreadUserForServer, Map<Integer,String>> elem:users.entrySet()) {
				for(Map.Entry<Integer,String> rep :elem.getValue().entrySet()) {
					if(rep.getKey()==idQuestion&& (rep.getValue()!=null)) {
						if(resp.containsKey(rep.getValue())) {
							resp.put(rep.getValue(), resp.get(rep.getValue()+1));
						}
						else {
							resp.put(rep.getValue(), 1);
						}
					}
				}
			}
			int i=0;
			for(Map.Entry<String, Integer> elem: resp.entrySet()) {
				if(i==0) {
					response=elem.getKey();
					i=1;
				}
				else {
					if(resp.get(response)<elem.getValue()) {
						response=elem.getKey();
					}
				}
			}
		}
		return response;
	}
	
	public boolean contains(ThreadUserForServer user) {
		return users.containsKey(user);
	}
	
	/**
	 * 
	 * @param user
	 * @param response
	 */
	public void setResponse(ThreadUserForServer user, String response) {
		if(contains(user)) {
			JSONObject object=new JSONObject(response);
			int idQuestion=object.getInt("id");
			String reponse=object.getString("response");
			Map<Integer,String > lastValue=users.get(user);
			lastValue.put(idQuestion,reponse);
			users.replace(user, lastValue);
		}
	}
	
	
	/**
	 * 
	 * @param quest
	 */
	public void sendQuestion(Question quest) {
		if(etat.estEnCours()) {
			this.question=quest;
			JSONObject object= new JSONObject();
			object.put("type", "question");
			JSONObject object2=new JSONObject();
			object2.put("id", quest.getId());
			object2.put("question", quest.getQuestion());
			JSONArray array= new JSONArray();
			List<String> propositions=quest.getPropositions();
			for(String str: propositions) {
				JSONObject ob =new JSONObject();
				ob.put("proposition", str);
				array.put(ob.toString());
			}
			object2.put("propositions", array);
			object.put("data", object2.toString());
			envoyerAll(object.toString());
		}
	}
	/**
	 * 
	 * @param idQuestion
	 * @return
	 */
	public boolean allResponseGetted(int idQuestion) {
		int i=0;
		for(Entry<ThreadUserForServer,Map<Integer,String>> user: users.entrySet()) {
			if(user.getValue().containsKey(idQuestion)) {
				i++;
			}
		}
		return i==users.size();
	}
	
	
	public int getSize() {
		
		return users.size();
	}
	public boolean isEmpty() {
		return getSize()==0;
	}
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Group)) {
			return false;
		}
		Group group=(Group)  object;
		return label.equals(group.getLabel());
	}
	
	public boolean allMemberPret() {
		for(ThreadUserForServer user :users.keySet()) {
			if(!user.estPret())
				return false;
		}
		return true;
	}
	
	public boolean demarrer() {
		boolean test=false;
		if(allMemberPret()) {
			for(ThreadUserForServer user : users.keySet()) {
				user.setEtatOfUser("encours");
			}
			etat.setEtat("encours");
			test=true;
		}
		return test;
	}
}
