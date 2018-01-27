import java.io.*;
import java.net.*;
import java.util.*;

public class Worker implements Runnable {
  Socket sock;
  Librarian mylib;
  /**
  * Constructor to just fill in the fields
  * @param s Socket to connect to
  */
  public Worker (Socket s){
    sock = s;
    ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
    mylib = new Librarian(bundle);
  }
  /**
  * Method to run this thread of the worker. Reads in from the InputStream
  */
  public void run(){
    try{
      InputStream inStream = sock.getInputStream();
      OutputStream outStream = sock.getOutputStream();
      Ingredients tempIn = new Ingredients();
      while(true){
        Message m = new Message(inStream);
          if(m.type.equals("getFile")){
            getFile(m);
          }
          if(m.type.equals("GET")){
	    m.myIngredients = tempIn;
	    System.out.println("################# OUR INGREDIENTS ###################");
	    System.out.println(tempIn.ingredientnames.size());
            for(String s:tempIn.ingredientnames){
			System.out.println("################# OUR INGREDIENTS ###################");
			System.out.println(s);
		}
            handleGET(m);
          }
          if(m.type.equals("POST")){
            tempIn = m.myIngredients;
	    m.myIngredients = tempIn;
	    System.out.println("################# OUR INGREDIENTS ###################");
	    System.out.println(tempIn.ingredientnames.size());
            for(String s:tempIn.ingredientnames){
			System.out.println("################# OUR INGREDIENTS ###################");
			System.out.println(s);
		}
            handleGET(m);
          }
          //Message m2 = new Message("ACK","");
          //m2.send(outStream);
        String endm = "END";
        if(m.content.regionMatches(0,endm,0,3) || m.content.regionMatches(0,"EOT",0,3)){
          sock.close();
          break;
        }
      }
    }catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void handleGET(Message m){	
	try{
		
		DataOutputStream outStream = new DataOutputStream(sock.getOutputStream());
		Message m3 = new Message("GET","");
		if(m.requestedUrl == "/getallingredients"){
			m3.requestedUrl = m.requestedUrl;
			m3.myIngredients=mylib.getAllIngredients();
			m3.sendData(outStream);
		}
		else {
			m3.myRecipes=mylib.searchPotentialRecipes(m.myIngredients);
			m3.sendData(outStream);
		}
	} catch(IOException e){
		System.out.println(e.getMessage());
	}
  }

  public void getFile(Message m){
    try{
      OutputStream outStream = sock.getOutputStream();
      File testFile = new File("files/" + m.content);
      FileInputStream fromLocalFile = new FileInputStream(testFile);
      byte[] fileContent = new byte[m.MAXBUFF];
      int tFileLength;
      while((tFileLength = fromLocalFile.read(fileContent)) != -1){
        Message m3 = new Message("byteData", String.valueOf(tFileLength));
        m3.send(outStream);
        outStream.write(fileContent,0,tFileLength);
        outStream.flush();
      }
      fromLocalFile.close();
    } catch (IOException e) {
      try{
        OutputStream outStream = sock.getOutputStream();
        Message m4 = new Message("NACK","");
        m4.send(outStream);
      } catch(IOException e2) {
        System.out.println(e2.getMessage());
      }
      System.out.println(e.getMessage());
    }
  }

}
