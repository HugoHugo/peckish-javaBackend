import java.io.*;
import java.util.*;
import java.sql.*;

public class initDatabase{
	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		Librarian mylib = new Librarian(bundle);
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE ingredients(I_id int primary key, name text UNIQUE);");
		}catch(SQLException e){System.out.println(e.getMessage());}
		String[] temp = {"elbow macaroni","butter","dijon mustard","cayenne pepper","shredded sharp cheddar", "flour","tomatoes","dry yeast","mozarella","basil","oregano","white onions","garlic","pepperoni","mushrooms","tomato sauce","mango","apples","rice","sausages","bell peppers","arugala","bamboo shoots","cauliflower","camerbert","bacon","ricotta"};
		List<String> ingnames = Arrays.asList(temp);
		Ingredients testIng = new Ingredients();
		testIng.ingredientnames = ingnames;
		mylib.stashIngredients(testIng);
		System.out.println("Done. (Theoretically)");
	}
}