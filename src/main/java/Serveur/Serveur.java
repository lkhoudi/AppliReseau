package Serveur;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.net.Socket;

public class Serveur extends Thread{
	List<Group> listesGroupes;
	List<ThreadUserForServer> listesUsersSocket;
	
	ServerSocket server ;
	int port ;
	// Defining the host and the port of the server
	
	public Serveur(int port)  {
		//InetSocketAddress sAddr = new InetSocketAddress(host, port);
		listesUsersSocket= new ArrayList<ThreadUserForServer>();
		
		try {
			server = new ServerSocket(port);
			//server.bind(sAddr);
		} catch (BindException e) {
			System.out.println("Port already used.");
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" Serveur created ...");
	}
	public void run() {
		int i=1;
		System.out.println(" Serveur listening ...");
		try {
			while(i<3) {
				
				Socket socket =  server.accept();
				System.out.println(" Serveur accepted on connexion ...");
				ThreadUserForServer userSocket=new ThreadUserForServer(socket);
				userSocket.start();
				listesUsersSocket.add(userSocket);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(ThreadUserForServer usersocket) {
		listesUsersSocket.add(usersocket);
	}
	
	public static void main(String[] args) {
		Serveur serveur=new Serveur(8989);
		serveur.start();
	}
}
