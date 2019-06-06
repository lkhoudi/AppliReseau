package entities;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Serveur.Serveur;
import Serveur.ThreadUserForServer;
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
	private String theme;
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
	private synchronized void initailise(String label, Serveur serveur) {
		this.label=label;
		users=new HashMap<ThreadUserForServer,Map<Integer,String>>();
		this.serveur=serveur;
		System.out.println("Création d'un groupe nom : "+label  );
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
	public synchronized  void addUser(ThreadUserForServer user) {
		System.out.println(" ajout d'un utilisateur : "+user.getName());
		users.put(user,new HashMap<Integer,String>());
	}
	/**
	 * 
	 * @param user
	 */
	public synchronized  void removeUser(ThreadUserForServer user) {
		users.remove(user);
	}
	
	/**
	 * 
	 * @param message
	 */
	public synchronized  void envoyerAll(String type,String message) {
		JSONObject object= new JSONObject();
		object.put("type", type);
		object.put("data", message);
		for( Entry<ThreadUserForServer, Map<Integer, String>> elm: users.entrySet()) {
			elm.getKey().envoyer(object.toString());
		}
	}
	
	/**
	 * 
	 * @param
	 * @param message
	 */
	public synchronized  void envoyerSauf(String email, String message) {
		for(Entry<ThreadUserForServer,Map<Integer,String>> elm: users.entrySet()) {
			if(!elm.getKey().getUser().getEmail().equals(email))
				elm.getKey().envoyer(message);
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public synchronized  String responseGroup(int idQuestion) {
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
	
	public synchronized  boolean contains(ThreadUserForServer user) {
		return users.containsKey(user);
	}
	
	/**
	 * 
	 * @param user
	 * @param response
	 */
	public synchronized  void setResponse(ThreadUserForServer user, String response) {
		if(contains(user)) {
			JSONObject object=new JSONObject(response);
			int idQuestion=object.getInt("id");
			String reponse=object.getString("réponse");
			Map<Integer,String > lastValue=users.get(user);
			lastValue.put(idQuestion,reponse);
			users.replace(user, lastValue);
		}
	}
	
	
	/**
	 * 
	 * @param quest
	 */
	public synchronized  void sendQuestion(Question quest) {
		if(etat.estEnCours()) {
			this.question=quest;
			System.out.println(quest.toJSon());
			envoyerAll("question",quest.toJSon());
		}
	}
	/**
	 * 
	 * @param idQuestion
	 * @return
	 */
	public synchronized  boolean allResponseGetted(int idQuestion) {
		int i=0;
		for(Entry<ThreadUserForServer,Map<Integer,String>> user: users.entrySet()) {
			if(user.getValue().containsKey(idQuestion)) {
				i++;
			}
		}
		return i==users.size();
	}
	
	
	public synchronized  int getSize() {
		
		return users.size();
	}
	
	public synchronized  boolean isEmpty() {
		return getSize()==0;
	}
	@Override
	public synchronized  boolean equals(Object object) {
		if(!(object instanceof Group)) {
			return false;
		}
		Group group=(Group)  object;
		return label.equals(group.getLabel());
	}
	
	public synchronized  boolean allMemberPret() {
		
		//envoyerAll("le serveur est entrain de tester si tous les joueurs de "+label+"  sont prets");
		//envoyerAll("le nombre de user : "+users.size());
		for(ThreadUserForServer user :users.keySet()) {
			//user.sendMessage("testEtat","état de "+user.getName()+" est "+user.getEtatUser().toString());
			if(!user.estPret()) {
				//envoyerAll("teste pour commencer le jeu malheuresement "+user.getName()+ "n'est pas prets");
				return false;
			}
		}
		return true;
	}
	
	public synchronized  boolean demarrer() {
		boolean test=false;
		
		if(allMemberPret()) {
			envoyerAll("information","Actuellement tous les membres de "+label+" sont prets");
			for(ThreadUserForServer user : users.keySet()) {
				user.setEtatOfUser("encours");
				user.sendMessage("etat", "ton etat est encours");
			}
			etat.setEtat("encours");
			envoyerAll("information","le jeu est maintenant en cours");
			test=true;
		}
		return test;
	}
	public synchronized  void setTheme(String them) {
		theme=them;
	}
	
	
	public String listUsers() {
		
		JSONArray array= new JSONArray();
		
		for(ThreadUserForServer user: users.keySet()) {
			array.put(user.getUser().toJsonString());
		}
		
		return array.toString();
	}

	public Serveur getServeur() {
		return serveur;
	}

	public synchronized String getUsersJSONWithout(ThreadUserForServer uti) {
		
		JSONArray array= new JSONArray();
		
		for(ThreadUserForServer user: users.keySet()) {
			if(!user.equals(uti))
				array.put(user.getUser().toJsonObject());
		}
		
		return array.toString();
	}
	
	public synchronized String toJsonDescription() {
		JSONObject object =new JSONObject();
		
		object.put("thème", theme);
		object.put("label", label);
		return object.toString();
	}
	public synchronized String getTheme() {
		return theme;
	}
	
	public Set<ThreadUserForServer> getUsers(){
		return users.keySet();
	}
}
