import java.io.*;
import java.util.*;
import java.sql.*;

public class initDatabase{
	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		Librarian mylib = new Librarian(bundle);
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE ingredients(I_id int primary key, name text UNIQUE, type text);");
		}catch(SQLException e){System.out.println(e.getMessage());}
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE recipes(R_id int primary key,rname text,steps text,numIngredients int,rating double precision,cooktime text,serving text,url text DEFAULT 'https://wp.stolaf.edu/',imageURL text DEFAULT 'https://cdn.pixabay.com/photo/2013/11/24/10/40/dessert-216870_960_720.jpg',source text);");
		}catch(SQLException e){System.out.println(e.getMessage());}
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE IinR(R_id int references recipes(R_id),I_id int references ingredients(I_id),amount text DEFAULT 'a pinch',unique(R_id, I_id));");
		}catch(SQLException e){System.out.println(e.getMessage());}
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("CREATE TABLE barcodes(code text primary key,I_id references ingredients(I_id)");
		}catch(SQLException e){System.out.println(e.getMessage());}
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("GRANT ALL PRIVILEGES ON ingredients, recipes, IinR, barcodes TO irelan1,valent1,radueg1,belezn1;");
		}catch(SQLException e){System.out.println(e.getMessage());}


		String[] temp = {"water","salt","pepper"};
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
		myR.source = "BudgetBytes";
		myR.rating = 4.19;
		myR.steps = "Directions: Fill the skillet two-thirds full of water, add the salt, and bring to a boil over medium-high heat. Add the macaroni, turn the heat to medium, and cook, stirring occasionally, until just shy of al dente. This should take about 10 minutes, but check the pasta package for recommended cooking times and aim for the lower end if a range is given. (The macaroni will continue to cook a bit in the sauce.) When the macaroni is ready, biting into a piece should reveal a very thin core of uncooked pasta. Drain the macaroni and return it to the skillet. Turn the heat to low. Add the butter and stir until it melts. Add the evaporated milk, mustard, and cayenne and stir well to combine. Add the cheese in three batches, stirring frequently as each batch is added and waiting until the cheese has melted before adding the next batch. After about 5 minutes total, the sauce will be smooth and noticeably thicker. Serve hot. Leftovers can be refrigerated in a covered container for up to 2 days.";
		myR.url = "https://www.budgetbytes.com/2017/04/will-skillet-mac-cheese/";
		myR.serving = "2";
		myR.cooktime = "20 m";
		myR.imageurl = "https://www.budgetbytes.com/wp-content/uploads/2017/04/Will-It-Skillet-Mac-and-Cheese-close.jpg";
		mylib.stashRecipe(myR);
		System.out.println("Done. (Theoretically)");
	}
}