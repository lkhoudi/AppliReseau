package Serveur.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Serveur.Question;
import Serveur.Quizz;

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
			String theme = (String) jsonObject.get("theme");
			JSONObject data = jsonObject.getJSONObject("quizz");
			
			quizz=new Quizz(theme,niveau);
			JSONArray array= data.getJSONArray(niveau);
			
			for(int i=0; i<array.length();i++) {
				JSONObject element=array.getJSONObject(i);
				int id=element.getInt("id");
				String reponse=element.getString("reponse");
				String question=element.getString("question");
				Question quest= new Question(id,reponse,  question);
				JSONArray propositionsArray =element.getJSONArray("propositions");
				Iterator<Object> propositions = propositionsArray.iterator();
				String anecdote = element.getString("anecdote");
				quest.setAnecdode(anecdote);
				quizz.add(quest);
				System.out.println(quest.toJson());
			}
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		catch (Exception e) {e.printStackTrace();}
		return quizz;
	}


	public static void main(String[] args) {
		
		Quizz quizz=ParserJson.jsonparser("./src/main/resources/cultureGenerale.json","debutant");
	}
}
