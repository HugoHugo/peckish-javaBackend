import java.io.*;
import java.util.StringTokenizer;
import com.google.gson.*;
import java.util.*;

/**
 * @author valent1
 *
 */
public class Message {

	public static final String TERMINATOR="\001";

	public static final int MAXBUFF=1000;

	public String type;

	public String content;

	public JsonObject requestContent;

	public Ingredients myIngredients;

	public List<Recipe> myRecipes;
	
	public String requestedUrl;

	/**
	* Default constructor for Message class
	*/
	public Message() {}

	/**
	* Constructor for basic field input
	* @param str1 Type of the message, usually LINE but not used really
	* @param str2 Content of the message
	*/
	public Message(String str1, String str2) {
		type = str1;
		content = str2;
	}

	public Message(String str1, String str2, String url) {
		type = str1;
		content = str2;
		requestedUrl = url;
	}

	/**
	* Constructor for Message class that takes an InputStream
	* @param is InputStream to read from, input comes from Sender
	* @throws EOFException In case it is the end of file
	* @throws IOException In case of bad user input
	*/
	public Message(InputStream is) throws EOFException, IOException {
		byte[] inputBuff = new byte[MAXBUFF];
		int countOfBytes;
		countOfBytes = is.read(inputBuff);
		String sInputBuff =toString(inputBuff);
		if(sInputBuff.regionMatches(0,"ACK",0,3)){
			type="ACK";
			content = "";
			System.out.println(type);
		}
		else if(sInputBuff.regionMatches(6,"LINE,",0,5)){
			type="LINE";
			content = sInputBuff.substring(getCommaFromByte(inputBuff)+10, getEndOfByte(inputBuff)-1);
			System.out.println("LINE Receiver Received " + countOfBytes + " bytes.");
			StringTokenizer contentTokenized = new StringTokenizer(content, TERMINATOR);
			while(contentTokenized.hasMoreTokens()){
				System.out.println(contentTokenized.nextToken());
			}
		}
		else if(sInputBuff.regionMatches(6,"getFile",0,7)){
			type="getFile";
			System.out.println("getFile Receiver Received " + countOfBytes + " bytes.");
			content = sInputBuff.substring(getCommaFromByte(inputBuff)+10, getEndOfByte(inputBuff)-1);
			StringTokenizer contentTokenized = new StringTokenizer(content, TERMINATOR);
			while(contentTokenized.hasMoreTokens()){
				System.out.println(contentTokenized.nextToken());
			}
		}
		else if(sInputBuff.regionMatches(6,"byteData",0,8)){
			type="byteData";
			System.out.println("byteData Receiver Received " + countOfBytes + " bytes.");
			content = toString(inputBuff);
			StringTokenizer contentTokenized = new StringTokenizer(content, TERMINATOR);
			while(contentTokenized.hasMoreTokens()){
				System.out.println(contentTokenized.nextToken());
			}
		}
		else if (sInputBuff.regionMatches(0,"GET",0,3)){
			if(sInputBuff.regionMatches(4,"/getallingredients",0,18)){
				requestedUrl = "/getallingredients";
			}
			type="GET";
			System.out.println("GET Receiver Received " + countOfBytes + " bytes.");
			content = toString(inputBuff);
			StringTokenizer contentTokenized = new StringTokenizer(content, TERMINATOR);
			while(contentTokenized.hasMoreTokens()){
				System.out.println(contentTokenized.nextToken());
			}
		}
		else if (sInputBuff.regionMatches(0,"POST",0,4)){
			type="POST";
			System.out.println("POST Receiver Received " + countOfBytes + " bytes.");
			System.out.println("############# DEBUG ##########\n" + sInputBuff);
			String tmp = sInputBuff.substring(getJSONContentFromByte(inputBuff), getEndOfJSONFromByte(inputBuff, getJSONContentFromByte(inputBuff)));
			System.out.println(tmp);
			content = tmp;
			Gson gson = new GsonBuilder().setLenient().create();
			requestContent = new JsonParser().parse(content).getAsJsonObject();
			System.out.println("Done parsing JSON!");
			if(requestContent.get("type").getAsString().equals("ingredients")){
				myIngredients = new Ingredients();
				myIngredients.ingredientnames = gson.fromJson(requestContent.get("ingredients"), List.class);
				System.out.println(myIngredients.ingredientnames.get(0));	
			}
		}
		else{
			type="LINE";
			System.out.println("Default Receiver Received " + countOfBytes + " bytes.");
			content = toString(inputBuff);
			StringTokenizer contentTokenized = new StringTokenizer(content, TERMINATOR);
			while(contentTokenized.hasMoreTokens()){
				System.out.println(contentTokenized.nextToken());
			}
		}
	}

	/**
	* Helper function for Message to convert string to bytes
	* @param s String
	* @return byte form of the string
	*/
	public byte[] getBytes(String s) {
		return s.getBytes();
	}

	/**
	* Helper function for Message to convert bytes to String
	* @param b byte[] argument
	* @return String version of the bytes
	*/
	public String toString(byte[] b) {
		String rVal = new String(b,0, b.length);
		return rVal;
	}

	/**
	* Helper method that finds where the string in byte representation ends with newline
	* @param userB Byte array provided by the user
	* @return i The position of the newline
	*/
	public int getNewlineFromByte(byte[] userB){
		for(int i=0; i<userB.length; ++i){
			if(userB[i] == 10){
				return i;
			}
		}
		return -1;
	}
	public int getEndOfJSONFromByte(byte[] userB, int jsonBegins){
		int rVal=-1;
		for(int i=jsonBegins; i<userB.length; ++i){
			if(userB[i] == 125){
				rVal = i; //Finds the last occurence of right closing brace and returns it
			}
		}
		++rVal;
		return rVal;
	}
	public int getJSONContentFromByte(byte[] userB){
		for(int i=0; i<userB.length; ++i){
			if(userB[i] == 13 && userB[i-1] == 10 && userB[i+1] == 10){ //find where JSON begins after header
				return i+2;
			}
		}
		return -1;
	}
	public int getCommaFromByte(byte[] userB){
		for(int i=0; i<userB.length; ++i){
			if(userB[i] == 44){
				return i;
			}
		}
		return -1;
	}
	public int getEndOfByte(byte[] userB){
		for(int i=0; i<userB.length; ++i){
			if(userB[i] == 0 && userB[i+1] == 0){ //if the next two positions in byte are 0 assume end of content
				return i-1;
			}
		}
		return -1;
	}
	/**
	* Method that transmits the message and prints its content to the user
	* @param str OutputStream to be read from
	* @throws IOException if an error in input occured
	*/
	public void send(OutputStream str) throws IOException {
		if (type == "ACK"){
			str.write(getBytes("ACK"),0,3);
			str.flush();
		}
		else if (type == "NACK"){
			str.write(getBytes("NACK"),0,4);
			str.flush();
		}
		else if (type == "byteData"){
			String finString = "[type=" + type + ", content=" + content + "]\n";
			byte[] finByte = getBytes(finString);
			int finLen = finByte.length;
			str.write(finByte,0,finLen);
			str.flush();
		}
		else{
			System.out.println("Give a line of input: ");
			byte[] oB = new byte[MAXBUFF];
			int count;
			count = System.in.read(oB);
			System.out.println("Give a type: ");
			byte[] oBtype = new byte[MAXBUFF];
			int countType;
			countType = System.in.read(oBtype);
			type = new String(oBtype,0,getNewlineFromByte(oBtype));
			content = new String(oB,0, getNewlineFromByte(oB));
			if (type.equals("LINE")){
				String finString = "[type=" + type + ", content=" + content + "]\n";
				byte[] finByte = getBytes(finString);
				int finLen = finByte.length;
				str.write(finByte,0,finLen);
				str.flush();
			}
			else if (type.equals("getFile")){
				String finString = "[type=" + type + ", content=" + content + "]\n";
				byte[] finByte = getBytes(finString);
				int finLen = finByte.length;
				str.write(finByte,0,finLen);
				str.flush();
			}
			else {
				System.out.println("No type given. Exiting...");
				System.exit(1);
			}
		}
	}
	
	public void sendData(DataOutputStream str) throws IOException {
		if (type == "GET" && requestedUrl == null){
			Gson gson = new GsonBuilder().setLenient().create();
			content = gson.toJson("GetData={hello: yeah}");
			str.writeBytes("HTTP/1.1 200 OK\nX-Powered-By: Express\nContent-Type: application/json; charset=utf-8\nConnection: close\n\n");
			str.writeBytes(gson.toJson(myRecipes));
			str.flush();
			str.close();
		}
		else if(type == "GET" && requestedUrl == "/getallingredients"){
			Gson gson = new GsonBuilder().setLenient().create();
			str.writeBytes("HTTP/1.1 200 OK\nX-Powered-By: Express\nContent-Type: application/json; charset=utf-8\nConnection: close\n\n");
			str.writeBytes(gson.toJson(myIngredients));
			str.flush();
			str.close();
		}
	}

	/**
	 * @param args Args to main
	 */
	public static void main(String[] args) {
		// Test for this class
	}

}
