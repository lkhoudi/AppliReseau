package Serveur.parser;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import entities.Question;
import entities.Quizz;
import entities.User;


public class ParserJson {

public static Quizz jsonparser(String filePath,String niveau) {
		
		JSONParser parser = new JSONParser();
		Quizz quizz = null;
		try {
			
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader =  new BufferedReader(fileReader);
			StringBuilder string =new StringBuilder();
			String line;
			 while((line = bufferedReader.readLine()) != null) {
	                string.append(line);
	        } 
			 
			JSONObject jsonObject =new JSONObject(string.toString());
			String theme = (String) jsonObject.get("thème");
			JSONObject data = jsonObject.getJSONObject("quizz");
			
			quizz=new Quizz(theme,niveau);
			JSONArray array= data.getJSONArray(niveau);
			
			for(int i=0; i<array.length();i++) {
				
				Question quest=parserQuestion(array.getJSONObject(i).toString());
				quizz.add(quest);
				//System.out.println(quest.toJson());
			}
		} catch (Exception e) { e.printStackTrace(); }
	return quizz;
	}



	public static Question parserQuestion(String stringQuestion) {
		Question question;
		
		JSONObject object= new JSONObject(stringQuestion);
		
		String reponse=object.getString("réponse");
		String quest=object.getString("question");
		Integer id=object.getInt("id");
		question= new Question(id,reponse,  quest);
		
		JSONArray propositionsArray =object.getJSONArray("propositions");
		for(int k=0; k<propositionsArray.length();k++) {
			question.addProposition(propositionsArray.getString(k));
		}
		String anecdote = object.getString("anecdote");
		question.setAnecdode(anecdote);
		return question;
	}
	
	public static User parserUser(String userString) {
		User user;
		
		JSONObject object =new JSONObject(userString);
		String email=object.getString("email");
		String firstname=object.getString("firstname");
		String lastname=object.getString("lastname");
		String avatar=object.getString("avatar");
		user=new  User( email,  firstname,  lastname,  avatar);
		return user;
	}
	
	public static List<User> parserListUser(String usersString){
		List<User> users= new ArrayList();

		JSONArray jsonArray =new JSONArray(usersString);

		for(int i=0;i<jsonArray.length();i++) {
			System.out.println("json array " + jsonArray);
			
			System.out.println(jsonArray.getJSONObject(i).toString());
			//users.add(parserUser(jsonArray.getJSONObject(i).toString()));
		}
		return users;
	}
	
	
	public static void main(String[] args) {
		String chemin="./src/main/resources/";
		String[] fichiers= {"animals","bakery","culturegenerale","mediterranee","music","question","worldCountries"};
		for(String str : fichiers) {

			Quizz quizz=ParserJson.jsonparser(chemin+fichiers[0]+".json","débutant");
		}
	}
}
