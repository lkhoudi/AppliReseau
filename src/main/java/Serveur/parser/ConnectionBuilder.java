package Serveur.parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class ConnectionBuilder {
	private static Connection connector;
	private static String dbName;
	private static String userName;
	private static String userPassword;
	
	public static void setInfo(String db, String name, String password) {
		dbName=db;
		userName=name;
		userPassword=password;
	}
	public static Connection getConnector() throws SQLException {
		if(connector==null) {
			connector=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,userName,userPassword); 
		}
		
		return connector;
	}
	
	public static Statement getStatement() throws SQLException {
		
		return getConnector().createStatement();   
	}
	
	public static ResultSet getResultSet(String requete) throws SQLException {
		
		return getStatement().executeQuery(requete);
	}
	
	public static void main(String[] agrs) throws SQLException {
		
		ConnectionBuilder.setInfo("centre", "root", "");
		ResultSet result= ConnectionBuilder.getResultSet("select * from news");
		
		while(result.next()) {
			System.out.println(result.getString(1));
		}
		
	}
	 
	

}
