// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /**
   * The client's alias
   */
  String alias;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   * @param alias The Alias to use for the client
   */
  // Changed for E50) EP
  // Add alias support to constructor
  public ChatClient(String host, int port, ChatIF clientUI, String alias) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.alias = alias;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * E50) F.A
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) {
	//Handles Commands From Client
	if (message.toString().startsWith("#")) {
		if (message.toString().contains("alias")) {
			System.out.println("\n" +"Accepting command #alias");
			int aliasStartIndex = message.indexOf("<");
			int aliasEndIndex = message.indexOf(">");
			String my_new_alias = (String)message.toString().subSequence((aliasStartIndex + 1), (aliasEndIndex));
			this.alias = my_new_alias;
			System.out.println("You updated alias is: " + this.alias);
		}
	//stores command from client
	String command = message.toString();
	//like in android studio for handling multiple cases, ie SimpleCalculator
	//Each case will describe each command
//	switch(command) {
//	case ("#alias"):
//		;
//	    	break;
//	    	}
	}
	else try
  {
    sendToServer(message);
  }
  catch(IOException e)
  {
    clientUI.display
      ("Could not send message to server.  Terminating client.");
    quit();
  }
  }
  
  /**
   * Modified for E49 a) F.A
   */
  @Override
  public void connectionClosed() {
      //System.out.println("Connection Closed to Server.");
      clientUI.display("Connection Closed to Server.");
  }

  /**
   * Modified for E49 a) F.A
   */
  @Override
  public void connectionException(Exception exception) {
      //System.out.println("Sever has stoped listening for connections, disconnecting");
      clientUI.display("Client has stoped listening for connections, disconnecting");
      System.exit(0);
  }

  /**
   * Modified for E50) EP
   */  
  @Override
  public void connectionEstablished() {
	  // Super hacky and probably a better way to do this
	  // If an alias has been set, automatically run a #alias command
	  if (!alias.isBlank())
		try {
			sendToServer(String.format("#alias %s", alias));
		} catch (IOException e) {
			clientUI.display("Could not set alias");
			quit();
		}
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
