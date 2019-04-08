package server;
/* File: TunaServer.java
 * Author: Stanley Pieda, based on course materials by Todd Kelley
 * Modified Date: Aug 2018
 * Description: Simple echo client.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import datatransfer.Tuna;
import datatransfer.Message;

import dataaccesslayer.TunaDaoImpl;

/**
 * @author Tomas Lozano from code of Stanley Pineda
 * Course: CST8277_300
 * Date: 2018-10-12
 * 
 * Description: TunaServer connects with a client and a database to insert data in the database according to client's requests.
 */
public class TunaServer {

	private int portNum = 8081;
	private boolean Flag = false;
	private String command;
	public static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private TunaDaoImpl tunaDB = new TunaDaoImpl();

			
	/**
	 * Main Method, starts the server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TunaServer server = new TunaServer();
		server.conectServer();
	}
	
	/**
	 * Method that creates the server connection and connection with the database. Takes the information from the client and make sure is inserted successfully.
	 * 
	 */
	public void conectServer() {
		
		try(ServerSocket socket = new ServerSocket(portNum)){
			tunaDB.deleteTunas();
			
			while(true) {
				Socket conn = socket.accept();
				System.out.println("Connection Established");
				threadExecutor.execute( new Runnable () {

					@Override
					public void run() {
						try(ObjectOutputStream output = new ObjectOutputStream(conn.getOutputStream());
							ObjectInputStream input = new ObjectInputStream(conn.getInputStream())){
							
							while(!Flag) {
								Message message = (Message) input.readObject();
								command = message.getCommand();
								Tuna tuna = message.getTuna();
								
								if(command.equals("insert")) {
									System.out.println("Inserting data in database");
									
									if(tunaDB.insertTuna(tuna)) {
										Tuna tuna2 = tunaDB.getTunaByUUID(tuna.getUUID());
										
										if(tuna2 != null) {
											message.setCommand("insert_success");
											tuna.setId(tuna2.getId());
											message.setTuna(tuna);
										}
										
									}
									
									output.writeObject(message);
								} else if (command.equals("disconnect")) {
									output.writeObject(message);
									
									System.out.println("Disconnected");
									Flag =  true;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						try {
							if (conn != null) {
								conn.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
