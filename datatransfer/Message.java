package datatransfer;

import java.io.Serializable;

/**
 * @author Tomas Lozano from code of Stanley Pineda
 * Course: CST8277_300
 * Date: 2018-10-12
 * 
 * Description: Message takes the command from client and the tuna information from the server, so they can communicate.
 */
public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String command;
	private Tuna tuna;
	
	
	/**
	 * Takes a command from the client and sends it to the next constructor.
	 * 
	 * @param command from client
	 */
	public Message (String command) {
		this(command, null);
	}
	
	/**
	 * Takes a command and a Tuna from the client
	 * 
	 * @param command what the client wants to do
	 * @param tuna, the information to be input into the database
	 */
	public Message (String command, Tuna tuna) {
		this.command = command;
		this.tuna = tuna;
	}
	
	/**
	 * Gets the command
	 * 
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Sets the command
	 * 
	 * @param command to be set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	/**
	 * Gets the Tuna value
	 * 
	 * @return the tuna value
	 */
	public Tuna getTuna() {
		return tuna;
	}
	
	/**
	 * Sets the tuna value
	 * 
	 * @param tuna value
	 */
	public void setTuna(Tuna tuna) {
		this.tuna = tuna;
	}
}
	
