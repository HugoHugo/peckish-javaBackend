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
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE recipes(R_id int primary key,rname text,steps text,numIngredients int,rating double precision,cooktime text,serving text,url text DEFAULT 'https://wp.stolaf.edu/',imageURL text DEFAULT 'https://cdn.pixabay.com/photo/2013/11/24/10/40/dessert-216870_960_720.jpg');");
		}catch(SQLException e){System.out.println(e.getMessage());}
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE IinR(R_id int references recipes(R_id),I_id int references ingredients(I_id),amount text DEFAULT 'a pinch',unique(R_id, I_id));");
		}catch(SQLException e){System.out.println(e.getMessage());}


		String[] temp = {"elbow macaroni","butter","dijon mustard","cayenne pepper","shredded sharp cheddar", "flour","tomatoes","dry yeast","mozarella","basil","oregano","white onions","garlic","pepperoni","mushrooms","tomato sauce","mango","apples","rice","sausages","bell peppers","arugala","bamboo shoots","cauliflower","camerbert","bacon","ricotta"};
		List<String> ingnames = Arrays.asList(temp);
		Ingredients testIng = new Ingredients();
		testIng.ingredientnames = ingnames;
		mylib.stashIngredients(testIng);
		testIng = new Ingredients();
		testIng.ingredientnames.add("salt");
		testIng.amounts.add("1/2 tsp");
		testIng.ingredientnames.add("elbow macaroni");
		testIng.amounts.add("2 cups");
		testIng.ingredientnames.add("butter");
		testIng.amounts.add("2 Tbsp");
		testIng.ingredientnames.add("dijon mustard");
		testIng.amounts.add("1/2 tsps");
		testIng.ingredientnames.add("cayenne pepper");
		testIng.amounts.add("1 pinch");
		testIng.ingredientnames.add("sharp cheddar");
		testIng.amounts.add("1.5 cups shredded");
		Recipe myR = new Recipe();
		myR.rname = "Macaroni and Cheese";
		myR.ingredients = testIng;
		myR.url = "BudgetBytes";
		mylib.stashRecipe(myR);
		System.out.println("Done. (Theoretically)");
	}
}