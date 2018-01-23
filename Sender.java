/* Example of network communication:  Sender.java -- RAB 1/99
   Requires two command line args:
     1.  name of host to connect to,
     2.  port number to use. */

import java.io.*;
import java.net.*;
/**
* Sender class to act as a client of the Backend class
* @see Backend
*/
public class Sender {
  static final int maxline = Message.MAXBUFF;

  /**
  * Main class that takes host and port as command line arguments
  * @param args Arguments to main
  */
  public static void main(String[] args) {
    try {
      /**
      * Host argument from user
      */
      String host = args[0];
      /**
      * port argument from user
      */
      int port = Integer.parseInt(args[1]);

      System.out.println("Initializing for network communication... ");
      /**
      * Socket for communication
      */
      Socket outSock = new Socket(host, port);
      /* assert:  socket and stream initialized */
      OutputStream outStream = outSock.getOutputStream();
      InputStream inStream = outSock.getInputStream();

      while(true){
        // outSock = new Socket(host, port);
        Message m = new Message();
        m.send(outStream);
        String endm = "END";
        if(m.content.regionMatches(0,endm,0,3) || m.content.regionMatches(0,"EOT",0,3)){
          outSock.close();
          break;
        }
        Message m2 = new Message(inStream); //Handles getFile
        if(m2.type.equals("byteData")){
          Message m3 = new Message(inStream);
        }
      }
    }
    catch (IOException e) {
      System.err.println("Sender failed.");
      System.err.println(e.getMessage());
      System.exit(1);  // an error exit status
      return;
    }
  }

}
