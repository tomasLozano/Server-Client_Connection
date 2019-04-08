package client;
/* File: TunaClient.java
 * Author: Stanley Pieda, based on course example by Todd Kelley
 * Modified Date: August 2018
 * Description: Networking client that uses simple protocol to send and receive transfer objects.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;

import datatransfer.Tuna;
import datatransfer.Message;


/**
 * @author Tomas Lozano from code of Stanley Pineda
 * Course: CST8277_300
 * Date: 2018-10-12
 * 
 * Description: TunaClient creates a connection with a server and sends the information to populate the database.
 */

public class TunaClient {
	
	private int portNum = 8081;
	private String serverName = "localhost";
	private boolean Flag = false;
	private String command = "";
	
	
	
	/**
	 * Runs the client program
	 * 
	 * @param args 
	 */
	public static void main(String[] args) {
		//System.out.println("Waiting for connection with client");
		TunaClient connection = new TunaClient();
		connection.ConnectClient();
	}
	
	
	/**
	 * A void method that creates the connection with the server and sends the information to the database
	 * 
	 */
	public void ConnectClient() {
		
		try (Socket socket = new Socket(InetAddress.getByName(serverName), portNum);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream())){
			
			Scanner scan = new Scanner(System.in);
			
			while (!Flag) {
				System.out.println("Choose a command: insert or disconnect");
				command = scan.nextLine();
		
				if (command.equals("insert")) {
					int RecordNum = 0;
					
					System.out.println("Insert new Tuna data");
					System.out.println("");
					
					System.out.println("Tuna Number: ");
					String TunaNum = scan.nextLine();
					RecordNum = Integer.parseInt(TunaNum);
					
					System.out.print("Omega:");
					String omega = scan.nextLine();
					System.out.print("Delta:");
					String delta = scan.nextLine();
					System.out.print("Theta:");
					String theta = scan.nextLine();
					
					UUID uuid = UUID.randomUUID();
					Tuna tuna = new Tuna(null, RecordNum, omega, delta, theta, uuid.toString());
					
					output.writeObject(new Message("insert", tuna));
					Message message = (Message) input.readObject();
					
					if(message.getCommand().equals("insert_success")) {
						System.out.println("Data inserted " + message.getTuna());
					} else {
						System.out.println("Data did not insert");
					}
					
				} else if (command.equals("disconnect")) {
					output.writeObject(new Message("disconnect"));
					Message message = (Message) input.readObject();
					
					if(message.getCommand().equals("disconnect")) {
						System.out.println("Disconecting from Server");
						Flag = true;
					} else {
						System.out.println("Did not disconect from Server");
					}
				}
			}
			
			scan.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
