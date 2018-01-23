/* Example of network communication:  Receiver.java -- RAB 1/99
   Requires one command line arg:
     1.  port number to use (on this machine). */

import java.io.*;
import java.net.*;

/**
* Backend class that runs the receiver end of the server
*/
public class Backend extends Message {
  static final int maxinBuff = Message.MAXBUFF;
  /**
  * Main class that takes the port number to use as an argument
  * @param args string passed from command line representing the port
  */

  public static void main(String[] args) {
    try {
      /**
      * Represents the port on which to run this server
      */
      int port = Integer.parseInt(args[0]);
      System.out.println("Initializing for network communication... ");
      /**
      * Opens a server socket on port from user
      */
      ServerSocket servSock = new ServerSocket(port);
      /* assert:  ServerSocket successfully created */

      while (true) {
        System.out.println("Waiting for an incoming connection... ");
        Socket inSock = servSock.accept();
        Thread t = new Thread(new Worker(inSock));
        t.start();
      }
    } catch (IOException e) {
      System.err.println("Receiver failed.");
      System.err.println(e.getMessage());
      System.exit(1);  // an error exit status
      return;
    }
  }
}
