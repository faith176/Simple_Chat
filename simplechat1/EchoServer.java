// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	if (client.getInfo("alias") != null) System.out.println("Message received: " + msg + " from " + client.getInfo("alias"));
	else System.out.println("Message received: " + msg + " from " + client);
    
    // Modified for E50
    //Handles Commands From Client
    if (msg.toString().startsWith("#")) {
    	//stores command from client
    	String command = msg.toString();
    	//like in android studio for handling multiple cases, ie SimpleCalculator
    	//Each case will describe each command
    	switch(command.split(" ")[0]) {
    	case "#alias":
    		// Set client alias
    		client.setInfo("alias", command.substring(command.indexOf(' ') + 1));
    		System.out.println(String.format("Set %s to alias %s", client, client.getInfo("alias")));
    		break;
    	case "#numbers":
    		getNumberOfClients();
    		break;
    	case "#all_users":
    		//from abstract class, stops listening for new connections
    		getClientConnections();
    		break;
    	case "#kill":
    		break;
    	}
    	
    } else {
    	this.sendToAllClients(msg);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println ("Server has stopped listening for connections.");
    
  }
  
  /*
   * Changed for E49 c) F.A
   * Override method called each time a client disconnects.
   */
  public void clientDisconnected(ConnectionToClient client) {
      String message = ("Client " + client + " has disconnected");
      System.out.println(message);
  }
  
  /*
   * Change for E49 c) EP
   * Override method called each time a client connects
   */
  @Override
  public void clientConnected(ConnectionToClient client) {
	  String message = String.format("Client %s has connected", client);
	  System.out.println(message);
  }

  /*
   * Changed for E49 c) F.A
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   */
  public void clientException(ConnectionToClient client, Throwable exception) {
      String message = ("Client " + client + " has logged off");
      System.out.println(message);
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
