package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadUserForServer extends Thread{
	private Socket socketUser;
	private BufferedReader reader;
	private PrintWriter writer;
	private User user;
	public ThreadUserForServer(Socket clientServerSocket) {
		socketUser=clientServerSocket;
		
		try{
			reader=new BufferedReader(new InputStreamReader(socketUser.getInputStream()));
			writer= new PrintWriter(socketUser.getOutputStream(),true);
			this.user= new User(reader.readLine(),"","");
			writer.println("bonjour vous venez de vous connecter sur le port "+socketUser.getLocalPort());
		}catch(IOException e) {}
	}

	public void run() {
		boolean ok=true;
		String message="";
		int i=0;
		while(i<15){
			try{
				
				writer.println("Bonjour "+user.getNumber()+" message du serveur numéro   "+i++);
				Thread.sleep(1000);
				message=reader.readLine();
				System.out.println(message);
				if(message.equals("ok"))
					ok=false;
				
			}catch(IOException e){}
			catch(Exception e){}
			writer.println("fin");
		}
		
		try{
			socketUser.close();
		}
		catch(IOException e){}
		
	}
	
	public String getNumberUser() {
		return user.getNumber();
	}
	public void envoyer(String message) {
		writer.println(message);
	}
	
	
}
